package edu.rhhs.frc.commands;

import edu.rhhs.frc.subsystems.Shooter;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 *
 */
public class ShooterShootAndRetract extends CommandGroup {
    
    public ShooterShootAndRetract() {
        addSequential(new ShooterCarriageState(Shooter.CarriageState.RELEASED));
        addSequential(new WaitCommand(0.5));
        addSequential(new ShooterWinchRetract());
        addSequential(new ShooterWinchSpoolOut());
    }
}
