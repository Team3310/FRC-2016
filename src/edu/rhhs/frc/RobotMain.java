
package edu.rhhs.frc;

import edu.rhhs.frc.subsystems.DriveTrain;
import edu.rhhs.frc.subsystems.Intake;
import edu.rhhs.frc.subsystems.Manipulator;
import edu.rhhs.frc.subsystems.Shooter;
import edu.rhhs.frc.utility.ControlLooper;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.ControllerPower;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

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
	public static OI oi;

    private Command autonomousCommand;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	controlLoop.addLoopable(driveTrain);
    	controlLoop.addLoopable(manipulator);
    	controlLoop.addLoopable(shooter);
	    driveTrain.getGyro().calibrate();
        updateStatus();
    }
	
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
		updateStatus();
	}

    public void autonomousInit() {
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
    
    public void updateStatus() {
    	driveTrain.updateStatus();
    	intake.updateStatus();
    	manipulator.updateStatus();
    	shooter.updateStatus();
    }
}