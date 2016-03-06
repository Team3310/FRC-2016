package edu.rhhs.frc.commands.auton;

import edu.rhhs.frc.commands.DriveTrainAbsoluteTurnMP;
import edu.rhhs.frc.commands.DriveTrainStraightMP;
import edu.rhhs.frc.commands.IntakeFullyRetract;
import edu.rhhs.frc.commands.IntakeLowBarPosition;
import edu.rhhs.frc.commands.ShooterShoot;
import edu.rhhs.frc.commands.ShooterWinchRetractAndSpoolOut;
import edu.rhhs.frc.subsystems.DriveTrain;
import edu.rhhs.frc.utility.MPSoftwarePIDController.MPSoftwareTurnType;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class LowBarShootOneHigh extends CommandGroup {
    
    public LowBarShootOneHigh() {
        addParallel(new IntakeDelayedDeploy());
        addSequential(new DriveTrainStraightMP(215, DriveTrain.MP_AUTON_MAX_STRAIGHT_VELOCITY_INCHES_PER_SEC, true, 0));
        addSequential(new DriveTrainAbsoluteTurnMP(60, DriveTrain.MP_AUTON_MAX_TURN_RATE_DEG_PER_SEC, MPSoftwareTurnType.TANK));
        addSequential(new DriveTrainStraightMP(84, DriveTrain.MP_AUTON_MAX_STRAIGHT_VELOCITY_INCHES_PER_SEC, true, 60));
        addSequential(new ShooterShoot());
        addParallel(new ShooterWinchRetractAndSpoolOut());
        addParallel(new IntakeFullyRetract());
        addSequential(new DriveTrainStraightMP(-84, DriveTrain.MP_AUTON_MAX_STRAIGHT_VELOCITY_INCHES_PER_SEC, true, 60));
        addSequential(new DriveTrainAbsoluteTurnMP(175, DriveTrain.MP_AUTON_MAX_TURN_RATE_DEG_PER_SEC, MPSoftwareTurnType.TANK));
        addParallel(new IntakeDelayedDeploy());
        addSequential(new DriveTrainStraightMP(176, DriveTrain.MP_AUTON_MAX_STRAIGHT_VELOCITY_INCHES_PER_SEC, true, 175));
        addSequential(new IntakeFullyRetract());
        addSequential(new DriveTrainAbsoluteTurnMP(0, DriveTrain.MP_AUTON_MAX_TURN_RATE_DEG_PER_SEC, MPSoftwareTurnType.TANK));
    }
}
