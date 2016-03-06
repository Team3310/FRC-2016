package edu.rhhs.frc.commands.auton;

import edu.rhhs.frc.commands.DriveTrainAbsoluteTurnMP;
import edu.rhhs.frc.commands.DriveTrainStraightMP;
import edu.rhhs.frc.commands.ShooterCarriageState;
import edu.rhhs.frc.commands.ShooterShoot;
import edu.rhhs.frc.commands.ShooterWinchRetract;
import edu.rhhs.frc.commands.ShooterWinchRetractAndSpoolOut;
import edu.rhhs.frc.commands.ShooterWinchSpoolOut;
import edu.rhhs.frc.subsystems.DriveTrain;
import edu.rhhs.frc.subsystems.Shooter;
import edu.rhhs.frc.utility.MPSoftwarePIDController.MPSoftwareTurnType;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 *
 */
public class LowBarCrossAndReturn extends CommandGroup {
    
    public LowBarCrossAndReturn() {
        addParallel(new IntakeDelayedDeploy());
        addSequential(new DriveTrainStraightMP(142, DriveTrain.MP_AUTON_MAX_STRAIGHT_VELOCITY_INCHES_PER_SEC, true, 0));
        addSequential(new DriveTrainAbsoluteTurnMP(181, DriveTrain.MP_AUTON_MAX_TURN_RATE_DEG_PER_SEC, MPSoftwareTurnType.TANK));
        addSequential(new DriveTrainStraightMP(108, DriveTrain.MP_AUTON_MAX_STRAIGHT_VELOCITY_INCHES_PER_SEC, true, 180));
        addSequential(new DriveTrainAbsoluteTurnMP(0, DriveTrain.MP_AUTON_MAX_TURN_RATE_DEG_PER_SEC, MPSoftwareTurnType.TANK));
    }
}