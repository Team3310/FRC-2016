package edu.rhhs.frc.subsystems;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.DrawMode;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.ShapeMode;

import edu.rhhs.frc.RobotMain;
import edu.rhhs.frc.vision.ImageProcessor;
import edu.rhhs.frc.vision.ImageProcessor.TargetInfo;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.vision.USBCamera;

public class Camera extends Subsystem
{	
    private USBCamera targetCam;
    private int imageCounter = 0;
    private ImageProcessor imageProcessor;
    private TargetInfo bestTarget;

    public Camera() {
		try {
	    	targetCam = new USBCamera("cam0");
	    	targetCam.setBrightness(0);
	    	targetCam.setExposureManual(0);
	    	targetCam.updateSettings();
	    	targetCam.startCapture();
	    	
	    	imageProcessor = new ImageProcessor();
		} 
		catch (Exception e) {
			System.err.println("An error occurred in the Camera constructor");
		}
	}
	
	@Override
	protected void initDefaultCommand() {
		
	}
	
	public void updateDashboardImage() {
    	Image currentImage = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
		targetCam.getImage(currentImage);
		
        NIVision.Rect rect = new NIVision.Rect(10, 10, 100, 100);
        NIVision.imaqDrawShapeOnImage(currentImage, currentImage, rect,
                DrawMode.DRAW_VALUE, ShapeMode.SHAPE_OVAL, 0.0f);
        
        CameraServer.getInstance().setImage(currentImage);
        
        if (currentImage != null) {
        	currentImage.free();
        }
	}
	
	public void readAndProcessImage() {
    	Image fileImage = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
		NIVision.imaqReadFile(fileImage, "/home/lvuser/image" + imageCounter + ".jpg");
		
        bestTarget = imageProcessor.findBestTarget(fileImage);
        CameraServer.getInstance().setImage(fileImage);
        
		imageCounter++;

		if (fileImage != null) {
        	fileImage.free();
        }
	}
	
	public void writeImage() {
    	Image currentImage = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
		targetCam.getImage(currentImage);

		NIVision.RGBValue rgbValues = new NIVision.RGBValue();
		NIVision.imaqWriteFile(currentImage, "/home/lvuser/image" + imageCounter + ".jpg", rgbValues);
	
		imageCounter++;
		
        if (currentImage != null) {
        	currentImage.free();
        }
	}
	
	public void updateStatus(RobotMain.OperationMode operationMode) {
		if (operationMode == RobotMain.OperationMode.TEST) {
			SmartDashboard.putNumber("Image counter", imageCounter);
			SmartDashboard.putNumber("Camera distance", bestTarget == null ? 0.0 : bestTarget.distanceToTargetFt);
			SmartDashboard.putNumber("Camera angle",  bestTarget == null ? 0.0 : bestTarget.angleToTargetDeg);
			SmartDashboard.putNumber("Camera score",  bestTarget == null ? 0.0 : bestTarget.compositeScore);
		}
	}
}