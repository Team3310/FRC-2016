package edu.rhhs.frc.commands;

import edu.rhhs.frc.RobotMain;
import edu.rhhs.frc.subsystems.Manipulator;
import edu.wpi.first.wpilibj.command.Command;

public class ManipulatorMoveMP extends Command
{
	private Manipulator.PresetPositions position;
	
	public ManipulatorMoveMP(Manipulator.PresetPositions position) {
		requires(RobotMain.manipulator);
		this.position = position;
	}

	protected void initialize() {
		RobotMain.manipulator.setPresetPosition(position);
	}

	protected void execute() {
	}

	protected boolean isFinished() {
		return RobotMain.manipulator.isAtTarget(); 
	}

	protected void end() {
	}

	protected void interrupted() {
	}
}