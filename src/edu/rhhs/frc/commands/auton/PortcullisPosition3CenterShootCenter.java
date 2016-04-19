package edu.rhhs.frc.commands.auton;

import edu.rhhs.frc.commands.DriveTrainStraightMP;
import edu.rhhs.frc.commands.ManipulatorMoveMP;
import edu.rhhs.frc.commands.ShooterWinchRetract;
import edu.rhhs.frc.commands.ShooterWinchSpoolOut;
import edu.rhhs.frc.subsystems.DriveTrain;
import edu.rhhs.frc.subsystems.Manipulator.PresetPositions;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class PortcullisPosition3CenterShootCenter extends CommandGroup {
    
    public PortcullisPosition3CenterShootCenter() {
    	addSequential(new ShooterWinchRetract());
        addParallel(new ShooterWinchSpoolOut());
        addParallel(new IntakeDelayedDeploy());
        addParallel(new ManipulatorMoveMP(PresetPositions.FULLY_DEPLOYED));
        addSequential(new DriveTrainStraightMP(140, DriveTrain.MP_AUTON_PORTCULLIS_VELOCITY_INCHES_PER_SEC, true, true, 0));
        addParallel(new ManipulatorMoveMP(PresetPositions.ZERO));
        addSequential(new Position3CenterShootCenter());
    }
}
