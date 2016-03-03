package edu.rhhs.frc.subsystems;

import java.util.ArrayList;

import edu.rhhs.frc.RobotMain;
import edu.rhhs.frc.RobotMap;
import edu.rhhs.frc.utility.CANTalonEncoder;
import edu.rhhs.frc.utility.ControlLoopable;
import edu.rhhs.frc.utility.MPTalonPIDController;
import edu.rhhs.frc.utility.MotionProfilePoint;
import edu.rhhs.frc.utility.PIDParams;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Manipulator extends Subsystem implements ControlLoopable
{
	private static final double ENCODER_TICKS_TO_WORLD = (4096.0 / 360.0) * (18.0 / 16.0);  
	public static enum ArmState { RETRACT, DEPLOY };
	public static enum Attachment { CHEVAL_DE_FRISE, PORTCULLIS };
	public static enum PresetPositions { RETRACTED, ZERO, PARTIALLY_DEPLOYED, FULLY_DEPLOYED };
	
	public static final double PORTCULLIS_RETRACTED_ANGLE_DEG = -10;
	public static final double PORTCULLIS_ZERO_ANGLE_DEG = 0;
	public static final double PORTCULLIS_PARTIALLY_DEPLOYED_ANGLE_DEG = 150;
	public static final double PORTCULLIS_FULLY_DEPLOYED_ANGLE_DEG = 180;

	public static final double CHEVAL_DE_FRISE_RETRACTED_ANGLE_DEG = -10;
	public static final double CHEVAL_DE_FRISE_ZERO_ANGLE_DEG = 0;
	public static final double CHEVAL_DE_FRISE_PARTIALLY_DEPLOYED_ANGLE_DEG = 165;
	public static final double CHEVAL_DE_FRISE_FULLY_DEPLOYED_ANGLE_DEG = 180;

	public static final double RETRACT_MAX_RATE_DEG_PER_SEC = 650;
	public static final double DEPLOY_MAX_RATE_DEG_PER_SEC = 650;
	
	private ArrayList<CANTalonEncoder> motorControllers = new ArrayList<CANTalonEncoder>();	
	private CANTalonEncoder leftArm, rightArm;
	private MPTalonPIDController mpController;
	private PIDParams mpPIDParams = new PIDParams(4.0, 0.0, 0, 0.0, 0.2);
	private boolean isAtTarget = true;
	private Attachment attachment;
	
	public Manipulator() {
		try {
			leftArm = new CANTalonEncoder(RobotMap.MANIPULATOR_LEFT_MOTOR_CAN_ID, ENCODER_TICKS_TO_WORLD, false, FeedbackDevice.QuadEncoder);
			rightArm = new CANTalonEncoder(RobotMap.MANIPULATOR_RIGHT_MOTOR_CAN_ID, ENCODER_TICKS_TO_WORLD, true, FeedbackDevice.QuadEncoder);
	
			leftArm.reverseSensor(true);
			leftArm.reverseOutput(true);
			
			rightArm.reverseSensor(false);
			rightArm.reverseOutput(false);
			
			leftArm.enableBrakeMode(true);
			rightArm.enableBrakeMode(true);
	
			motorControllers.add(leftArm);
			motorControllers.add(rightArm);
			
			mpPIDParams.iZone = 128;
		}
		catch (Exception e) {
			System.err.println("An error occurred in the Manipulator constructor");
		}
	}
	
	public Attachment getAttachment() {
		return attachment;
	}

	public void setAttachment(Attachment attachment) {
		this.attachment = attachment;
	}

	public void setPresetPosition(PresetPositions position) {
		double targetAngleDegrees = 0;
		if (position == PresetPositions.RETRACTED) {
			targetAngleDegrees = (attachment == Attachment.PORTCULLIS) ? PORTCULLIS_RETRACTED_ANGLE_DEG : CHEVAL_DE_FRISE_RETRACTED_ANGLE_DEG;
		}
		else if (position == PresetPositions.ZERO) {
			targetAngleDegrees = (attachment == Attachment.PORTCULLIS) ? PORTCULLIS_ZERO_ANGLE_DEG : CHEVAL_DE_FRISE_ZERO_ANGLE_DEG;
		}
		else if (position == PresetPositions.PARTIALLY_DEPLOYED) {
			targetAngleDegrees = (attachment == Attachment.PORTCULLIS) ? PORTCULLIS_PARTIALLY_DEPLOYED_ANGLE_DEG : CHEVAL_DE_FRISE_PARTIALLY_DEPLOYED_ANGLE_DEG;
		}
		else if (position == PresetPositions.FULLY_DEPLOYED) {
			targetAngleDegrees = (attachment == Attachment.PORTCULLIS) ? PORTCULLIS_FULLY_DEPLOYED_ANGLE_DEG : CHEVAL_DE_FRISE_FULLY_DEPLOYED_ANGLE_DEG;
		}
		
		double startAngleDegrees = (rightArm.getPositionWorld() + leftArm.getPositionWorld())/2;
		double maxVelocityDegreePerSec = (targetAngleDegrees > startAngleDegrees) ? DEPLOY_MAX_RATE_DEG_PER_SEC : RETRACT_MAX_RATE_DEG_PER_SEC;
		mpController.setMPTarget(startAngleDegrees, targetAngleDegrees, maxVelocityDegreePerSec); 
		isAtTarget = false;
	}
	
	public void setPositionMP(double targetAngleDegrees, double maxVelocityDegreePerSec) {
		double startAngleDegrees = (rightArm.getPositionWorld() + leftArm.getPositionWorld())/2;
		mpController.setMPTarget(startAngleDegrees, targetAngleDegrees, maxVelocityDegreePerSec); 
		isAtTarget = false;
	}
	
	public void setZeroPosition() {
		mpController.setZeroPosition();
	}
	
	public void getRightArmAngle() {
		rightArm.getPositionWorld();
	}
	
	public void getLeftArmAngle() {
		leftArm.getPositionWorld();
	}
	
	public void setArmSpeed(double speed) {
		leftArm.changeControlMode(TalonControlMode.PercentVbus);
		rightArm.changeControlMode(TalonControlMode.PercentVbus);
		leftArm.set(speed);
		rightArm.set(speed);
	}

	protected void initDefaultCommand() {
	}
	
	@Override
	public void controlLoopUpdate() {
		if (isAtTarget == false) {
			isAtTarget = mpController.controlLoopUpdate();
		}
	}
	
	public boolean isAtTarget() {
		return isAtTarget;
	}

	@Override
	public void setPeriodMs(long periodMs) {
		mpController = new MPTalonPIDController(periodMs, mpPIDParams, motorControllers);
		
		// Set the startup position to zero
		setZeroPosition();
	}
	
	public void updateStatus(RobotMain.OperationMode operationMode) {
		if (operationMode == RobotMain.OperationMode.TEST) {
			SmartDashboard.putNumber("Left Arm", leftArm.getPositionWorld());
			SmartDashboard.putNumber("Right Arm", rightArm.getPositionWorld());
//			MotionProfilePoint mpPoint = mpController.getCurrentPoint(); 
//			double delta = mpPoint != null ? rightArm.getPositionWorld() - mpController.getCurrentPoint().position : 0;
//			SmartDashboard.putNumber("Right Arm Delta", delta);
		}
	}

}