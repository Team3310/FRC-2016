package edu.rhhs.frc.subsystems;

import java.util.ArrayList;

import edu.rhhs.frc.OI;
import edu.rhhs.frc.RobotMain;
import edu.rhhs.frc.RobotMap;
import edu.rhhs.frc.commands.DriveWithJoystick;
import edu.rhhs.frc.utility.CANTalonEncoder;
import edu.rhhs.frc.utility.ControlLoopable;
import edu.rhhs.frc.utility.MotionProfileController;
import edu.rhhs.frc.utility.PIDParams;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

public class DriveTrain extends Subsystem implements ControlLoopable
{
	public static enum DriveTrainControlMode { JOYSTICK, MP_STRAIGHT };
	public static enum SpeedShiftState { HI, LO };
	public static enum PTOShiftState { ENGAGED, DISENGAGED };
	
	public static final double ENCODER_TICKS_TO_WORLD = 4096; 
	
	// Motor controllers
	private ArrayList<CANTalonEncoder> motorControllers;	

	private CANTalonEncoder leftDrive1;
	private CANTalon leftDrive2;
	private CANTalon leftDrive3;
	
	private CANTalonEncoder rightDrive1;
	private CANTalon rightDrive2;
	private CANTalon rightDrive3;

	private RobotDrive m_drive;

	// Pneumatics
	private Solenoid speedShift;
	private DoubleSolenoid ptoShift;

	// Input devices
	public static final int DRIVER_INPUT_JOYSTICK_ARCADE = 0;
	public static final int DRIVER_INPUT_JOYSTICK_TANK = 1;
	public static final int DRIVER_INPUT_JOYSTICK_CHEESY = 2;
	public static final int DRIVER_INPUT_XBOX_CHEESY = 3;
	public static final int DRIVER_INPUT_XBOX_ARCADE_LEFT = 4;
	public static final int DRIVER_INPUT_XBOX_ARCADE_RIGHT = 5;
	public static final int DRIVER_INPUT_WHEEL = 6;

	public static final double STEER_NON_LINEARITY = 1.0;
	public static final double MOVE_NON_LINEARITY = 1.0;

	private int m_moveNonLinear = 0;
	private int m_steerNonLinear = 0;

	private double m_moveScale = 1.0;
	private double m_steerScale = 1.0;

	private double m_moveInput = 0.0;
	private double m_steerInput = 0.0;

	private double m_moveOutput = 0.0;
	private double m_steerOutput = 0.0;

	private double m_moveTrim = 0.0;
	private double m_steerTrim = 0.0;

	private long periodMs;
	private boolean controlLoopEnabled;
	private DriveTrainControlMode controlMode;
	
	private MotionProfileController mpStraightController;
	private PIDParams mpStraightPIDParams = new PIDParams(0, 0, 0, 0, 0);

	private ADXRS450_Gyro gyro = new ADXRS450_Gyro();

	public DriveTrain() {
		leftDrive1 = new CANTalonEncoder(RobotMap.DRIVETRAIN_LEFT_MOTOR1, ENCODER_TICKS_TO_WORLD, false);
		leftDrive2 = new CANTalon(RobotMap.DRIVETRAIN_LEFT_MOTOR2);
		leftDrive3 = new CANTalon(RobotMap.DRIVETRAIN_LEFT_MOTOR3);
		
		rightDrive1 = new CANTalonEncoder(RobotMap.DRIVETRAIN_RIGHT_MOTOR1, ENCODER_TICKS_TO_WORLD, true);
		rightDrive2 = new CANTalon(RobotMap.DRIVETRAIN_RIGHT_MOTOR2);
		rightDrive3 = new CANTalon(RobotMap.DRIVETRAIN_RIGHT_MOTOR3);
		
		leftDrive2.changeControlMode(TalonControlMode.Follower);
		leftDrive2.set(leftDrive1.getDeviceID());
		leftDrive3.changeControlMode(TalonControlMode.Follower);
		leftDrive3.set(leftDrive1.getDeviceID());
		motorControllers.add(leftDrive1);
		
		rightDrive1.setRight(true);
		rightDrive2.changeControlMode(TalonControlMode.Follower);
		rightDrive2.set(rightDrive1.getDeviceID());
		rightDrive3.changeControlMode(TalonControlMode.Follower);
		rightDrive3.set(rightDrive1.getDeviceID());
		motorControllers.add(rightDrive1);
		
		m_drive = new RobotDrive(leftDrive1, rightDrive1);
		m_drive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
		m_drive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
		m_drive.setSafetyEnabled(false);
		
		speedShift = new Solenoid(RobotMap.DRIVETRAIN_SPEEDSHIFT_MODULE_ID);
		ptoShift = new DoubleSolenoid(RobotMap.DRIVETRAIN_WINCH_ENGAGE_MODULE_ID, RobotMap.DRIVETRAIN_WINCH_DISENGAGE_MODULE_ID);
		
		mpStraightController = new MotionProfileController(periodMs, mpStraightPIDParams, motorControllers);
	}

