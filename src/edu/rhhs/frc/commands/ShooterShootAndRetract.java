package edu.rhhs.frc.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ShooterShootAndRetract extends CommandGroup {
    
    public ShooterShootAndRetract() {
        addSequential(new ShooterShoot());
        addSequential(new ShooterWinchRetractAndSpoolOut());
    }
}
