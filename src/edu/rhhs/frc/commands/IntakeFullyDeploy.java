package edu.rhhs.frc.commands;

import edu.rhhs.frc.subsystems.Intake;
import edu.rhhs.frc.subsystems.Intake.LiftState;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class IntakeFullyDeploy extends CommandGroup {
    
    public IntakeFullyDeploy() {
        addSequential(new IntakeOuterPosition(LiftState.DOWN));
        addSequential(new IntakeInnerPosition(LiftState.UP));
        addSequential(new IntakeOuterSpeed(Intake.OUTER_INTAKE_LOAD_SPEED));
        addSequential(new IntakeInnerSpeed(Intake.INNER_INTAKE_LOAD_SPEED));
    }
}
