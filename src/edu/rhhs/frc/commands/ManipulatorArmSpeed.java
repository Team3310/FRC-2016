package edu.rhhs.frc.commands;

import edu.rhhs.frc.RobotMain;
import edu.rhhs.frc.subsystems.Manipulator.ArmSide;
import edu.wpi.first.wpilibj.command.Command;

public class ManipulatorArmSpeed extends Command
{
	private double speed;
	private ArmSide side;
	
	public ManipulatorArmSpeed(double speed, ArmSide side) {
		requires(RobotMain.manipulator);
		this.speed = speed;
		this.side = side;
	}

	@Override
	protected void initialize() {
		RobotMain.manipulator.setArmSpeed(speed, side);
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