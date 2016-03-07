package edu.rhhs.frc.commands.auton;

import edu.rhhs.frc.commands.DriveTrainStraightMP;
import edu.rhhs.frc.subsystems.DriveTrain;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class MoatPosition2CenterShootLeft extends CommandGroup {
    
    public MoatPosition2CenterShootLeft() {
        addSequential(new DriveTrainStraightMP(132, DriveTrain.MP_AUTON_MOAT_VELOCITY_INCHES_PER_SEC, true, 0));
        addSequential(new Position2CenterShootLeft());
    }
}
