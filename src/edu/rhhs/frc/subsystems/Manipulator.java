package edu.rhhs.frc.subsystems;

import java.util.ArrayList;

import edu.rhhs.frc.RobotMain;
import edu.rhhs.frc.RobotMap;
import edu.rhhs.frc.utility.CANTalonEncoder;
import edu.rhhs.frc.utility.ControlLoopable;
import edu.rhhs.frc.utility.MotionProfileController;
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
	
	public static final double RETRACTED_ANGLE_DEG = 0;
	public static final double DEPLOYED_ANGLE_DEG = 170;
	public static final double RETRACT_MAX_RATE_DEG_PER_SEC = 450;
	public static final double DEPLOY_MAX_RATE_DEG_PER_SEC = 450;
	
	private ArrayList<CANTalonEncoder> motorControllers = new ArrayList<CANTalonEncoder>();	
	private CANTalonEncoder leftArm, rightArm;
	private MotionProfileController mpController;
	private PIDParams mpPIDParams = new PIDParams(5.0, 0.0, 0, 0.0, 0.2);
	private boolean isAtTarget = true;
	
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
	
	public void setPositionMP(double targetAngleDegrees, double maxVelocityDegreePerSec) {
		double startAngleDegrees = (rightArm.getPositionWorld() + leftArm.getPositionWorld())/2;
		mpController.setMPTarget(startAngleDegrees, targetAngleDegrees, maxVelocityDegreePerSec, false); 
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
		mpController = new MotionProfileController(periodMs, mpPIDParams, motorControllers);
		
		// Set the startup position to zero
		setZeroPosition();
	}
	
	public void updateStatus(RobotMain.OperationMode operationMode) {
		if (operationMode == RobotMain.OperationMode.TEST) {
			SmartDashboard.putNumber("Left Arm", leftArm.getPositionWorld());
			SmartDashboard.putNumber("Right Arm", rightArm.getPositionWorld());
			MotionProfilePoint mpPoint = mpController.getCurrentPoint(); 
			double delta = mpPoint != null ? rightArm.getPositionWorld() - mpController.getCurrentPoint().position : 0;
			SmartDashboard.putNumber("Right Arm Delta", delta);
			SmartDashboard.putNumber("Right Arm Target", rightArm.getSetpoint());
		}
	}

}