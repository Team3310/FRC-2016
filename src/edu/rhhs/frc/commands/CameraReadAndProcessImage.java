package edu.rhhs.frc.commands;

import edu.rhhs.frc.RobotMain;
import edu.wpi.first.wpilibj.command.Command;

public class CameraReadAndProcessImage extends Command
{
	public CameraReadAndProcessImage() {
		requires(RobotMain.camera);
	}

	@Override
	protected void initialize() {
		RobotMain.camera.readAndProcessImage();
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