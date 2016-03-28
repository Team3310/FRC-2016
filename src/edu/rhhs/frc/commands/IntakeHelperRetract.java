package edu.rhhs.frc.commands;

import edu.rhhs.frc.subsystems.Intake.LiftState;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 *
 */
public class IntakeHelperRetract extends CommandGroup {
    
    public IntakeHelperRetract() {
        addSequential(new IntakeInnerPosition(LiftState.UP));
//        addSequential(new WaitCommand(0.2));
        addSequential(new IntakeOuterPosition(LiftState.UP));
        addSequential(new IntakeOuterSpeed(0));
        addSequential(new IntakeInnerSpeed(0));
        addSequential(new WaitCommand(0.8));
        addSequential(new IntakeInnerPosition(LiftState.DOWN));
    }
}
