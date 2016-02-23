package edu.rhhs.frc.subsystems;

import java.util.ArrayList;

import edu.rhhs.frc.RobotMain;
import edu.rhhs.frc.RobotMap;
import edu.rhhs.frc.utility.CANTalonEncoder;
import edu.rhhs.frc.utility.ControlLoopable;
import edu.rhhs.frc.utility.MotionProfileController;
import edu.rhhs.frc.utility.PIDParams;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDeviceStatus;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Manipulator extends Subsystem implements ControlLoopable
{
	private static final double ENCODER_TICKS_TO_WORLD = 4096.0 / 360.0 * 16.0 / 18.0;

	private ArrayList<CANTalonEncoder> motorControllers = new ArrayList<CANTalonEncoder>();	
	private CANTalonEncoder leftArm, rightArm;
	private MotionProfileController mpController;
	private PIDParams mpPIDParams = new PIDParams(0.1, 0, 0, .01, 0.18);
	private boolean isAtTarget = true;
	
	public Manipulator() {
		try {
			leftArm = new CANTalonEncoder(RobotMap.MANIPULATOR_LEFT_MOTOR_CAN_ID, ENCODER_TICKS_TO_WORLD, false, FeedbackDevice.CtreMagEncoder_Relative);
			rightArm = new CANTalonEncoder(RobotMap.MANIPULATOR_RIGHT_MOTOR_CAN_ID, ENCODER_TICKS_TO_WORLD, true, FeedbackDevice.CtreMagEncoder_Relative);
	
			leftArm.reverseSensor(true);
			leftArm.reverseOutput(false);
			
			rightArm.reverseSensor(false);
			rightArm.reverseOutput(true);
			
			leftArm.enableBrakeMode(true);
			rightArm.enableBrakeMode(true);
	
			motorControllers.add(rightArm);
		}
		catch (Exception e) {
			System.err.println("An error occurred in the Manipulator constructor");
		}
	}
	
	public void setPositionMP(double angleDegrees, double maxVelocityDegreePerSec) {
		mpController.setMPTarget(angleDegrees, maxVelocityDegreePerSec, false, 0); 
		isAtTarget = false;
	}
	
	public void setZeroPosition() {
		mpController.setZeroPosition();
	}
	
	public void setArmSpeed(double speed) {
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
			SmartDashboard.putNumber("Left Arm", leftArm.getPosition());
			SmartDashboard.putNumber("Right Arm", rightArm.getPosition());

			FeedbackDeviceStatus status = leftArm.isSensorPresent(FeedbackDevice.CtreMagEncoder_Relative);
			SmartDashboard.putString("Left Arm Encoder Status", status.name());
		}
	}

}