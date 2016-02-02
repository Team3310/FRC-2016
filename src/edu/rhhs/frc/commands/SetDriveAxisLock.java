package edu.rhhs.frc.commands;

import edu.rhhs.frc.RobotMain;
import edu.wpi.first.wpilibj.command.Command;

public class SetDriveAxisLock extends Command
{
	private boolean locked;

	public SetDriveAxisLock(boolean locked) {
		requires(RobotMain.driveTrain);
		this.locked = locked;
	}

	protected void initialize() {
		// RobotMain.driveTrain.setYawAngleZero();
		RobotMain.driveTrain.setAxisLocked(locked);
	}

	protected void execute() {
	}

	protected boolean isFinished() {
		return true;
	}

	protected void end() {
	}

	protected void interrupted() {
	}
}