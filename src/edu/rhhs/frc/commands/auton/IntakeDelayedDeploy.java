package edu.rhhs.frc.commands.auton;

import edu.rhhs.frc.commands.IntakeLowBarPosition;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 *
 */
public class IntakeDelayedDeploy extends CommandGroup {
    
    public IntakeDelayedDeploy() {
        addSequential(new WaitCommand(0.1));
        addSequential(new IntakeLowBarPosition());
    }
}
