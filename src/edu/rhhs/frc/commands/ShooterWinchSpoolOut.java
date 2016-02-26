package edu.rhhs.frc.commands;

import edu.rhhs.frc.RobotMain;
import edu.rhhs.frc.subsystems.Shooter;
import edu.wpi.first.wpilibj.command.Command;

public class ShooterWinchSpoolOut extends Command
{	
	public ShooterWinchSpoolOut() {
		requires(RobotMain.shooter);
	}

	@Override
	protected void initialize() {
		RobotMain.shooter.resetWinchEncoder();
		RobotMain.shooter.setWinchSpeed(Shooter.WINCH_SPOOLOUT_SPEED);
		
	}

	@Override
	protected void execute() {
	}

	@Override
	protected boolean isFinished() {
		return RobotMain.shooter.isWinchSpooledOut();
	}

	@Override
	protected void end() {
		RobotMain.shooter.setWinchSpeed(0.0);		
	}

	@Override
	protected void interrupted() {
		end();
	}
}