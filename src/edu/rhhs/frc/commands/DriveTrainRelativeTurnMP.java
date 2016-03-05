package edu.rhhs.frc.commands;

import edu.rhhs.frc.RobotMain;
import edu.rhhs.frc.subsystems.DriveTrain.DriveTrainControlMode;
import edu.rhhs.frc.utility.MPSoftwarePIDController.MPTurnType;
import edu.wpi.first.wpilibj.command.Command;

public class DriveTrainRelativeTurnMP extends Command
{
	private double relativeTurnAngleDeg, maxTurnRateDegPerSec;
	private MPTurnType turnType;

	public DriveTrainRelativeTurnMP(double relativeTurnAngleDeg, double maxTurnRateDegPerSec, MPTurnType turnType) {
		requires(RobotMain.driveTrain);
		this.relativeTurnAngleDeg = relativeTurnAngleDeg;
		this.maxTurnRateDegPerSec = maxTurnRateDegPerSec;
		this.turnType = turnType;
	}

	protected void initialize() {
		RobotMain.driveTrain.setRelativeTurnMP(relativeTurnAngleDeg, maxTurnRateDegPerSec, turnType);
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