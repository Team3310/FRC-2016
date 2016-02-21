package edu.rhhs.frc.subsystems;

import edu.rhhs.frc.OI;
import edu.rhhs.frc.RobotMain;
import edu.rhhs.frc.RobotMap;
import edu.rhhs.frc.commands.DriveWithJoystick;
import edu.rhhs.frc.subsystems.Intake.LiftState;
import edu.rhhs.frc.utility.CANTalonEncoder;
import edu.rhhs.frc.utility.MotionProfilePoint;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveTrain extends MPSubsystem
{
	public static enum ControlMode { DRIVER, SOFTWARE_DRIVE, SOFTWARE_TURN };
	public static enum SpeedShiftState { HI, LO };
	public static enum PTOShiftState { ENGAGED, DISENGAGED };
	
	public static final long LOOP_PERIOD_MS = 10;
	public static final double ENCODER_TICKS_TO_WORLD = 0; //MAJOR TODO
	
	private CANTalonEncoder leftDrive1;
	private CANTalon leftDrive2;
	private CANTalon leftDrive3;
	private CANTalonEncoder rightDrive1;
	private CANTalon rightDrive2;
	private CANTalon rightDrive3;
	
	private Solenoid speedShift;
	private DoubleSolenoid ptoShift;

	private RobotDrive m_drive;
	private double lastYawAngle;
	private long lastTime;
	private double totalError;

	protected double KpGyro = 0.0;
	protected double KiGyro = 0.0;
	protected double KdGyro = 0.0;
	
	// Controllers
	public static final int CONTROLLER_JOYSTICK_ARCADE = 0;
	public static final int CONTROLLER_JOYSTICK_TANK = 1;
	public static final int CONTROLLER_JOYSTICK_CHEESY = 2;
	public static final int CONTROLLER_XBOX_CHEESY = 3;
	public static final int CONTROLLER_XBOX_ARCADE_LEFT = 4;
	public static final int CONTROLLER_XBOX_ARCADE_RIGHT = 5;
	public static final int CONTROLLER_WHEEL = 6;

	public static final double STEER_NON_LINEARITY = 1.0;
	public static final double MOVE_NON_LINEARITY = 1.0;

	private int m_moveNonLinear = 0;
	private int m_steerNonLinear = 0;

	private boolean isAxisLocked = false;
	// private double m_moveScaleSlow = 0.35;
	// private double m_steerScaleSlow = 0.50;
	private double m_moveScale = 1.0;
	private double m_steerScale = 1.0;
	// private double m_moveScaleTurbo = 1.0;
	// private double m_steerScaleTurbo = 1.0;

	private double m_moveInput = 0.0;
	private double m_steerInput = 0.0;

	private double m_moveOutput = 0.0;
	private double m_steerOutput = 0.0;

	private double m_moveTrim = 0.0;
	private double m_steerTrim = 0.0;

	private ControlMode m_controlMode;

	// private int m_controllerMode;

	// PID/Software Area

	public DriveTrain() {
		super(LOOP_PERIOD_MS);

		//PID
		//P .018
		//I .00006
		//D .05

		//DriveTrain Motors
		leftDrive1 = new CANTalonEncoder(RobotMap.DRIVETRAIN_LEFT_MOTOR1, ENCODER_TICKS_TO_WORLD, false);
		leftDrive2 = new CANTalon(RobotMap.DRIVETRAIN_LEFT_MOTOR2);
		leftDrive3 = new CANTalon(RobotMap.DRIVETRAIN_LEFT_MOTOR3);
		addMotorController(leftDrive1);
		
		rightDrive1 = new CANTalonEncoder(RobotMap.DRIVETRAIN_RIGHT_MOTOR1, ENCODER_TICKS_TO_WORLD, true);
		rightDrive2 = new CANTalon(RobotMap.DRIVETRAIN_RIGHT_MOTOR2);
		rightDrive3 = new CANTalon(RobotMap.DRIVETRAIN_RIGHT_MOTOR3);
		addMotorController(rightDrive1);
		
		leftDrive2.changeControlMode(TalonControlMode.Follower);
		leftDrive2.set(leftDrive1.getDeviceID());
		leftDrive3.changeControlMode(TalonControlMode.Follower);
		leftDrive3.set(leftDrive1.getDeviceID());
		
		rightDrive2.changeControlMode(TalonControlMode.Follower);
		rightDrive2.set(rightDrive1.getDeviceID());
		rightDrive3.changeControlMode(TalonControlMode.Follower);
		rightDrive3.set(rightDrive1.getDeviceID());
		
		m_drive = new RobotDrive(leftDrive1, rightDrive1);
		// m_drive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
		// m_drive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
		m_drive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
		m_drive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
		m_drive.setSafetyEnabled(false);
		// m_drive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
		// m_drive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
		
		//DriveTrain Pneumatics
		speedShift = new Solenoid(RobotMap.DRIVETRAIN_SPEEDSHIFT_MODULE_ID);
		ptoShift = new DoubleSolenoid(RobotMap.DRIVETRAIN_WINCH_ENGAGE_MODULE_ID, RobotMap.DRIVETRAIN_WINCH_DISENGAGE_MODULE_ID);
		
//		RobotMain.gyro.calibrate();
	}

	@Override
	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		setDefaultCommand(new DriveWithJoystick());
	}
	
	public void straightMP(double distanceInches, boolean useGyroLock) {
		//Implementation
		this.setMPTarget(distanceInches, 0); //TODO: Velocity
	}
	
	public void turnMP(double angleDegrees) {
		//Implementation
		this.setMPTarget(angleDegrees, 0); //TODO: Velocity
	}
	
	public void controlLoopUpdate() {
		if (!isControlLoopEnabled()) return;
		MotionProfilePoint lastMPPoint = mpPoint;
		mpPoint = mp.getNextPoint(mpPoint);
		if (mpPoint == null) {
			disableControlLoop();
			for (CANTalonEncoder motorController : motorControllers) {
				motorController.changeControlMode(TalonControlMode.PercentVbus);
			}
			return;
		}
		
		currentPosition = mpPoint.position;
		currentVelocity = 1000 * (currentPosition - lastPosition) / (System.currentTimeMillis() - lastTime);
		double Kf = 0.0;
		if (Math.abs(mpPoint.position) > 0.001) {
			Kf = (Ka * mpPoint.acceleration + Kv * mpPoint.velocity) / mpPoint.position;
		}
		
		// Talon PID MODE for encoders
	
		for (CANTalonEncoder motorController : motorControllers) {
			motorController.setF(Kf);
			motorController.setWorld(mpPoint.position);
		}
		
		// Gyro software MODE
		// 0) set the control mode
		// 1) switch Talons to vbus (already done above)
		// 2) KpGyro, KdGyro, KiGyro, KfGyro, KaGyro, KvGyro
		// 3) PID Calculate
		double previousError = this.getMPTarget() - lastMPPoint.position;
		double error = this.getMPTarget() - getYawAngleDeg();
		
		double KfGyro = Ka * mpPoint.acceleration + Kv * mpPoint.velocity;
		
		double output = KpGyro*error + KiGyro*totalError + KdGyro*(error-previousError) + KfGyro;
		
		for (CANTalonEncoder motorController : motorControllers) {
			if (motorController.isRight()) {
				motorController.set(output);
			}
			else {
				motorController.set(-output);
			}
		}
			
		lastTime = System.currentTimeMillis();
		lastPosition = currentPosition;
	}

	public void driveWithJoystick() {
		if(m_controlMode != ControlMode.DRIVER || m_drive == null) return;
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
		if (isAxisLocked)
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

	public void setAxisLocked(boolean locked) {
		isAxisLocked = locked;
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

	public void updateStatus() {
		//TODO: Add wheel distances/positions
//		SmartDashboard.putNumber("YawRate", getYawRateDegPerSec());
		
//		SmartDashboard.putNumber("MP Target", this.getMPTarget());
//		SmartDashboard.putBoolean("Control Loop", this.isControlLoopEnabled());
//		SmartDashboard.putNumber("IMU Yaw (deg)", getYawAngleDeg());
//		SmartDashboard.putData(this);
		
	}

	//PIDInput
//	lastYawAngle = getYawAngleDeg();
//	lastTime = System.nanoTime();
//	if (m_controlMode == ControlMode.SOFTWARE_TURN)
//		return getYawAngleDeg();
//	if (m_controlMode == ControlMode.SOFTWARE_DRIVE)
//		return 0; // TODO: ADD IMPLEMENTATION
//	if (m_controlMode == ControlMode.DRIVER)
//		return 0;
//	return 0;
//
	//PIDOutput
//	this.output = output;
//	if (m_controlMode == ControlMode.SOFTWARE_TURN)
//		m_drive.arcadeDrive(0, output);
//	if (m_controlMode == ControlMode.DRIVER)
//		this.getPIDController().disable();

	public void setControlMode(ControlMode mode) {
		this.m_controlMode = mode;
	}

	//Gyro Implementation
	public double getYawAngleDeg() {
		double yaw = RobotMain.gyro.getAngle(); //TODO: Goes from 360 to 361
		if (Math.abs(yaw) > 5 && yaw < 0) {
			yaw += 360;
		}
		return yaw;
	}

	public double getYawRateDegPerSec() {
		double rate = (getYawAngleDeg() - lastYawAngle) * 1000000000.0 / (System.nanoTime() - lastTime);
		lastYawAngle = getYawAngleDeg();
		lastTime = System.nanoTime();
		return rate;
	}

	public void setYawAngleZero() {
		RobotMain.gyro.reset();
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

}