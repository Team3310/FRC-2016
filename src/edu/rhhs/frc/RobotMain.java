
package edu.rhhs.frc;

import java.util.Hashtable;

import edu.rhhs.frc.commands.auton.LowBarCrossAndReturn;
import edu.rhhs.frc.commands.auton.LowBarShootHigh;
import edu.rhhs.frc.commands.auton.MoatPosition2CenterShootLeft;
import edu.rhhs.frc.commands.auton.MoatPosition3CenterShootCenter;
import edu.rhhs.frc.subsystems.Camera;
import edu.rhhs.frc.subsystems.DriveTrain;
import edu.rhhs.frc.subsystems.Intake;
import edu.rhhs.frc.subsystems.Manipulator;
import edu.rhhs.frc.subsystems.Manipulator.PresetPositions;
import edu.rhhs.frc.subsystems.Shooter;
import edu.rhhs.frc.utility.ControlLooper;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotMain extends IterativeRobot
{
	public static final DriveTrain driveTrain = new DriveTrain();
	public static final Shooter shooter = new Shooter();
	public static final Intake intake = new Intake();
	public static final Manipulator manipulator = new Manipulator();	
	public static final Camera camera = new Camera();
	public static final ControlLooper controlLoop = new ControlLooper("Main control loop", 10);
	public static final PowerDistributionPanel pdp = new PowerDistributionPanel();

	public static OI oi;
	
	public static enum OperationMode { TEST, COMPETITION };
	private static OperationMode operationMode = OperationMode.TEST;

	public static enum AutonPosition { LOW_BAR, TWO, THREE, FOUR, FIVE };
	public static enum AutonDefense { LOW_BAR, PORTCULLIS, RAMPARTS, MOAT, ROCKWALL, ROUGH_TERRAIN };
	public static enum AutonTask { SHOOT_LEFT_HIGH, SHOOT_CENTER_HIGH, SHOOT_RIGHT_HIGH, CROSS_AND_RETURN };
	
	private Hashtable<String, Command> autonCommandTable = new Hashtable<String, Command>();

	private SendableChooser operationModeChooser;
	private SendableChooser manipulatorChooser;
	private SendableChooser autonPositionChooser;
	private SendableChooser autonDefenseChooser;
	private SendableChooser autonTaskChooser;

    private Command autonomousCommand;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	controlLoop.addLoopable(driveTrain);
    	controlLoop.addLoopable(manipulator);

	    operationModeChooser = new SendableChooser();
	    operationModeChooser.addDefault("Test", OperationMode.TEST);
	    operationModeChooser.addObject ("Competition", OperationMode.COMPETITION);
		SmartDashboard.putData("Operation Mode", operationModeChooser);
		
		manipulatorChooser = new SendableChooser();
		manipulatorChooser.addDefault("Cheval de Frise", Manipulator.Attachment.CHEVAL_DE_FRISE);
		manipulatorChooser.addObject ("Portcullis", Manipulator.Attachment.PORTCULLIS);
		SmartDashboard.putData("Manipulator", manipulatorChooser);
		
		autonPositionChooser = new SendableChooser();
		autonPositionChooser.addDefault("Low bar", AutonPosition.LOW_BAR);
		autonPositionChooser.addObject ("Position 2", AutonPosition.TWO);
		autonPositionChooser.addObject ("Position 3", AutonPosition.THREE);
		autonPositionChooser.addObject ("Position 4", AutonPosition.FOUR);
		autonPositionChooser.addObject ("Position 5", AutonPosition.FIVE);
		SmartDashboard.putData("AutonPosition", autonPositionChooser);
		
		autonDefenseChooser = new SendableChooser();
		autonDefenseChooser.addDefault("Low bar", AutonDefense.LOW_BAR);
		autonDefenseChooser.addObject ("Portcullis", AutonDefense.PORTCULLIS);
		autonDefenseChooser.addObject ("Ramparts", AutonDefense.RAMPARTS);
		autonDefenseChooser.addObject ("Moat", AutonDefense.MOAT);
		autonDefenseChooser.addObject ("Rockwall", AutonDefense.ROCKWALL);
		autonDefenseChooser.addObject ("Rough Terrain", AutonDefense.ROUGH_TERRAIN);
		SmartDashboard.putData("AutonDefense", autonDefenseChooser);
				
		autonTaskChooser = new SendableChooser();
		autonTaskChooser.addDefault("Shoot left high", AutonTask.SHOOT_LEFT_HIGH);
		autonTaskChooser.addDefault("Shoot center high", AutonTask.SHOOT_CENTER_HIGH);
		autonTaskChooser.addDefault("Shoot right high", AutonTask.SHOOT_RIGHT_HIGH);
		autonTaskChooser.addObject ("Cross and return", AutonTask.CROSS_AND_RETURN);
		SmartDashboard.putData("AutonTask", autonTaskChooser);

		setupAutonTable();
		
		updateStatus();
    }
	
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
		driveTrain.checkForGyroCalibration();
		updateStatus();
	}

    public void autonomousInit() {
    	updateChoosers();
    	
        // Schedule the autonomous command (example)
    	controlLoop.start();
    	manipulator.setPresetPosition(PresetPositions.RETRACTED);
        if (autonomousCommand != null) autonomousCommand.start();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
        updateStatus();
    }

    public void teleopInit() {
    	// This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to 
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (autonomousCommand != null) autonomousCommand.cancel();
    	updateChoosers();
        controlLoop.start();
    	manipulator.setPresetPosition(PresetPositions.RETRACTED);
        updateStatus();
    }

    /**
     * This function is called when the disabled button is hit.
     * You can use it to reset subsystems before shutting down.
     */
    public void disabledInit(){
    }

    /** This function is called periodically during operator control */
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
        updateStatus();
    }
    
    /** This function is called periodically during test mode */
    public void testPeriodic() {
        LiveWindow.run();
    }
    
    private void updateChoosers() {
		operationMode = (OperationMode)operationModeChooser.getSelected();
		manipulator.setAttachment((Manipulator.Attachment)manipulatorChooser.getSelected());
		AutonPosition autonPosition = (AutonPosition)autonPositionChooser.getSelected();
		AutonDefense autonDefense = (AutonDefense)autonDefenseChooser.getSelected();
		AutonTask autonTask = (AutonTask)autonTaskChooser.getSelected();
		autonomousCommand = autonCommandTable.get(buildAutonKey(autonPosition, autonDefense, autonTask));
    }
    
    private void setupAutonTable() {
    	autonCommandTable.put(buildAutonKey(AutonPosition.LOW_BAR, AutonDefense.LOW_BAR, AutonTask.SHOOT_CENTER_HIGH), 
    			new LowBarShootHigh());
    	autonCommandTable.put(buildAutonKey(AutonPosition.LOW_BAR, AutonDefense.LOW_BAR, AutonTask.CROSS_AND_RETURN), 
    			new LowBarCrossAndReturn());
    	autonCommandTable.put(buildAutonKey(AutonPosition.TWO, AutonDefense.MOAT, AutonTask.SHOOT_LEFT_HIGH), 
    			new MoatPosition2CenterShootLeft());
    	autonCommandTable.put(buildAutonKey(AutonPosition.THREE, AutonDefense.MOAT, AutonTask.SHOOT_CENTER_HIGH), 
    			new MoatPosition3CenterShootCenter());
   }
    
    private String buildAutonKey(AutonPosition position, AutonDefense defense, AutonTask task) {
    	return position.toString() + defense.toString() + task.toString();
    }
    
    public void updateStatus() {
    	driveTrain.updateStatus(operationMode);
    	intake.updateStatus(operationMode);
    	manipulator.updateStatus(operationMode);
    	shooter.updateStatus(operationMode);
    	camera.updateStatus(operationMode);
    }
}