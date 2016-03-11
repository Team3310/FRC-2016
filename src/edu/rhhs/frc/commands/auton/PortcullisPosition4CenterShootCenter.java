package edu.rhhs.frc.commands.auton;

import edu.rhhs.frc.commands.DriveTrainStraightMP;
import edu.rhhs.frc.commands.ManipulatorMoveMP;
import edu.rhhs.frc.subsystems.DriveTrain;
import edu.rhhs.frc.subsystems.Manipulator.PresetPositions;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class PortcullisPosition4CenterShootCenter extends CommandGroup {
    
    public PortcullisPosition4CenterShootCenter() {
    	addSequential(new ManipulatorMoveMP(PresetPositions.FULLY_DEPLOYED));
        addSequential(new DriveTrainStraightMP(140, DriveTrain.MP_AUTON_MOAT_VELOCITY_INCHES_PER_SEC, true, true, 0));
    	addSequential(new ManipulatorMoveMP(PresetPositions.RETRACTED));
        addSequential(new Position4CenterShootCenter());
    }
}
