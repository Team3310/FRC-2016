package edu.rhhs.frc.commands.auton;

import edu.rhhs.frc.commands.DriveTrainAbsoluteTurnMP;
import edu.rhhs.frc.commands.DriveTrainStraightMP;
import edu.rhhs.frc.commands.IntakeInnerPosition;
import edu.rhhs.frc.commands.IntakeOuterPosition;
import edu.rhhs.frc.commands.ShooterLaserAlignmentShootAndRetract;
import edu.rhhs.frc.subsystems.DriveTrain;
import edu.rhhs.frc.subsystems.Intake.LiftState;
import edu.rhhs.frc.utility.MPSoftwarePIDController.MPSoftwareTurnType;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class Position5CenterShootRight extends CommandGroup {
    
    public Position5CenterShootRight() {
        // Starting point is 30" from the outerworks ramp/carpet point in the courtyard
        addSequential(new DriveTrainStraightMP(125, DriveTrain.MP_AUTON_MAX_STRAIGHT_VELOCITY_INCHES_PER_SEC, true, true, 0));
        addSequential(new IntakeOuterPosition(LiftState.UP));
        addSequential(new IntakeInnerPosition(LiftState.DOWN));
        addSequential(new DriveTrainAbsoluteTurnMP(300, DriveTrain.MP_AUTON_MAX_TURN_RATE_DEG_PER_SEC, MPSoftwareTurnType.TANK));
        addSequential(new ShooterLaserAlignmentShootAndRetract());
//        addSequential(new DriveTrainStraightMP(-22, DriveTrain.MP_AUTON_MAX_STRAIGHT_VELOCITY_INCHES_PER_SEC, true, true, 60));
//        addSequential(new DriveTrainAbsoluteTurnMP(175, DriveTrain.MP_AUTON_MAX_TURN_RATE_DEG_PER_SEC, MPSoftwareTurnType.TANK));
//        addSequential(new DriveTrainStraightMP(109, DriveTrain.MP_AUTON_MAX_STRAIGHT_VELOCITY_INCHES_PER_SEC, true, true, 175));
    }
}
