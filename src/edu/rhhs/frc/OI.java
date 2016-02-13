package edu.rhhs.frc;

import edu.rhhs.frc.commands.DriveTrainTurn;
import edu.rhhs.frc.commands.ResetGyro;
import edu.rhhs.frc.commands.SetDriveAxisLock;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.InternalButton;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI 
{
	private static OI instance;

	//private XboxController m_drivetrainController;
	//private XboxController m_robotArmController;
	private Joystick m_joystick1;
	private Joystick m_joystick2;

	private OI() {
		m_joystick1 = new Joystick(0);
		m_joystick2 = new Joystick(1);
		//m_drivetrainController = new XboxController(0);
		
		//Joystick - Straight Move
//		JoystickButton shooterShoot = new JoystickButton(m_joystick1, 1);
//		shooterShoot.whenPressed(new SetShooterPosition(ModuleState.EXTENDED));
//		
//		JoystickButton shooterPullback = new JoystickButton(m_joystick1, 2);
//		shooterPullback.whenPressed(new SetShooterPosition(ModuleState.RETRACTED));
		
		JoystickButton axisLock = new JoystickButton(m_joystick2, 1);
		axisLock.whenPressed(new SetDriveAxisLock(true));
		axisLock.whenReleased(new SetDriveAxisLock(false));
		
		JoystickButton left180 = new JoystickButton(m_joystick2, 3);
		left180.whenPressed(new DriveTrainTurn(-180, 5));
		JoystickButton right180 = new JoystickButton(m_joystick2, 4);
		right180.whenPressed(new DriveTrainTurn(180, 5));
		JoystickButton left90 = new JoystickButton(m_joystick2, 5);
		left90.whenPressed(new DriveTrainTurn(-90, 3));
		JoystickButton right90 = new JoystickButton(m_joystick2, 6);
		right90.whenPressed(new DriveTrainTurn(90, 3));
		//		SmartDashboard.putData("Go 0", go0);
		//		Button off = new InternalButton();
		//		off.whenPressed(new ExecuteTimer(0.0));
		//		SmartDashboard.putData("Stop", off);

		//		Button reset = new InternalButton();
		//		reset.whenPressed(new ResetNavX());
		//		SmartDashboard.putData("Reset imu", reset);
		//		
		//		Button go90PID = new InternalButton();
		//		go90PID.whenPressed(new PIDTalonTest(90.0));
		//		SmartDashboard.putData("Go 90 PID", go90PID);
		//		
		//		Button go0PID = new InternalButton();
		//		go0PID.whenPressed(new PIDTalonTest(0.0));
		//		SmartDashboard.putData("Go 0 PID", go0PID);

//		Button mpTest = new InternalButton();
//		mpTest.whenPressed(new TalonMPSet(90.0, 90.0));
//		SmartDashboard.putData("Test Motion Profile", mpTest);
//
//		Button mpDrivetrainTest = new InternalButton();
//		mpDrivetrainTest.whenPressed(new DrivetrainMPSet(60.0, 40.0));
//		SmartDashboard.putData("Test Drivetrain Motion Profile", mpDrivetrainTest);
		Button resetGyro = new InternalButton();
		resetGyro.whenPressed(new ResetGyro());
		SmartDashboard.putData("Reset Gyro", resetGyro);
	}
	
//	public XboxController getDriveTrainController() {
//		return m_drivetrainController;
//	}
	
	public Joystick getJoystick1() {
		return m_joystick1;
	}
	
	public Joystick getJoystick2() {
		return m_joystick2;
	}

	public static OI getInstance() {
		if(instance == null) {
			instance = new OI();
		}
		return instance;
	}
}