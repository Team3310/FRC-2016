package edu.rhhs.frc.commands;

import edu.rhhs.frc.RobotMain;
import edu.rhhs.frc.subsystems.Shooter;
import edu.rhhs.frc.subsystems.Shooter.CarriageState;

public class ShooterWinchRetract extends ExtraTimeoutCommand
{
	private final static double CARRIAGE_PNEUMATIC_LOCK_TIMEOUT = 0.030;
	private boolean waitingForLock;
	
	public ShooterWinchRetract() {
		requires(RobotMain.shooter);
	}

	@Override
	protected void initialize() {
		waitingForLock = false;
		resetTimer();
		RobotMain.shooter.setCarriageRelease(CarriageState.RELEASED);
		RobotMain.shooter.setWinchSpeed(Shooter.WINCH_RETRACT_SPEED);
	}

	@Override
	protected void execute() {
		if (!waitingForLock) {
			if (RobotMain.shooter.isCarriageRetracted() || RobotMain.shooter.isWinchCurrentAtMax()) {
				RobotMain.shooter.setCarriageRelease(CarriageState.LOCKED);
				startExtraTimeout(CARRIAGE_PNEUMATIC_LOCK_TIMEOUT);
				waitingForLock = true;
			}
		}
	}

	@Override
	protected boolean isFinished() {
		return isExtraTimedOut();
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