	@Override
	public void initDefaultCommand() {
		setDefaultCommand(new DriveWithJoystick());
	}
	
	public ADXRS450_Gyro getGyro() {
		return gyro;
	}
	
	public void setStraightMP(double distanceInches, double maxVelocity, boolean useGyroLock, double desiredAbsoluteAngle) {
		if (isControlLoopEnabled()) {
			disableControlLoop();
		}	
		mpStraightController.setMPTarget(distanceInches, maxVelocity, useGyroLock, desiredAbsoluteAngle); 
		setControlMode(DriveTrainControlMode.MP_STRAIGHT);
		enableControlLoop();
	}
	
	public void setControlMode(DriveTrainControlMode controlMode) {
		this.controlMode = controlMode;
		if (controlMode == DriveTrainControlMode.JOYSTICK) {
			leftDrive1.changeControlMode(TalonControlMode.PercentVbus);
			rightDrive1.changeControlMode(TalonControlMode.PercentVbus);
		}
	}
	
	public void controlLoopUpdate() {
		if (!isControlLoopEnabled()) {
			return;
		}
		
		boolean isFinished = false;
		if (controlMode == DriveTrainControlMode.MP_STRAIGHT) {
			isFinished = mpStraightController.controlLoopUpdate(getGyro().getAngle());
		}

		if (isFinished) {
			disableControlLoop();
			setControlMode(DriveTrainControlMode.JOYSTICK);
			return;
		}				
	}

	public void driveWithJoystick() {
		if(controlMode != DriveTrainControlMode.JOYSTICK || m_drive == null) return;
		// switch(m_controllerMode) {
		// case CONTROLLER_JOYSTICK_ARCADE:
		// m_moveInput = OI.getInstance().getJoystick1().getY();
		// m_steerInput = OI.getInstance().getJoystick1().getX();
		// m_moveOutput = adjustForSensitivity(m_moveScale, m_moveTrim,
		// m_moveInput, m_moveNonLinear, MOVE_NON_LINEARITY);
		// m_steerOutput = adjustForSensitivity(m_steerScale, m_steerTrim,
		// m_steerInput, m_steerNonLinear, STEER_NON_LINEARITY);
		// m_drive.arcadeDrive(m_moveOutput, m_steerOutput);
		// break;
		// case CONTROLLER_JOYSTICK_TANK:
		// m_moveInput = OI.getInstance().getJoystick1().getY();
		// m_steerInput = OI.getInstance().getJoystick2().getY();
		// m_moveOutput = adjustForSensitivity(m_moveScale, m_moveTrim,
		// m_moveInput, m_moveNonLinear, MOVE_NON_LINEARITY);
		// m_steerOutput = adjustForSensitivity(m_moveScale, m_moveTrim,
		// m_steerInput, m_moveNonLinear, MOVE_NON_LINEARITY);
		// m_drive.tankDrive(m_moveOutput, m_steerOutput);
		// break;
		// case CONTROLLER_JOYSTICK_CHEESY:
		// m_moveInput = OI.getInstance().getJoystick1().getY();
		// m_steerInput = OI.getInstance().getJoystick2().getX();
		// m_moveOutput = adjustForSensitivity(m_moveScale, m_moveTrim,
		// m_moveInput, m_moveNonLinear, MOVE_NON_LINEARITY);
		// m_steerOutput = adjustForSensitivity(m_steerScale, m_steerTrim,
		// m_steerInput, m_steerNonLinear, STEER_NON_LINEARITY);
		// m_drive.arcadeDrive(m_moveOutput, m_steerOutput);
		// break;
		// case CONTROLLER_XBOX_CHEESY:
		// boolean turbo = OI.getInstance().getDriveTrainController()
		// .getLeftJoystickButton();
		// boolean slow = OI.getInstance().getDriveTrainController()
		// .getRightJoystickButton();
		// double speedToUseMove, speedToUseSteer;
		// if (turbo && !slow) {
		// speedToUseMove = m_moveScaleTurbo;
		// speedToUseSteer = m_steerScaleTurbo;
		// } else if (!turbo && slow) {
		// speedToUseMove = m_moveScaleSlow;
		// speedToUseSteer = m_steerScaleSlow;
		// } else {
		// speedToUseMove = m_moveScale;
		// speedToUseSteer = m_steerScale;
		// }

		// m_moveInput =
		// OI.getInstance().getDriveTrainController().getLeftYAxis();
		// m_steerInput =
		// OI.getInstance().getDriveTrainController().getRightXAxis();
		m_moveInput = OI.getInstance().getJoystick1().getY();
		m_steerInput = OI.getInstance().getJoystick2().getX();

		m_moveOutput = adjustForSensitivity(m_moveScale, m_moveTrim,
				m_moveInput, m_moveNonLinear, MOVE_NON_LINEARITY);
		m_steerOutput = adjustForSensitivity(m_steerScale, m_steerTrim,
				m_steerInput, m_steerNonLinear, STEER_NON_LINEARITY);
		m_steerOutput = 0;
		
		m_drive.arcadeDrive(m_moveOutput, m_steerOutput);
		// break;
		// case CONTROLLER_XBOX_ARCADE_RIGHT:
		// m_moveInput =
		// OI.getInstance().getDrivetrainController().getRightYAxis();
		// m_steerInput =
		// OI.getInstance().getDrivetrainController().getRightXAxis();
		// m_moveOutput = adjustForSensitivity(m_moveScale, m_moveTrim,
		// m_moveInput, m_moveNonLinear, MOVE_NON_LINEARITY);
		// m_steerOutput = adjustForSensitivity(m_steerScale, m_steerTrim,
		// m_steerInput, m_steerNonLinear, STEER_NON_LINEARITY);
		// m_drive.arcadeDrive(m_moveOutput, m_steerOutput);
		// break;
		// case CONTROLLER_XBOX_ARCADE_LEFT:
		// m_moveInput =
		// OI.getInstance().getDrivetrainController().getLeftYAxis();
		// m_steerInput =
		// OI.getInstance().getDrivetrainController().getLeftXAxis();
		// m_moveOutput = adjustForSensitivity(m_moveScale, m_moveTrim,
		// m_moveInput, m_moveNonLinear, MOVE_NON_LINEARITY);
		// m_steerOutput = adjustForSensitivity(m_steerScale, m_steerTrim,
		// m_steerInput, m_steerNonLinear, STEER_NON_LINEARITY);
		// m_drive.arcadeDrive(m_moveOutput, m_steerOutput);
		// break;
		// }
	}

