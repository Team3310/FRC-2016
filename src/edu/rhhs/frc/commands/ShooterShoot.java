package edu.rhhs.frc.commands;

import edu.rhhs.frc.subsystems.Shooter;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 *
 */
public class ShooterShoot extends CommandGroup {
    
    public ShooterShoot(boolean checkForValidTarget) {
        addSequential(new DriveTrainHold(true));
        addSequential(new WaitCommand(0.1));
        addSequential(new ShooterCarriageState(Shooter.CarriageState.RELEASED));
        addSequential(new WaitCommand(0.1));
        addSequential(new DriveTrainHold(false));
    }
}
