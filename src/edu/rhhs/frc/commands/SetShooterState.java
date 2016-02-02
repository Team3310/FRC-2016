package edu.rhhs.frc.commands;

import edu.rhhs.frc.RobotMain;
import edu.rhhs.frc.subsystems.PneumaticShooter.ModuleState;
import edu.wpi.first.wpilibj.command.Command;

public class SetShooterState extends Command
{
	private ModuleState state;
	
	public SetShooterState(ModuleState state) {
		requires(RobotMain.shooter);
		this.state = state;
	}

	@Override
	protected void initialize() {
		RobotMain.shooter.setState(state);
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