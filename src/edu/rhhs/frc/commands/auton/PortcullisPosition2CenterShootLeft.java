package edu.rhhs.frc.commands.auton;

import edu.rhhs.frc.commands.DriveTrainStraightMP;
import edu.rhhs.frc.commands.ManipulatorMoveMP;
import edu.rhhs.frc.subsystems.DriveTrain;
import edu.rhhs.frc.subsystems.Manipulator.PresetPositions;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class PortcullisPosition2CenterShootLeft extends CommandGroup {
    
    public PortcullisPosition2CenterShootLeft() {
    	addSequential(new ManipulatorMoveMP(PresetPositions.FULLY_DEPLOYED));
        addSequential(new DriveTrainStraightMP(123, DriveTrain.MP_AUTON_MOAT_VELOCITY_INCHES_PER_SEC, true, true, 0));
    	addSequential(new ManipulatorMoveMP(PresetPositions.RETRACTED));
        addSequential(new Position2CenterShootLeft());
    }
}
