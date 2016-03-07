package edu.rhhs.frc.commands.auton;

import edu.rhhs.frc.commands.DriveTrainAbsoluteTurnMP;
import edu.rhhs.frc.commands.DriveTrainStraightMP;
import edu.rhhs.frc.subsystems.DriveTrain;
import edu.rhhs.frc.utility.MPSoftwarePIDController.MPSoftwareTurnType;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class MoatCrossAndReturn extends CommandGroup {
    
    public MoatCrossAndReturn() {
//    	addSequential(new DriveTrainSpeedShift(SpeedShiftState.HI));
        addParallel(new IntakeDelayedDeploy());
        addSequential(new DriveTrainStraightMP(162, DriveTrain.MP_AUTON_MOAT_VELOCITY_INCHES_PER_SEC, true, 0));
        addSequential(new DriveTrainAbsoluteTurnMP(181, DriveTrain.MP_AUTON_MAX_TURN_RATE_DEG_PER_SEC, MPSoftwareTurnType.TANK));
        addSequential(new DriveTrainStraightMP(128, DriveTrain.MP_AUTON_MAX_STRAIGHT_VELOCITY_INCHES_PER_SEC, true, 180));
        addSequential(new DriveTrainAbsoluteTurnMP(0, DriveTrain.MP_AUTON_MAX_TURN_RATE_DEG_PER_SEC, MPSoftwareTurnType.TANK));
    }
}
