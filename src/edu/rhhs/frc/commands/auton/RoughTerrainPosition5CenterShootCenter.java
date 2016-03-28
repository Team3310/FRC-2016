package edu.rhhs.frc.commands.auton;

import edu.rhhs.frc.commands.DriveTrainStraightMP;
import edu.rhhs.frc.commands.ShooterWinchRetract;
import edu.rhhs.frc.commands.ShooterWinchSpoolOut;
import edu.rhhs.frc.subsystems.DriveTrain;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class RoughTerrainPosition5CenterShootCenter extends CommandGroup {
    
    public RoughTerrainPosition5CenterShootCenter() {
        addSequential(new ShooterWinchRetract());
        addParallel(new ShooterWinchSpoolOut());
        addSequential(new DriveTrainStraightMP(172, DriveTrain.MP_AUTON_MOAT_VELOCITY_INCHES_PER_SEC, true, true, 0));
        addSequential(new Position5CenterShootCenter());
    }
}