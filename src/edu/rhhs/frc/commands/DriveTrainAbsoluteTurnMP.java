package edu.rhhs.frc.commands;

import edu.rhhs.frc.RobotMain;
import edu.rhhs.frc.subsystems.DriveTrain.DriveTrainControlMode;
import edu.rhhs.frc.utility.MPSoftwarePIDController.MPSoftwareTurnType;
import edu.wpi.first.wpilibj.command.Command;

public class DriveTrainAbsoluteTurnMP extends Command
{
	private double absoluteTurnAngleDeg, maxTurnRateDegPerSec;
	private MPSoftwareTurnType turnType;

	public DriveTrainAbsoluteTurnMP(double absoluteTurnAngleDeg, double maxTurnRateDegPerSec, MPSoftwareTurnType turnType) {
		requires(RobotMain.driveTrain);
		this.absoluteTurnAngleDeg = absoluteTurnAngleDeg;
		this.maxTurnRateDegPerSec = maxTurnRateDegPerSec;
		this.turnType = turnType;
	}

	protected void initialize() {
		RobotMain.driveTrain.setAbsoluteTurnMP(absoluteTurnAngleDeg, maxTurnRateDegPerSec, turnType);
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