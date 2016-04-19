package edu.rhhs.frc.commands.auton;

import edu.rhhs.frc.commands.DriveTrainAbsoluteTurnMP;
import edu.rhhs.frc.commands.DriveTrainStraightMP;
import edu.rhhs.frc.commands.IntakeInnerPosition;
import edu.rhhs.frc.commands.IntakeOuterPosition;
import edu.rhhs.frc.commands.ManipulatorMoveMP;
import edu.rhhs.frc.commands.ShooterWinchRetract;
import edu.rhhs.frc.commands.ShooterWinchSpoolOut;
import edu.rhhs.frc.subsystems.DriveTrain;
import edu.rhhs.frc.subsystems.Intake.LiftState;
import edu.rhhs.frc.subsystems.Manipulator.PresetPositions;
import edu.rhhs.frc.utility.MPSoftwarePIDController.MPSoftwareTurnType;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class PortcullisPosition4CenterShootCenter extends CommandGroup {
    
    public PortcullisPosition4CenterShootCenter() {
        addSequential(new ShooterWinchRetract());
        addParallel(new ShooterWinchSpoolOut());
        addParallel(new IntakeOuterPosition(LiftState.DOWN));
        addParallel(new IntakeInnerPosition(LiftState.DOWN));
        addParallel(new ManipulatorMoveMP(PresetPositions.FULLY_DEPLOYED));
        addSequential(new DriveTrainStraightMP(140, DriveTrain.MP_AUTON_CDF_VELOCITY_INCHES_PER_SEC, true, true, 0));
        addParallel(new ManipulatorMoveMP(PresetPositions.ZERO));
        addSequential(new DriveTrainAbsoluteTurnMP(-9, DriveTrain.MP_AUTON_MAX_TURN_RATE_DEG_PER_SEC, MPSoftwareTurnType.TANK));
        addSequential(new Position4CenterShootCenter());
    }
}
