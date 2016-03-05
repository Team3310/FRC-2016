package edu.rhhs.frc.commands.auton;

import edu.rhhs.frc.commands.IntakeLowBarPosition;
import edu.rhhs.frc.commands.ShooterCarriageState;
import edu.rhhs.frc.commands.ShooterWinchRetract;
import edu.rhhs.frc.commands.ShooterWinchSpoolOut;
import edu.rhhs.frc.subsystems.Shooter;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 *
 */
public class IntakeDelayedDeploy extends CommandGroup {
    
    public IntakeDelayedDeploy() {
        addSequential(new WaitCommand(0.5));
        addSequential(new IntakeLowBarPosition());
    }
}
