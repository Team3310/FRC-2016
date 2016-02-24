package edu.rhhs.frc.commands;

import edu.rhhs.frc.RobotMain;
import edu.wpi.first.wpilibj.command.Command;

public class ResetGyro extends Command
{
	public ResetGyro() {
	}

	protected void initialize() {
		//RobotMain.driveTrain.getGyro().reset();
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