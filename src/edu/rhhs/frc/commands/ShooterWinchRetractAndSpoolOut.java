package edu.rhhs.frc.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class ShooterWinchRetractAndSpoolOut extends CommandGroup {
    
    public ShooterWinchRetractAndSpoolOut() {
        addSequential(new ShooterWinchRetract());
        addParallel(new ShooterWinchSpoolOut());
    }
}
