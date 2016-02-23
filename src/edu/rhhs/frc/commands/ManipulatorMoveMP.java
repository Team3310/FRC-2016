package edu.rhhs.frc.commands;

import edu.rhhs.frc.RobotMain;
import edu.rhhs.frc.subsystems.DriveTrain.DriveTrainControlMode;
import edu.wpi.first.wpilibj.command.Command;

public class ManipulatorMoveMP extends Command
{
	private double angleDegrees, maxVelocityDegreesPerSec;

	public ManipulatorMoveMP(double angleDegrees, double maxVelocityDegreesPerSec) {
		requires(RobotMain.manipulator);
		this.angleDegrees = angleDegrees;
		this.maxVelocityDegreesPerSec = maxVelocityDegreesPerSec;
	}

	protected void initialize() {
		RobotMain.manipulator.setPositionMP(angleDegrees, maxVelocityDegreesPerSec);
	}

	protected void execute() {
	}

	protected boolean isFinished() {
		return RobotMain.manipulator.isAtTarget(); 
	}

	protected void end() {
	}

	protected void interrupted() {
	}
}