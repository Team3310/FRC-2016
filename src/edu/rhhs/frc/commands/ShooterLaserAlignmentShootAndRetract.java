package edu.rhhs.frc.commands;

import edu.rhhs.frc.subsystems.DriveTrain;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class ShooterLaserAlignmentShootAndRetract extends CommandGroup {
    
    public ShooterLaserAlignmentShootAndRetract() {
    	addSequential(new CameraTurnToBestTarget());
        addSequential(new DriveTrainStraightMPLaser(48, DriveTrain.MP_LASER_SEARCH_VELOCITY_INCHES_PER_SEC, true, false, 60));
        addSequential(new CameraTurnToBestTarget());
        addSequential(new ShooterShoot(true));
        addParallel(new ShooterWinchRetractAndSpoolOut());
    }
}
