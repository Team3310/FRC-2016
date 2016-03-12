package edu.rhhs.frc.commands.auton;

import edu.rhhs.frc.commands.CameraTurnToBestTarget;
import edu.rhhs.frc.commands.DriveTrainAbsoluteTurnMP;
import edu.rhhs.frc.commands.DriveTrainStraightMP;
import edu.rhhs.frc.commands.IntakeHelperRetract;
import edu.rhhs.frc.commands.ManipulatorMoveMP;
import edu.rhhs.frc.commands.ShooterShoot;
import edu.rhhs.frc.commands.ShooterWinchRetract;
import edu.rhhs.frc.commands.ShooterWinchRetractAndSpoolOut;
import edu.rhhs.frc.commands.ShooterWinchSpoolOut;
import edu.rhhs.frc.subsystems.DriveTrain;
import edu.rhhs.frc.subsystems.Manipulator;
import edu.rhhs.frc.subsystems.Manipulator.PresetPositions;
import edu.rhhs.frc.utility.MPSoftwarePIDController.MPSoftwareTurnType;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 *
 */
public class LowBarShootLeft extends CommandGroup {
    
    public LowBarShootLeft() {
        addSequential(new ShooterWinchRetract());
        addParallel(new ShooterWinchSpoolOut());
        addParallel(new IntakeDelayedDeploy());
        addParallel(new ManipulatorMoveMP(PresetPositions.FULLY_DEPLOYED, Manipulator.Attachment.CHEVAL_DE_FRISE));
        addSequential(new DriveTrainStraightMP(212, DriveTrain.MP_AUTON_LOWBAR_VELOCITY_INCHES_PER_SEC, true, true, 0));
        addParallel(new ManipulatorMoveMP(PresetPositions.RETRACTED, Manipulator.Attachment.CHEVAL_DE_FRISE));
        addSequential(new IntakeHelperRetract());
        addSequential(new DriveTrainAbsoluteTurnMP(60, DriveTrain.MP_AUTON_MAX_TURN_RATE_DEG_PER_SEC, MPSoftwareTurnType.TANK));
        addSequential(new DriveTrainStraightMP(52, DriveTrain.MP_AUTON_LOWBAR_VELOCITY_INCHES_PER_SEC, true, true, 60));
        addSequential(new WaitCommand(0.1));
        addSequential(new CameraTurnToBestTarget());
        addSequential(new CameraTurnToBestTarget());
        addSequential(new CameraTurnToBestTarget());
        addSequential(new WaitCommand(0.1));
        addSequential(new DriveTrainStraightMP(42, DriveTrain.MP_AUTON_LOWBAR_VELOCITY_INCHES_PER_SEC, true, false, 60));
        addSequential(new WaitCommand(0.1));
        addSequential(new ShooterShoot(true));
//        addParallel(new ShooterWinchRetractAndSpoolOut());
//        addSequential(new DriveTrainStraightMP(-68, DriveTrain.MP_AUTON_MAX_STRAIGHT_VELOCITY_INCHES_PER_SEC, true, false, 60));
//        addSequential(new DriveTrainAbsoluteTurnMP(175, DriveTrain.MP_AUTON_MAX_TURN_RATE_DEG_PER_SEC, MPSoftwareTurnType.TANK));
//        addParallel(new IntakeDelayedDeploy());
//        addSequential(new DriveTrainStraightMP(50, DriveTrain.MP_AUTON_MAX_STRAIGHT_VELOCITY_INCHES_PER_SEC, true, 175));
    }
}
