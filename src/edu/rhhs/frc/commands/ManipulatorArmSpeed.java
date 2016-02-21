package edu.rhhs.frc.commands;

import edu.rhhs.frc.RobotMain;
import edu.wpi.first.wpilibj.command.Command;

public class ManipulatorArmSpeed extends Command
{
	private double speed;
	
	public ManipulatorArmSpeed(double speed) {
		requires(RobotMain.manipulator);
		this.speed = speed;
	}

	@Override
	protected void initialize() {
		RobotMain.manipulator.setArmSpeed(speed);
	}

	@Override
	protected void execute() {
		
	}

	@Override
	protected boolean isFinished() {
		return true;
	}

	@Override
	protected void end() {
		
	}

	@Override
	protected void interrupted() {
			
	}
}