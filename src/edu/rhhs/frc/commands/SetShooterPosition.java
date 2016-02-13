package edu.rhhs.frc.commands;

import edu.rhhs.frc.RobotMain;
import edu.rhhs.frc.subsystems.ShooterSubsystem.ShotPosition;
import edu.wpi.first.wpilibj.command.Command;

public class SetShooterPosition extends Command
{
	private ShotPosition state;
	
	public SetShooterPosition(ShotPosition state) {
		requires(RobotMain.shooter);
		this.state = state;
	}

	@Override
	protected void initialize() {
		RobotMain.shooter.setShotPosition(state);
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