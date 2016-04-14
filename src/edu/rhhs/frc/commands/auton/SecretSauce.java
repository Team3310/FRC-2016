package edu.rhhs.frc.commands.auton;

import edu.rhhs.frc.commands.DriveTrainStraightMP;
import edu.rhhs.frc.commands.IntakeFullyDeploy;
import edu.rhhs.frc.commands.IntakeOuterSpeed;
import edu.wpi.first.wpilibj.command.CommandGroup;
/**
 *
 */
public class SecretSauce extends CommandGroup {
    
    public SecretSauce() {
        addParallel(new IntakeOuterSpeed(1.0));
        addSequential(new DriveTrainStraightMP(20, 200, true, false, 0));
        addSequential(new IntakeFullyDeploy());
        addSequential(new DriveTrainStraightMP(-40, 75, true, false, 0));
    }
}
