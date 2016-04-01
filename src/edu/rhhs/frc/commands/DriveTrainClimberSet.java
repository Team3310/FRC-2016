package edu.rhhs.frc.commands;

import edu.rhhs.frc.RobotMain;
import edu.rhhs.frc.subsystems.DriveTrain.ClimberState;
import edu.wpi.first.wpilibj.command.Command;

public class DriveTrainClimberSet extends Command
{
	private ClimberState state;
	
	public DriveTrainClimberSet(ClimberState state) {
		requires(RobotMain.driveTrain);
		this.state = state;
	}

	@Override
	protected void initialize() {
		RobotMain.driveTrain.setClimberState(state);
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