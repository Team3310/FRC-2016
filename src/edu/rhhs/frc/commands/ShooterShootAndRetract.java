package edu.rhhs.frc.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ShooterShootAndRetract extends CommandGroup {
    
    public ShooterShootAndRetract(boolean checkForValidTarget) {
        addSequential(new ShooterShoot(checkForValidTarget));
        addSequential(new ShooterWinchRetractAndSpoolOut());
    }
}
