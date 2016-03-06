package edu.rhhs.frc.subsystems;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.DrawMode;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.ShapeMode;

import edu.rhhs.frc.RobotMain;
import edu.rhhs.frc.vision.ImageProcessor;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.image.NIVisionException;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.vision.USBCamera;

public class Camera extends Subsystem
{	
    private USBCamera targetCam;
    private int imageCounter = 0;

    public Camera() {
		try {
	    	targetCam = new USBCamera("cam0");
	    	targetCam.setBrightness(0);
	    	targetCam.setExposureManual(0);
	    	targetCam.updateSettings();
	    	targetCam.startCapture();
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
	
	public void locateBestTarget() {
//		targetCam.getImage(currentImage);
		
        ImageProcessor imageProcessor = new ImageProcessor();
//        imageProcessor.findBestTarget(currentImage, null);
	}
	
	public void updateStatus(RobotMain.OperationMode operationMode) {
		if (operationMode == RobotMain.OperationMode.TEST) {
			SmartDashboard.putNumber("Image counter", imageCounter);
		}
	}
}