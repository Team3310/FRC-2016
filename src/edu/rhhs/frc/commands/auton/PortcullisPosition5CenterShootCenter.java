package edu.rhhs.frc.commands.auton;

import edu.rhhs.frc.commands.DriveTrainStraightMP;
import edu.rhhs.frc.commands.IntakeInnerPosition;
import edu.rhhs.frc.commands.IntakeOuterPosition;
import edu.rhhs.frc.commands.ManipulatorMoveMP;
import edu.rhhs.frc.commands.ShooterWinchRetract;
import edu.rhhs.frc.commands.ShooterWinchSpoolOut;
import edu.rhhs.frc.subsystems.DriveTrain;
import edu.rhhs.frc.subsystems.Intake.LiftState;
import edu.rhhs.frc.subsystems.Manipulator.PresetPositions;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class PortcullisPosition5CenterShootCenter extends CommandGroup {
    
    public PortcullisPosition5CenterShootCenter() {
        addSequential(new ShooterWinchRetract());
        addParallel(new ShooterWinchSpoolOut());
        addSequential(new IntakeOuterPosition(LiftState.DOWN));
        addSequential(new IntakeInnerPosition(LiftState.DOWN));
        addParallel(new ManipulatorMoveMP(PresetPositions.FULLY_DEPLOYED));
        addSequential(new DriveTrainStraightMP(132, DriveTrain.MP_AUTON_CDF_VELOCITY_INCHES_PER_SEC, true, true, 0));
        addParallel(new ManipulatorMoveMP(PresetPositions.ZERO));
        addSequential(new Position5CenterShootCenter());
    }
}
