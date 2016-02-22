package edu.rhhs.frc.commands;

import edu.wpi.first.wpilibj.command.Command;

public class ResetNavX extends Command
{
	public ResetNavX() {

	}
	
	protected void initialize() {
		// Robot.drivetrainSub.setDisplacementZero();
		// Robot.drivetrainSub.setYawAngleZero();
		System.out.println("Reset IMU " + System.currentTimeMillis());
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