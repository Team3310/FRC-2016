package edu.rhhs.frc.subsystems;

import java.util.ArrayList;

import edu.rhhs.frc.RobotMap;
import edu.rhhs.frc.subsystems.DriveTrain.DriveTrainControlMode;
import edu.rhhs.frc.utility.CANTalonEncoder;
import edu.rhhs.frc.utility.ControlLoopable;
import edu.rhhs.frc.utility.MotionProfileController;
import edu.rhhs.frc.utility.PIDParams;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Manipulator extends Subsystem implements ControlLoopable
{
	private static final double ENCODER_TICKS_TO_WORLD = 4096.0 / 360.0 * 16.0 / 18.0;

	private ArrayList<CANTalonEncoder> motorControllers = new ArrayList<CANTalonEncoder>();	
	private CANTalonEncoder leftArm, rightArm;
	private MotionProfileController mpController;
	private PIDParams mpPIDParams = new PIDParams(0.1, 0, 0, .01, 0.18);
	private boolean atSetPoint = true;
	
	public Manipulator() {
		leftArm = new CANTalonEncoder(RobotMap.MANIPULATOR_LEFT_MOTOR_ID, ENCODER_TICKS_TO_WORLD, false);
		leftArm.reverseOutput(true);
		rightArm = new CANTalonEncoder(RobotMap.MANIPULATOR_RIGHT_MOTOR_ID, ENCODER_TICKS_TO_WORLD, true);
		motorControllers.add(rightArm);
	}
	
	public void setPositionMP(double angleDegrees, double maxVelocityDegreePerSec) {
		mpController.setMPTarget(angleDegrees, maxVelocityDegreePerSec, false, 0); 
		atSetPoint = false;
	}
	
	public void setArmSpeed(double speed) {
		leftArm.set(speed);
		rightArm.set(speed);
	}

	protected void initDefaultCommand() {
		
	}
	
	public void updateStatus() {
		SmartDashboard.putNumber("Left Arm", leftArm.getPosition());
		SmartDashboard.putNumber("Right Arm", rightArm.getPosition());
	}

	@Override
	public void controlLoopUpdate() {
		if (atSetPoint == false) {
			atSetPoint = mpController.controlLoopUpdate(0);
		}
	}

	@Override
	public void setPeriodMs(long periodMs) {
		mpController = new MotionProfileController(periodMs, mpPIDParams, motorControllers);
		mpController.setZeroPosition();
	}
}