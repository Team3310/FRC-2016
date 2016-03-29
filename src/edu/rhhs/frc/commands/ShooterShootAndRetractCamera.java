package edu.rhhs.frc.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class ShooterShootAndRetractCamera extends CommandGroup {
    
    public ShooterShootAndRetractCamera() {
        addSequential(new CameraTurnToBestTarget());
//        addSequential(new CameraTurnToBestTarget());
        addSequential(new ShooterShoot(true));
        addSequential(new ShooterWinchRetractAndSpoolOut());
    }
}