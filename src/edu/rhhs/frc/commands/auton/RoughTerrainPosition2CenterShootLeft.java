package edu.rhhs.frc.commands.auton;

import edu.rhhs.frc.commands.DriveTrainStraightMP;
import edu.rhhs.frc.commands.IntakeInnerPosition;
import edu.rhhs.frc.commands.IntakeLowBarPosition;
import edu.rhhs.frc.commands.IntakeOuterPosition;
import edu.rhhs.frc.commands.ShooterWinchRetract;
import edu.rhhs.frc.commands.ShooterWinchSpoolOut;
import edu.rhhs.frc.subsystems.DriveTrain;
import edu.rhhs.frc.subsystems.Intake.LiftState;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class RoughTerrainPosition2CenterShootLeft extends CommandGroup {
    
    public RoughTerrainPosition2CenterShootLeft() {
        addSequential(new ShooterWinchRetract());
        addParallel(new ShooterWinchSpoolOut());
        addParallel(new IntakeOuterPosition(LiftState.DOWN));
        addParallel(new IntakeInnerPosition(LiftState.DOWN));
        addSequential(new DriveTrainStraightMP(150, DriveTrain.MP_AUTON_MOAT_VELOCITY_INCHES_PER_SEC, true, true, 0));
        addSequential(new Position2CenterShootLeft());
    }
}
