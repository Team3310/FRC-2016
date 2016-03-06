
package edu.rhhs.frc;

import edu.rhhs.frc.commands.auton.LowBarCrossAndReturn;
import edu.rhhs.frc.commands.auton.LowBarShootOneHigh;
import edu.rhhs.frc.commands.auton.MoatCrossAndReturn;
import edu.rhhs.frc.subsystems.Camera;
import edu.rhhs.frc.subsystems.DriveTrain;
import edu.rhhs.frc.subsystems.Intake;
import edu.rhhs.frc.subsystems.Manipulator;
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
	public static final ControlLooper controlLoop = new ControlLooper("Main control loop", 10);
	public static final PowerDistributionPanel pdp = new PowerDistributionPanel();
	public static final Camera camera = new Camera();

	public static OI oi;
	
	public static enum OperationMode { TEST, COMPETITION };
	private SendableChooser operationModeChooser;
	private static OperationMode operationMode = OperationMode.TEST;
	private SendableChooser manipulatorChooser;
	private SendableChooser autonoumousChooser;

    private Command autonomousCommand;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	controlLoop.addLoopable(driveTrain);
    	controlLoop.addLoopable(manipulator);
    	controlLoop.addLoopable(shooter);

	    operationModeChooser = new SendableChooser();
	    operationModeChooser.addDefault("Test", OperationMode.TEST);
	    operationModeChooser.addObject ("Competition", OperationMode.COMPETITION);
		SmartDashboard.putData("Operation Mode", operationModeChooser);
		
		manipulatorChooser = new SendableChooser();
		manipulatorChooser.addDefault("Cheval de Frise", Manipulator.Attachment.CHEVAL_DE_FRISE);
		manipulatorChooser.addObject ("Portcullis", Manipulator.Attachment.PORTCULLIS);
		SmartDashboard.putData("Manipulator", manipulatorChooser);
		
		autonoumousChooser = new SendableChooser();
		autonoumousChooser.addDefault("Low bar shoot 1 high", new LowBarShootOneHigh());
		autonoumousChooser.addObject ("Low bar cross and return", new LowBarCrossAndReturn());
		autonoumousChooser.addObject ("Moat cross and return", new MoatCrossAndReturn());
		SmartDashboard.putData("Autonomous", autonoumousChooser);
		
       updateStatus();
    }
	
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
		updateStatus();
	}

    public void autonomousInit() {
    	updateChoosers();
    	
        // Schedule the autonomous command (example)
    	controlLoop.start();
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
        updateStatus();
    }

    /**
     * This function is called when the disabled button is hit.
     * You can use it to reset subsystems before shutting down.
     */
    public void disabledInit(){
    	System.out.println("DisabledInit");
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
		autonomousCommand = (Command)autonoumousChooser.getSelected();
    }
    
    public void updateStatus() {
    	driveTrain.updateStatus(operationMode);
    	intake.updateStatus(operationMode);
    	manipulator.updateStatus(operationMode);
    	shooter.updateStatus(operationMode);
    	camera.updateStatus(operationMode);
    }
}