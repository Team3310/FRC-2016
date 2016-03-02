package edu.rhhs.frc.commands;

import edu.rhhs.frc.RobotMain;
import edu.rhhs.frc.subsystems.DriveTrain.DriveTrainControlMode;
import edu.rhhs.frc.utility.MPTalonPIDController.MPTurnType;
import edu.wpi.first.wpilibj.command.Command;

public class DriveTrainTurnMP extends Command
{
	private double turnAngleDeg, maxTurnRateDegPerSec;
	private MPTurnType turnType;

	public DriveTrainTurnMP(double turnAngleDeg, double maxTurnRateDegPerSec, MPTurnType turnType) {
		requires(RobotMain.driveTrain);
		this.turnAngleDeg = turnAngleDeg;
		this.maxTurnRateDegPerSec = maxTurnRateDegPerSec;
		this.turnType = turnType;
	}

	protected void initialize() {
		RobotMain.driveTrain.setTurnMP(turnAngleDeg, maxTurnRateDegPerSec, turnType);
	}

	protected void execute() {
	}

	protected boolean isFinished() {
		return RobotMain.driveTrain.isFinished(); 
	}

	protected void end() {
		RobotMain.driveTrain.setControlMode(DriveTrainControlMode.JOYSTICK);
	}

	protected void interrupted() {
		end();
	}
}