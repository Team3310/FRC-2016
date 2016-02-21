package edu.rhhs.frc.commands;

import edu.rhhs.frc.RobotMain;
import edu.rhhs.frc.subsystems.DriveTrain.SpeedShiftState;
import edu.rhhs.frc.subsystems.DriveTrain.PTOShiftState;
import edu.wpi.first.wpilibj.command.Command;

public class DriveTrainPTOShift extends Command
{
	private PTOShiftState state;
	
	public DriveTrainPTOShift(PTOShiftState state) {
		requires(RobotMain.driveTrain);
		this.state = state;
	}

	@Override
	protected void initialize() {
		RobotMain.driveTrain.setPTOState(state);
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