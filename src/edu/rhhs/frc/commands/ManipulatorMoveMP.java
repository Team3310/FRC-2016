package edu.rhhs.frc.commands;

import edu.rhhs.frc.RobotMain;
import edu.rhhs.frc.subsystems.Manipulator;
import edu.wpi.first.wpilibj.command.Command;

public class ManipulatorMoveMP extends Command
{
	private double targetAngleDegrees, maxVelocityDegreesPerSec;

	public ManipulatorMoveMP(Manipulator.ArmState armState) {
		requires(RobotMain.manipulator);
		if (armState == Manipulator.ArmState.DEPLOY) {
			this.targetAngleDegrees = Manipulator.DEPLOYED_ANGLE_DEG;
			this.maxVelocityDegreesPerSec =  Manipulator.DEPLOY_MAX_RATE_DEG_PER_SEC;
		}
		else if (armState == Manipulator.ArmState.RETRACT) {
			this.targetAngleDegrees = Manipulator.RETRACTED_ANGLE_DEG;
			this.maxVelocityDegreesPerSec =  Manipulator.RETRACT_MAX_RATE_DEG_PER_SEC;
		}
	}

	protected void initialize() {
		RobotMain.manipulator.setPositionMP(targetAngleDegrees, maxVelocityDegreesPerSec);
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