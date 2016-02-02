package edu.rhhs.frc.subsystems;

import edu.rhhs.frc.utility.CANTalonEncoder;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class MPDrivetrainTest extends MPSubsystem
{
	private CANTalonEncoder rightFrontMotor;
	private CANTalonEncoder leftFrontMotor;
	private CANTalon rightRearMotor;
	private CANTalon leftRearMotor;

	private static final long loopPeriodMs = 10;
	private static final double encoderTicksPerInch = 52.15189;

	public MPDrivetrainTest() {
		super(loopPeriodMs);

		leftFrontMotor = new CANTalonEncoder(1, encoderTicksPerInch);
		leftFrontMotor.reverseSensor(true);
		leftFrontMotor.reverseOutput(false);
		addMotorController(leftFrontMotor);

		rightFrontMotor = new CANTalonEncoder(2, encoderTicksPerInch);
		rightFrontMotor.reverseSensor(false);
		rightFrontMotor.reverseOutput(true);
		addMotorController(rightFrontMotor);

		leftRearMotor = new CANTalon(3);
		leftRearMotor.changeControlMode(TalonControlMode.Follower);
		leftRearMotor.set(leftFrontMotor.getDeviceID());

		rightRearMotor = new CANTalon(4);
		rightRearMotor.changeControlMode(TalonControlMode.Follower);
		rightRearMotor.set(rightFrontMotor.getDeviceID());

		setPID(1, 0, 0, .01, 0.18);
	}

	public void initDefaultCommand() {
	}

	public void updateStatus() {
		SmartDashboard.putNumber("Left Drive", leftFrontMotor.getPositionWorld());
		SmartDashboard.putNumber("Right Drive", rightFrontMotor.getPositionWorld());
		SmartDashboard.putNumber("MP Position", getMPPosition());
		SmartDashboard.putNumber("MP Velocity", getMPVelocity());
	}
}