package edu.rhhs.frc.commands.auton;

import edu.rhhs.frc.commands.DriveTrainStraightMP;
import edu.rhhs.frc.commands.IntakeInnerPosition;
import edu.rhhs.frc.commands.IntakeOuterPosition;
import edu.rhhs.frc.commands.ShooterWinchRetract;
import edu.rhhs.frc.commands.ShooterWinchSpoolOut;
import edu.rhhs.frc.subsystems.DriveTrain;
import edu.rhhs.frc.subsystems.Intake.LiftState;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class RampartsPosition4LeftShootCenter extends CommandGroup {
    
    public RampartsPosition4LeftShootCenter() {
        addSequential(new ShooterWinchRetract());
        addParallel(new ShooterWinchSpoolOut());
        addSequential(new IntakeOuterPosition(LiftState.DOWN));
        addSequential(new IntakeInnerPosition(LiftState.DOWN));
    	//addSequential(new DriveTrainGyroOffset(-6.0));
        addSequential(new DriveTrainStraightMP(140, DriveTrain.MP_AUTON_MOAT_VELOCITY_INCHES_PER_SEC, true, false, 0));
        addSequential(new Position4CenterShootCenter());
    }
}
