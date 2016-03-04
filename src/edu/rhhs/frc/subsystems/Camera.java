package edu.rhhs.frc.subsystems;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.DrawMode;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.ShapeMode;

import edu.rhhs.frc.RobotMain;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.vision.USBCamera;

public class Camera extends Subsystem
{	
    private USBCamera targetCam;
    private Image currentImage;

    public Camera() {
		try {
	    	targetCam = new USBCamera("cam0");
	    	targetCam.setBrightness(10);
	    	targetCam.setExposureManual(10);
	    	targetCam.updateSettings();
	    	targetCam.startCapture();
	    	
	    	currentImage = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
		} 
		catch (Exception e) {
			System.err.println("An error occurred in the Camera constructor");
		}
	}
	
	@Override
	protected void initDefaultCommand() {
		
	}
	
	public void updateDashboardImage() {
		targetCam.getImage(currentImage);
		
        NIVision.Rect rect = new NIVision.Rect(10, 10, 100, 100);
        NIVision.imaqDrawShapeOnImage(currentImage, currentImage, rect,
                DrawMode.DRAW_VALUE, ShapeMode.SHAPE_OVAL, 0.0f);
        
        CameraServer.getInstance().setImage(currentImage);
	}
	
	public void updateStatus(RobotMain.OperationMode operationMode) {
		if (operationMode == RobotMain.OperationMode.TEST) {
		}
	}
}