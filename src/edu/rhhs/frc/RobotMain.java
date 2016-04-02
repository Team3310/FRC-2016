
package edu.rhhs.frc;

import java.util.Hashtable;

import edu.rhhs.frc.commands.auton.LowBarCrossAndReturn;
import edu.rhhs.frc.commands.auton.LowBarShootLeft;
import edu.rhhs.frc.commands.auton.MoatPosition2CenterShootLeft;
import edu.rhhs.frc.commands.auton.MoatPosition3CenterShootCenter;
import edu.rhhs.frc.commands.auton.MoatPosition4CenterShootCenter;
import edu.rhhs.frc.commands.auton.MoatPosition5CenterShootCenter;
import edu.rhhs.frc.commands.auton.PortcullisPosition2CenterShootLeft;
import edu.rhhs.frc.commands.auton.PortcullisPosition3CenterShootCenter;
import edu.rhhs.frc.commands.auton.PortcullisPosition4CenterShootCenter;
import edu.rhhs.frc.commands.auton.PortcullisPosition5CenterShootCenter;
import edu.rhhs.frc.commands.auton.RampartsPosition2LeftShootLeft;
import edu.rhhs.frc.commands.auton.RampartsPosition3LeftShootCenter;
import edu.rhhs.frc.commands.auton.RampartsPosition4LeftShootCenter;
import edu.rhhs.frc.commands.auton.RampartsPosition5LeftShootCenter;
import edu.rhhs.frc.commands.auton.RoughTerrainPosition2CenterShootLeft;
import edu.rhhs.frc.commands.auton.RoughTerrainPosition3CenterShootCenter;
import edu.rhhs.frc.commands.auton.RoughTerrainPosition4CenterShootCenter;
import edu.rhhs.frc.commands.auton.RoughTerrainPosition5CenterShootCenter;
import edu.rhhs.frc.commands.auton.SpyStraightShootLeft;
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
	public static OperationMode operationMode = OperationMode.COMPETITION;

	public static enum AutonPosition { SPY, LOW_BAR, TWO, THREE, FOUR, FIVE };
	public static enum AutonDefense { SPY, LOW_BAR, PORTCULLIS, RAMPARTS, MOAT, ROCKWALL, ROUGH_TERRAIN, CDF };
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
	    operationModeChooser.addObject("Competition", OperationMode.COMPETITION);
		SmartDashboard.putData("Operation Mode", operationModeChooser);
		
		manipulatorChooser = new SendableChooser();
		manipulatorChooser.addDefault("Portcullis", Manipulator.Attachment.PORTCULLIS);
		manipulatorChooser.addObject("Cheval de Frise", Manipulator.Attachment.CHEVAL_DE_FRISE);
		SmartDashboard.putData("Manipulator", manipulatorChooser);
		
		autonPositionChooser = new SendableChooser();
		autonPositionChooser.addObject("Spy", AutonPosition.SPY);
		autonPositionChooser.addDefault("Low bar", AutonPosition.LOW_BAR);
		autonPositionChooser.addObject("Position 2", AutonPosition.TWO);
		autonPositionChooser.addObject("Position 3", AutonPosition.THREE);
		autonPositionChooser.addObject("Position 4", AutonPosition.FOUR);
		autonPositionChooser.addObject("Position 5", AutonPosition.FIVE);
		SmartDashboard.putData("AutonPosition", autonPositionChooser);
		
		autonDefenseChooser = new SendableChooser();
		autonDefenseChooser.addObject("Spy", AutonDefense.SPY);
		autonDefenseChooser.addDefault("Low bar", AutonDefense.LOW_BAR);
		autonDefenseChooser.addObject("Portcullis", AutonDefense.PORTCULLIS);
		autonDefenseChooser.addObject("Cheval de Frise", AutonDefense.CDF);
		autonDefenseChooser.addObject("Ramparts", AutonDefense.RAMPARTS);
		autonDefenseChooser.addObject("Moat", AutonDefense.MOAT);
		autonDefenseChooser.addObject("Rockwall", AutonDefense.ROCKWALL);
		autonDefenseChooser.addObject("Rough Terrain", AutonDefense.ROUGH_TERRAIN);
		SmartDashboard.putData("AutonDefense", autonDefenseChooser);
				
		autonTaskChooser = new SendableChooser();
		autonTaskChooser.addDefault("Shoot left high", AutonTask.SHOOT_LEFT_HIGH);
		autonTaskChooser.addObject("Shoot center high", AutonTask.SHOOT_CENTER_HIGH);
		autonTaskChooser.addObject("Shoot right high", AutonTask.SHOOT_RIGHT_HIGH);
		autonTaskChooser.addObject("Cross and return", AutonTask.CROSS_AND_RETURN);
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
    	manipulator.setZeroPosition();
    	manipulator.setPresetPosition(PresetPositions.RETRACTED);
    	driveTrain.endGyroCalibration();
    	driveTrain.resetGyro();
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
    	driveTrain.endGyroCalibration();
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
    	autonCommandTable.put(buildAutonKey(AutonPosition.SPY, AutonDefense.SPY, AutonTask.SHOOT_CENTER_HIGH), 
    			new SpyStraightShootLeft());

    	autonCommandTable.put(buildAutonKey(AutonPosition.LOW_BAR, AutonDefense.LOW_BAR, AutonTask.SHOOT_LEFT_HIGH), 
    			new LowBarShootLeft());
    	autonCommandTable.put(buildAutonKey(AutonPosition.LOW_BAR, AutonDefense.LOW_BAR, AutonTask.CROSS_AND_RETURN), 
    			new LowBarCrossAndReturn());

    	autonCommandTable.put(buildAutonKey(AutonPosition.TWO, AutonDefense.MOAT, AutonTask.SHOOT_LEFT_HIGH), 
    			new MoatPosition2CenterShootLeft());
    	autonCommandTable.put(buildAutonKey(AutonPosition.THREE, AutonDefense.MOAT, AutonTask.SHOOT_CENTER_HIGH), 
    			new MoatPosition3CenterShootCenter());
    	autonCommandTable.put(buildAutonKey(AutonPosition.FOUR, AutonDefense.MOAT, AutonTask.SHOOT_CENTER_HIGH), 
    			new MoatPosition4CenterShootCenter());
    	autonCommandTable.put(buildAutonKey(AutonPosition.FIVE, AutonDefense.MOAT, AutonTask.SHOOT_CENTER_HIGH), 
    			new MoatPosition5CenterShootCenter());

    	autonCommandTable.put(buildAutonKey(AutonPosition.TWO, AutonDefense.ROCKWALL, AutonTask.SHOOT_LEFT_HIGH), 
    			new MoatPosition2CenterShootLeft());
    	autonCommandTable.put(buildAutonKey(AutonPosition.THREE, AutonDefense.ROCKWALL, AutonTask.SHOOT_CENTER_HIGH), 
    			new MoatPosition3CenterShootCenter());
    	autonCommandTable.put(buildAutonKey(AutonPosition.FOUR, AutonDefense.ROCKWALL, AutonTask.SHOOT_CENTER_HIGH), 
    			new MoatPosition4CenterShootCenter());
    	autonCommandTable.put(buildAutonKey(AutonPosition.FIVE, AutonDefense.ROCKWALL, AutonTask.SHOOT_CENTER_HIGH), 
    			new MoatPosition5CenterShootCenter());

    	autonCommandTable.put(buildAutonKey(AutonPosition.TWO, AutonDefense.ROUGH_TERRAIN, AutonTask.SHOOT_LEFT_HIGH), 
    			new RoughTerrainPosition2CenterShootLeft());
    	autonCommandTable.put(buildAutonKey(AutonPosition.THREE, AutonDefense.ROUGH_TERRAIN, AutonTask.SHOOT_CENTER_HIGH), 
    			new RoughTerrainPosition3CenterShootCenter());
    	autonCommandTable.put(buildAutonKey(AutonPosition.FOUR, AutonDefense.ROUGH_TERRAIN, AutonTask.SHOOT_CENTER_HIGH), 
    			new RoughTerrainPosition4CenterShootCenter());
    	autonCommandTable.put(buildAutonKey(AutonPosition.FIVE, AutonDefense.ROUGH_TERRAIN, AutonTask.SHOOT_CENTER_HIGH), 
    			new RoughTerrainPosition5CenterShootCenter());

    	autonCommandTable.put(buildAutonKey(AutonPosition.TWO, AutonDefense.RAMPARTS, AutonTask.SHOOT_LEFT_HIGH), 
    			new RampartsPosition2LeftShootLeft());
    	autonCommandTable.put(buildAutonKey(AutonPosition.THREE, AutonDefense.RAMPARTS, AutonTask.SHOOT_CENTER_HIGH), 
    			new RampartsPosition3LeftShootCenter());
    	autonCommandTable.put(buildAutonKey(AutonPosition.FOUR, AutonDefense.RAMPARTS, AutonTask.SHOOT_CENTER_HIGH), 
    			new RampartsPosition4LeftShootCenter());
    	autonCommandTable.put(buildAutonKey(AutonPosition.FIVE, AutonDefense.RAMPARTS, AutonTask.SHOOT_CENTER_HIGH), 
    			new RampartsPosition5LeftShootCenter());
    	
    	autonCommandTable.put(buildAutonKey(AutonPosition.TWO, AutonDefense.PORTCULLIS, AutonTask.SHOOT_LEFT_HIGH), 
    			new PortcullisPosition2CenterShootLeft());
    	autonCommandTable.put(buildAutonKey(AutonPosition.THREE, AutonDefense.PORTCULLIS, AutonTask.SHOOT_CENTER_HIGH), 
    			new PortcullisPosition3CenterShootCenter());
    	autonCommandTable.put(buildAutonKey(AutonPosition.FOUR, AutonDefense.PORTCULLIS, AutonTask.SHOOT_CENTER_HIGH), 
    			new PortcullisPosition4CenterShootCenter());
    	autonCommandTable.put(buildAutonKey(AutonPosition.FIVE, AutonDefense.PORTCULLIS, AutonTask.SHOOT_CENTER_HIGH), 
    			new PortcullisPosition5CenterShootCenter());
    	
    	autonCommandTable.put(buildAutonKey(AutonPosition.TWO, AutonDefense.CDF, AutonTask.SHOOT_LEFT_HIGH), 
    			new PortcullisPosition2CenterShootLeft());
    	autonCommandTable.put(buildAutonKey(AutonPosition.THREE, AutonDefense.CDF, AutonTask.SHOOT_CENTER_HIGH), 
    			new PortcullisPosition3CenterShootCenter());
    	autonCommandTable.put(buildAutonKey(AutonPosition.FOUR, AutonDefense.CDF, AutonTask.SHOOT_CENTER_HIGH), 
    			new PortcullisPosition4CenterShootCenter());
    	autonCommandTable.put(buildAutonKey(AutonPosition.FIVE, AutonDefense.CDF, AutonTask.SHOOT_CENTER_HIGH), 
    			new PortcullisPosition5CenterShootCenter());
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