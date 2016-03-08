package edu.rhhs.frc.commands.auton;

import edu.rhhs.frc.commands.CameraTurnToBestTarget;
import edu.rhhs.frc.commands.DriveTrainAbsoluteTurnMP;
import edu.rhhs.frc.commands.DriveTrainStraightMP;
import edu.rhhs.frc.commands.IntakeFullyRetract;
import edu.rhhs.frc.commands.ShooterShoot;
import edu.rhhs.frc.commands.ShooterWinchRetractAndSpoolOut;
import edu.rhhs.frc.subsystems.DriveTrain;
import edu.rhhs.frc.utility.MPSoftwarePIDController.MPSoftwareTurnType;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 *
 */
public class LowBarShootHigh extends CommandGroup {
    
    public LowBarShootHigh() {
        addParallel(new IntakeDelayedDeploy());
        addSequential(new DriveTrainStraightMP(215, DriveTrain.MP_AUTON_MAX_STRAIGHT_VELOCITY_INCHES_PER_SEC, true, true, 0));
        addParallel(new IntakeFullyRetract());
        addSequential(new DriveTrainAbsoluteTurnMP(60, DriveTrain.MP_AUTON_MAX_TURN_RATE_DEG_PER_SEC, MPSoftwareTurnType.TANK));
        addSequential(new DriveTrainStraightMP(62, DriveTrain.MP_AUTON_MAX_STRAIGHT_VELOCITY_INCHES_PER_SEC, true, true, 60));
        addSequential(new WaitCommand(0.1));
        addSequential(new CameraTurnToBestTarget());
        addSequential(new CameraTurnToBestTarget());
        addSequential(new WaitCommand(0.1));
        addSequential(new DriveTrainStraightMP(22, DriveTrain.MP_AUTON_MAX_STRAIGHT_VELOCITY_INCHES_PER_SEC, true, false, 60));
        addSequential(new WaitCommand(0.1));
        addSequential(new ShooterShoot());
        addParallel(new ShooterWinchRetractAndSpoolOut());
//        addSequential(new DriveTrainStraightMP(-84, DriveTrain.MP_AUTON_MAX_STRAIGHT_VELOCITY_INCHES_PER_SEC, true, 60));
//        addSequential(new DriveTrainAbsoluteTurnMP(175, DriveTrain.MP_AUTON_MAX_TURN_RATE_DEG_PER_SEC, MPSoftwareTurnType.TANK));
//        addParallel(new IntakeDelayedDeploy());
//        addSequential(new DriveTrainStraightMP(50, DriveTrain.MP_AUTON_MAX_STRAIGHT_VELOCITY_INCHES_PER_SEC, true, 175));
    }
}