	private boolean inDeadZone(double input) {
		boolean inDeadZone;
		if (Math.abs(input) < .3) {
			inDeadZone = true;
		} else {
			inDeadZone = false;
		}
		return inDeadZone;
	}

	private double adjustForSensitivity(double scale, double trim,
			double steer, int nonLinearFactor, double wheelNonLinearity) {
		if (inDeadZone(steer))
			return 0;

		steer += trim;
		steer *= scale;
		steer = limitValue(steer);

		int iterations = Math.abs(nonLinearFactor);
		for (int i = 0; i < iterations; i++) {
			if (nonLinearFactor > 0) {
				steer = nonlinearStickCalcPositive(steer, wheelNonLinearity);
			} else {
				steer = nonlinearStickCalcNegative(steer, wheelNonLinearity);
			}
		}
		return steer;
	}

	private double limitValue(double value) {
		if (value > 1.0) {
			value = 1.0;
		} else if (value < -1.0) {
			value = -1.0;
		}
		return value;
	}

	private double nonlinearStickCalcPositive(double steer,
			double steerNonLinearity) {
		return Math.sin(Math.PI / 2.0 * steerNonLinearity * steer)
				/ Math.sin(Math.PI / 2.0 * steerNonLinearity);
	}

	private double nonlinearStickCalcNegative(double steer,
			double steerNonLinearity) {
		return Math.asin(steerNonLinearity * steer)
				/ Math.asin(steerNonLinearity);
	}

	public void setShiftState(SpeedShiftState state) {
		if(state == SpeedShiftState.HI) {
			speedShift.set(true);
		}
		else if(state == SpeedShiftState.LO) {
			speedShift.set(false);
		}
	}

	public void setPTOState(PTOShiftState state) {
		if(state == PTOShiftState.ENGAGED) {
			ptoShift.set(Value.kForward);
		}
		else if(state == PTOShiftState.DISENGAGED) {
			ptoShift.set(Value.kReverse);
		}
	}

	public boolean isControlLoopEnabled() {
		return controlLoopEnabled;
	}
	
	protected void disableControlLoop() {
		controlLoopEnabled = false;
	}

	protected void enableControlLoop() {
		controlLoopEnabled = true;
	}

	@Override
	public void setPeriodMs(long periodMs) {
		this.periodMs = periodMs;
	}
	
	public void updateStatus() {
	}	
}