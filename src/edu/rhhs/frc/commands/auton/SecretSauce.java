package edu.rhhs.frc.commands.auton;

import edu.rhhs.frc.commands.DriveTrainAbsoluteTurnMP;
import edu.rhhs.frc.commands.DriveTrainGyroOffset;
import edu.rhhs.frc.commands.DriveTrainRelativeTurnPID;
import edu.rhhs.frc.commands.DriveTrainStraightMP;
import edu.rhhs.frc.commands.IntakeFullyDeploy;
import edu.rhhs.frc.commands.IntakeFullyRetract;
import edu.rhhs.frc.commands.IntakeOuterSpeed;
import edu.rhhs.frc.subsystems.DriveTrain;
import edu.rhhs.frc.utility.MPSoftwarePIDController.MPSoftwareTurnType;
import edu.wpi.first.wpilibj.command.CommandGroup;
/**
 *
 */
public class SecretSauce extends CommandGroup {
    
    public SecretSauce() {
        addParallel(new IntakeOuterSpeed(1.0));
        addSequential(new DriveTrainRelativeTurnPID(10, MPSoftwareTurnType.TANK));
        addSequential(new IntakeFullyDeploy());
        addSequential(new DriveTrainStraightMP(-10, 75, true, false, 0));
        addSequential(new IntakeFullyRetract());
        addSequential(new DriveTrainStraightMP(20, 75, true, false, 0));
    	addSequential(new DriveTrainGyroOffset(-100.0));
        addSequential(new DriveTrainAbsoluteTurnMP(0, DriveTrain.MP_AUTON_MAX_TURN_RATE_DEG_PER_SEC, MPSoftwareTurnType.LEFT_SIDE_ONLY));
        addSequential(new RoughTerrainPosition5CenterShootCenter());
    }
}
