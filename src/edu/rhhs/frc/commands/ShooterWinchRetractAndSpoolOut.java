package edu.rhhs.frc.commands;

import edu.rhhs.frc.subsystems.Shooter;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 *
 */
public class ShooterWinchRetractAndSpoolOut extends CommandGroup {
    
    public ShooterWinchRetractAndSpoolOut() {
        addSequential(new ShooterWinchRetract());
        addSequential(new ShooterWinchSpoolOut());
    }
}
