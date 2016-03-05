package edu.rhhs.frc.commands;

import edu.rhhs.frc.RobotMain;
import edu.rhhs.frc.subsystems.Manipulator;
import edu.wpi.first.wpilibj.command.Command;

public class ManipulatorMoveMP extends Command
{
	private Manipulator.PresetPositions position;
	private boolean safeDeploy;
	
	public ManipulatorMoveMP(Manipulator.PresetPositions position) {
		this(position, false);
	}

	public ManipulatorMoveMP(Manipulator.PresetPositions position, boolean safeDeploy) {
		requires(RobotMain.manipulator);
		this.position = position;
		this.safeDeploy = safeDeploy;
	}

	protected void initialize() {
		if (safeDeploy) {
			if (RobotMain.manipulator.getLeftArmAngle() > (Manipulator.CHEVAL_DE_FRISE_PARTIALLY_DEPLOYED_ANGLE_DEG - 10)) {
				RobotMain.manipulator.setPresetPosition(position);			
			}
		}
		else {
			RobotMain.manipulator.setPresetPosition(position);			
		}
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