package edu.rhhs.frc.commands;

import edu.rhhs.frc.subsystems.Intake.LiftState;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class IntakeLowBarPosition extends CommandGroup {
    
    public IntakeLowBarPosition() {
        addSequential(new IntakeOuterPosition(LiftState.DOWN));
        addSequential(new IntakeInnerPosition(LiftState.DOWN));
        addSequential(new IntakeOuterSpeed(0));
        addSequential(new IntakeInnerSpeed(0));
    }
}
