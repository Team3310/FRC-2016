package edu.rhhs.frc.commands.auton;

import edu.rhhs.frc.commands.DriveTrainAbsoluteTurnMP;
import edu.rhhs.frc.commands.DriveTrainStraightMP;
import edu.rhhs.frc.commands.IntakeHelperRetract;
import edu.rhhs.frc.commands.IntakeLowBarPosition;
import edu.rhhs.frc.commands.ShooterLaserAlignmentShootAndRetract;
import edu.rhhs.frc.commands.ShooterWinchRetract;
import edu.rhhs.frc.commands.ShooterWinchSpoolOut;
import edu.rhhs.frc.subsystems.DriveTrain;
import edu.rhhs.frc.utility.MPSoftwarePIDController.MPSoftwareTurnType;
import edu.wpi.first.wpilibj.command.CommandGroup;
/**
 *
 */
public class LowBarShootLeft extends CommandGroup {
    
    public LowBarShootLeft() {
        addParallel(new IntakeLowBarPosition());
        addSequential(new ShooterWinchRetract());
        addParallel(new ShooterWinchSpoolOut());
        addSequential(new DriveTrainStraightMP(222, DriveTrain.MP_AUTON_LOWBAR_VELOCITY_INCHES_PER_SEC, true, true, 0));
        addSequential(new IntakeHelperRetract());
        addSequential(new DriveTrainAbsoluteTurnMP(60, DriveTrain.MP_AUTON_MAX_TURN_RATE_DEG_PER_SEC, MPSoftwareTurnType.TANK));
        addSequential(new DriveTrainStraightMP(70, DriveTrain.MP_AUTON_LOWBAR_VELOCITY_INCHES_PER_SEC, true, true, 60));
        addSequential(new ShooterLaserAlignmentShootAndRetract());
//        addSequential(new DriveTrainStraightMP(-68, DriveTrain.MP_AUTON_MAX_STRAIGHT_VELOCITY_INCHES_PER_SEC, true, false, 60));
//        addSequential(new DriveTrainAbsoluteTurnMP(175, DriveTrain.MP_AUTON_MAX_TURN_RATE_DEG_PER_SEC, MPSoftwareTurnType.TANK));
//        addParallel(new IntakeDelayedDeploy());
//        addSequential(new DriveTrainStraightMP(50, DriveTrain.MP_AUTON_MAX_STRAIGHT_VELOCITY_INCHES_PER_SEC, true, 175));
    }
}
