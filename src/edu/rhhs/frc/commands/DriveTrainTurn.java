package edu.rhhs.frc.commands;

import edu.rhhs.frc.RobotMain;
import edu.rhhs.frc.subsystems.DriveTrain.ControlMode;
import edu.wpi.first.wpilibj.command.Command;

public class DriveTrainTurn extends Command
{
	private double target, errorDeg;

	public DriveTrainTurn(double target) {
		requires(RobotMain.driveTrain);
		this.target = target;
	}

	public DriveTrainTurn(double target, double errorDeg) {
		requires(RobotMain.driveTrain);
		this.target = target;
		this.errorDeg = errorDeg;
	}

	protected void initialize() {
		RobotMain.driveTrain.setYawAngleZero();
//		RobotMain.driveTrain.setSetpoint(target);
//		RobotMain.driveTrain.getPIDController().setAbsoluteTolerance(errorDeg);
		RobotMain.driveTrain.setControlMode(ControlMode.SOFTWARE_TURN);
//		RobotMain.driveTrain.enable();
	}

	protected void execute() {
	}

	protected boolean isFinished() {
//		return Math.abs(RobotMain.driveTrain.getError()) < 5 && Math.abs(RobotMain.driveTrain.getYawRateDegPerSec()) < 1;
		return false; //TODO: Temporary?
	}

	protected void end() {
		RobotMain.driveTrain.setControlMode(ControlMode.DRIVER);
		RobotMain.driveTrain.disableControlLoop();
	}

	protected void interrupted() {
		end();
	}
}