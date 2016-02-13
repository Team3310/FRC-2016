package edu.rhhs.frc.commands;

import edu.rhhs.frc.RobotMain;
import edu.wpi.first.wpilibj.command.Command;

public class IntakeSetInnerSpeed extends Command
{
	private double speed;
	
	public IntakeSetInnerSpeed(double speed) {
		requires(RobotMain.intake);
		this.speed = speed;
	}

	@Override
	protected void initialize() {
		RobotMain.intake.setSpeedInner(speed);
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