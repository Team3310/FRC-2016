package edu.rhhs.frc.commands;

import edu.rhhs.frc.RobotMain;
import edu.rhhs.frc.subsystems.DriveTrain.DriveTrainControlMode;
import edu.wpi.first.wpilibj.command.Command;

public class DriveTrainStraightMP extends Command
{
	private double distanceInches, maxVelocityInchesPerSec, desiredAbsoluteAngle;
	private boolean useGyroLock;

	public DriveTrainStraightMP(double distanceInches, double maxVelocityInchesPerSec, boolean useGyroLock, double desiredAbsoluteAngle) {
		requires(RobotMain.driveTrain);
		this.distanceInches = distanceInches;
		this.maxVelocityInchesPerSec = maxVelocityInchesPerSec;
		this.desiredAbsoluteAngle = desiredAbsoluteAngle;
		this.useGyroLock = useGyroLock;
	}

	protected void initialize() {
		RobotMain.driveTrain.setStraightMP(distanceInches, maxVelocityInchesPerSec, useGyroLock, desiredAbsoluteAngle);
	}

	protected void execute() {
	}

	protected boolean isFinished() {
		return RobotMain.driveTrain.isControlLoopEnabled(); 
	}

	protected void end() {
	}

	protected void interrupted() {
		RobotMain.driveTrain.setControlMode(DriveTrainControlMode.JOYSTICK);
	}
}