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
public class RampartsPosition4LeftShootCenter extends CommandGroup {
    
    public RampartsPosition4LeftShootCenter() {
        addSequential(new ShooterWinchRetract());
        addParallel(new ShooterWinchSpoolOut());
    	addSequential(new DriveTrainGyroOffset(-6.0));
        addSequential(new DriveTrainStraightMP(140, DriveTrain.MP_AUTON_MOAT_VELOCITY_INCHES_PER_SEC, true, true, 0));
        addSequential(new Position4CenterShootCenter());
    }
}
