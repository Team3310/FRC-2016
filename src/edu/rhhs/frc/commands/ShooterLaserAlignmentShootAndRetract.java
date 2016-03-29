package edu.rhhs.frc.commands;

import edu.rhhs.frc.subsystems.DriveTrain;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class ShooterLaserAlignmentShootAndRetract extends CommandGroup {
    
    public ShooterLaserAlignmentShootAndRetract() {
    	addSequential(new CameraTurnToBestTarget());
        addSequential(new CameraTurnToBestTarget());
        addSequential(new DriveTrainStraightMPLaser(24, DriveTrain.MP_LASER_SEARCH_VELOCITY_INCHES_PER_SEC, true, false, 60));
        addSequential(new CameraTurnToBestTarget());
        addSequential(new CameraTurnToBestTarget());
        addSequential(new WaitCommand(0.1));
        addSequential(new ShooterShoot(true));
        addParallel(new ShooterWinchRetractAndSpoolOut());
    }
}
