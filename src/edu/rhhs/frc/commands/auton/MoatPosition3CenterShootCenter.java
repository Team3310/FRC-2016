package edu.rhhs.frc.commands.auton;

import edu.rhhs.frc.commands.DriveTrainStraightMP;
import edu.rhhs.frc.commands.ShooterWinchRetract;
import edu.rhhs.frc.commands.ShooterWinchSpoolOut;
import edu.rhhs.frc.subsystems.DriveTrain;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class MoatPosition3CenterShootCenter extends CommandGroup {
    
    public MoatPosition3CenterShootCenter() {
        addSequential(new ShooterWinchRetract());
        addParallel(new ShooterWinchSpoolOut());
        addParallel(new IntakeDelayedDeploy());
        addSequential(new DriveTrainStraightMP(187, DriveTrain.MP_AUTON_MOAT_VELOCITY_INCHES_PER_SEC, true, true, 0));
        addSequential(new Position3CenterShootCenter());
    }
}
