package edu.rhhs.frc.commands;

import edu.rhhs.frc.RobotMain;
import edu.rhhs.frc.subsystems.IntakeSubsystem.LiftState;
import edu.wpi.first.wpilibj.command.Command;

public class IntakeInnerPosition extends Command
{
	private LiftState ls;
	
	public IntakeInnerPosition(LiftState ls) {
		requires(RobotMain.intake);
		this.ls = ls;
	}

	@Override
	protected void initialize() {
		RobotMain.intake.setPositionInner(ls);
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