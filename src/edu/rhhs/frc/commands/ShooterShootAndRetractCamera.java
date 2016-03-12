package edu.rhhs.frc.commands;

import edu.rhhs.frc.subsystems.DriveTrain;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ShooterShootAndRetractCamera extends CommandGroup {
    
    public ShooterShootAndRetractCamera() {
        addSequential(new CameraTurnToBestTarget());
        addSequential(new CameraTurnToBestTarget());
        addSequential(new DriveTrainStraightMP(15, DriveTrain.MP_AUTON_MAX_STRAIGHT_VELOCITY_INCHES_PER_SEC, true, false, 60));
        addSequential(new ShooterShoot(true));
        addSequential(new ShooterWinchRetractAndSpoolOut());
    }
}
