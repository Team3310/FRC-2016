package edu.rhhs.frc.commands.auton;

import edu.rhhs.frc.commands.DriveTrainGyroOffset;
import edu.rhhs.frc.commands.DriveTrainStraightMP;
import edu.rhhs.frc.commands.ShooterWinchRetract;
import edu.rhhs.frc.commands.ShooterWinchSpoolOut;
import edu.rhhs.frc.subsystems.DriveTrain;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class RampartsPosition2LeftShootLeft extends CommandGroup {
    
    public RampartsPosition2LeftShootLeft() {
        addSequential(new ShooterWinchRetract());
        addParallel(new ShooterWinchSpoolOut());
        addParallel(new IntakeDelayedDeploy());
    	addSequential(new DriveTrainGyroOffset(-6.0));
        addSequential(new DriveTrainStraightMP(150, DriveTrain.MP_AUTON_MOAT_VELOCITY_INCHES_PER_SEC, true, false, 0));
        addSequential(new Position2CenterShootLeft());
    }
}
