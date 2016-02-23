package edu.rhhs.frc.commands;

import edu.rhhs.frc.RobotMain;
import edu.wpi.first.wpilibj.command.Command;

public class DriveTrainSpeed extends Command
{
	private double speed;
	
    public DriveTrainSpeed(double speed) {
        requires(RobotMain.driveTrain);
        this.speed = speed;
    }

    protected void initialize() {
    	RobotMain.driveTrain.setSpeed(speed);
    }

    protected void execute() {
    	
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    	
    }

    protected void interrupted() {
    	
    }
}