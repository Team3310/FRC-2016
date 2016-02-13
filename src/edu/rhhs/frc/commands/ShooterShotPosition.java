package edu.rhhs.frc.commands;

import edu.rhhs.frc.RobotMain;
import edu.rhhs.frc.subsystems.ShooterSubsystem.ShotPosition;
import edu.wpi.first.wpilibj.command.Command;

public class ShooterShotPosition extends Command
{
	private ShotPosition sp;
	
	public ShooterShotPosition(ShotPosition sp) {
		requires(RobotMain.shooter);
		this.sp = sp;
	}

	@Override
	protected void initialize() {
		RobotMain.shooter.setShotPosition(sp);
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