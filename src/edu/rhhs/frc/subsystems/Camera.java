package edu.rhhs.frc.subsystems;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;

import edu.rhhs.frc.RobotMain;
import edu.rhhs.frc.RobotMain.OperationMode;
import edu.rhhs.frc.vision.ImageProcessor;
import edu.rhhs.frc.vision.ImageProcessor.TargetInfo;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.vision.USBCamera;

public class Camera extends Subsystem
{	
    private USBCamera targetCam;
    private ImageProcessor imageProcessor;
	private Image currentImage = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);

    private TargetInfo bestTarget;
	private int imageCounter = 0;
	private long processTimeMs = 0;
	private double offsetAngleDeg = 0;
	private boolean lastTargetValid = false;
	private boolean alignmentFinished = false;

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
	
	public void postCameraImageToDashboard() {
		targetCam.getImage(currentImage);        
        CameraServer.getInstance().setImage(currentImage);
	}
	
	public void readImagePostProcessedImageToDashboard() {
    	try {
    		NIVision.imaqReadFile(currentImage, "/home/lvuser/image" + imageCounter + ".jpg");
    	}
    	catch (Exception e) {
    		imageCounter = 0;
    		NIVision.imaqReadFile(currentImage, "/home/lvuser/image" + imageCounter + ".jpg");
    	}
        bestTarget = imageProcessor.findBestTarget(currentImage, true);
        CameraServer.getInstance().setImage(currentImage);
        
		imageCounter++;
	}
	
	public TargetInfo readImageGetBestTarget() {
		long startTime = System.currentTimeMillis();
    	try {
    		NIVision.imaqReadFile(currentImage, "/home/lvuser/image" + imageCounter + ".jpg");
    	}
    	catch (Exception e) {
    		imageCounter = 0;
    		NIVision.imaqReadFile(currentImage, "/home/lvuser/image" + imageCounter + ".jpg");
    	}
		imageCounter++;

		bestTarget = imageProcessor.findBestTarget(currentImage, false);
		processTimeMs = System.currentTimeMillis() - startTime;
		
		return bestTarget;
	}
	
	public boolean isTargetValid() {
		return lastTargetValid;
	}
	
	public TargetInfo getBestTarget() {
		lastTargetValid = false;
    	try {
			targetCam.getImage(currentImage);        
			bestTarget = imageProcessor.findBestTarget(currentImage, RobotMain.operationMode == OperationMode.TEST);
			
			if (bestTarget != null) {
				bestTarget.angleToTargetDeg += offsetAngleDeg;
				if (bestTarget.compositeScore < ImageProcessor.MINIMUM_VALID_COMPOSITE_SCORE) {
					lastTargetValid = true;
				}
			}
			
			if (RobotMain.operationMode == OperationMode.TEST) {
				CameraServer.getInstance().setImage(currentImage);
			}		
			
			return bestTarget;
    	}
    	catch (Exception e) {
    		return null;
    	}
	}
	
	public void writeImage() {
		targetCam.getImage(currentImage);

		NIVision.RGBValue rgbValues = new NIVision.RGBValue();
		NIVision.imaqWriteFile(currentImage, "/home/lvuser/image" + imageCounter + ".jpg", rgbValues);
	
		imageCounter++;
	}
	
	public void incrementAngleOffset(double deltaAngle) {
		offsetAngleDeg += deltaAngle;
	}
	
	
	
	public boolean isAlignmentFinished() {
		return alignmentFinished;
	}

	public void setAlignmentFinished(boolean state) {
		if (state && lastTargetValid) {
			alignmentFinished = true;
		}
		else {
			alignmentFinished = false;
		}
		
		return;
	}

	public void updateStatus(RobotMain.OperationMode operationMode) {
		if (operationMode == RobotMain.OperationMode.TEST) {
			SmartDashboard.putNumber("Image Counter", imageCounter);
			SmartDashboard.putNumber("Camera Distance", bestTarget == null ? 0.0 : bestTarget.distanceToTargetFt);
			SmartDashboard.putNumber("Camera Angle",  bestTarget == null ? 0.0 : bestTarget.angleToTargetDeg);
			SmartDashboard.putNumber("Camera Score",  bestTarget == null ? 0.0 : bestTarget.compositeScore);
			SmartDashboard.putNumber("Camera Time ms",  processTimeMs);
			SmartDashboard.putNumber("Camera Offset",  offsetAngleDeg);
			SmartDashboard.putBoolean("Camera Aligned",  alignmentFinished);
		}
	}
}