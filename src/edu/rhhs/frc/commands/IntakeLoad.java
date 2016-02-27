package edu.rhhs.frc.commands;

import edu.rhhs.frc.subsystems.Intake;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class IntakeLoad extends CommandGroup {
    
    public IntakeLoad() {
        addSequential(new IntakeOuterSpeed(Intake.OUTER_INTAKE_LOAD_SPEED));
        addSequential(new IntakeInnerSpeed(Intake.INNER_INTAKE_LOAD_SPEED));
    }
}
