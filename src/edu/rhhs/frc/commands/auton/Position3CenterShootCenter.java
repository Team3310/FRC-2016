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
public class Position3CenterShootCenter extends CommandGroup {
    
    public Position3CenterShootCenter() {
        addSequential(new DriveTrainAbsoluteTurnMP(45, DriveTrain.MP_AUTON_MAX_TURN_RATE_DEG_PER_SEC, MPSoftwareTurnType.TANK));
        addSequential(new DriveTrainStraightMP(60, DriveTrain.MP_AUTON_MOAT_VELOCITY_INCHES_PER_SEC, true, true, 45));
        addSequential(new DriveTrainAbsoluteTurnMP(0, DriveTrain.MP_AUTON_MAX_TURN_RATE_DEG_PER_SEC, MPSoftwareTurnType.TANK));
        addSequential(new DriveTrainStraightMP(20, DriveTrain.MP_AUTON_MOAT_VELOCITY_INCHES_PER_SEC, true, true, 0));
        addSequential(new IntakeOuterPosition(LiftState.UP));
        addSequential(new IntakeInnerPosition(LiftState.DOWN));
        addSequential(new ShooterLaserAlignmentShootAndRetract());
//        addSequential(new DriveTrainStraightMP(-23, DriveTrain.MP_AUTON_MAX_STRAIGHT_VELOCITY_INCHES_PER_SEC, true, 0));
//        addSequential(new DriveTrainAbsoluteTurnMP(45, DriveTrain.MP_AUTON_MAX_TURN_RATE_DEG_PER_SEC, MPSoftwareTurnType.TANK));
//        addSequential(new DriveTrainStraightMP(-54, DriveTrain.MP_AUTON_MAX_STRAIGHT_VELOCITY_INCHES_PER_SEC, true, 45));
//        addSequential(new DriveTrainAbsoluteTurnMP(180, DriveTrain.MP_AUTON_MAX_TURN_RATE_DEG_PER_SEC, MPSoftwareTurnType.TANK));
//        addSequential(new DriveTrainStraightMP(17, DriveTrain.MP_AUTON_MAX_STRAIGHT_VELOCITY_INCHES_PER_SEC, true, 180));
    }
}
