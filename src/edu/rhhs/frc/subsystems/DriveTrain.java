package edu.rhhs.frc.subsystems;


import com.kauailabs.navx.frc.AHRS;

import edu.rhhs.frc.OI;
import edu.rhhs.frc.commands.DriveWithJoystick;
import edu.rhhs.frc.utility.AudioPlayer;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SPI.Port;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveTrain extends MPSubsystem
{
	// private CANTalon rightFrontMotor;
	// private CANTalon leftFrontMotor;
	private CANTalon rightRearMotor;
	private CANTalon leftRearMotor;

	public static enum ControlMode { DRIVER, SOFTWARE_DRIVE, SOFTWARE_TURN };

	private RobotDrive m_drive;
	private Port m_imuSerialPort;
	private AHRS m_imu = null;
	private boolean m_imuFirstIteration;
	private double lastYawAngle;
	private long lastTime;
	private double output;

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
		//PID
		//P .018
		//I .00006
		//D .05
		
		getPIDController().setOutputRange(-1, 1);
		// leftFrontMotor = new CANTalon(1);
		// rightFrontMotor = new CANTalon(2);
		leftRearMotor = new CANTalon(2);
		rightRearMotor = new CANTalon(4);
		// m_drive = new RobotDrive(leftFrontMotor, leftRearMotor,
		// rightFrontMotor, rightRearMotor);
		m_drive = new RobotDrive(leftRearMotor, rightRearMotor);
		// m_drive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
		// m_drive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
		m_drive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
		m_drive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
		m_drive.setSafetyEnabled(false);
		// m_drive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
		// m_drive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);

		// You can add a second parameter to modify the
		// update rate (in hz) from. The minimum is 4.
		// The maximum (and the default) is 100 on a nav6, 60 on a navX MXP.
		// If you need to minimize CPU load, you can set it to a
		// lower value, as shown here, depending upon your needs.
		// The recommended maximum update rate is 50Hz

		// You can also use the IMUAdvanced class for advanced
		// features on a nav6 or a navX MXP.

		// You can also use the AHRS class for advanced features on
		// a navX MXP. This offers superior performance to the
		// IMU Advanced class, and also access to 9-axis headings
		// and magnetic disturbance detection. This class also offers
		// access to altitude/barometric pressure data from a
		// navX MXP Aero.
		m_imuSerialPort = Port.kMXP;

		byte updateRateHz = 50;
		m_imu = new AHRS(m_imuSerialPort, updateRateHz);
		m_imuFirstIteration = true;
		calibrateIMU();
		getPIDController().setAbsoluteTolerance(5);
	}

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		setDefaultCommand(new DriveWithJoystick());
	}

	/**
	 * Zeroes the IMU in the NavX.
	 */
	public void calibrateIMU() {
		// Set up the IMU
		if(!m_imuFirstIteration) return;
		for (int i = 0; i < 500; i++) {
			try {
				boolean isCalibrating = m_imu.isCalibrating();
				if (!isCalibrating) {
					Timer.delay(0.3);
					m_imu.zeroYaw();
					m_imuFirstIteration = false;
					break;
				}
				m_imu.wait(10);
			} catch (Exception e) {

			}
		}
	}

	public AHRS getIMU() {
		return m_imu;
	}

	public double getYawAngleDeg() {
		double yaw = m_imu.getYaw();
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
		m_imu.zeroYaw();
	}

	public void setDisplacementZero() {
		m_imu.resetDisplacement();
	}

	public void driveWithJoystick() {
		if (Math.abs(m_imu.getRoll()) > 8) {
			AudioPlayer.play();
		}

		if (m_drive != null && m_controlMode == ControlMode.DRIVER) {
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
			
			output = m_steerOutput;
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
		SmartDashboard.putNumber("YawRate", getYawRateDegPerSec());
		
		SmartDashboard.putNumber("PIDOutput", output);
		SmartDashboard.putBoolean("PIDOnTarget", getPIDController().onTarget());
		SmartDashboard.putNumber("PIDError", getPIDController().getError());
		SmartDashboard.putNumber("PID Target", getPIDController().getSetpoint());
		SmartDashboard.putBoolean("PID", getPIDController().isEnabled());

		SmartDashboard.putNumber("NavX X Accel", getIMU()
				.getWorldLinearAccelX());
		SmartDashboard.putNumber("NavX Y Accel", getIMU()
				.getWorldLinearAccelY());
		SmartDashboard
				.putNumber("NavX X Distance", getIMU().getDisplacementX());
		SmartDashboard
				.putNumber("NavX Y Distance", getIMU().getDisplacementY());
		SmartDashboard.putNumber("IMU Yaw (deg)", getYawAngleDeg());
		SmartDashboard.putNumber("IMU Pitch", m_imu.getRoll());
		SmartDashboard.putData(this);
	}

	@Override
	protected double returnPIDInput() {
		lastYawAngle = getYawAngleDeg();
		lastTime = System.nanoTime();
		if (m_controlMode == ControlMode.SOFTWARE_TURN)
			return getYawAngleDeg();
		if (m_controlMode == ControlMode.SOFTWARE_DRIVE)
			return 0; // TODO: ADD IMPLEMENTATION
		if (m_controlMode == ControlMode.DRIVER)
			return 0;
		return 0;
	}

	@Override
	protected void usePIDOutput(double output) {
		this.output = output;
		if (m_controlMode == ControlMode.SOFTWARE_TURN)
			m_drive.arcadeDrive(0, output);
		if (m_controlMode == ControlMode.DRIVER)
			this.getPIDController().disable();
	}

	public double getError() {
		return this.getPIDController().getError();
	}

	public void setControlMode(ControlMode mode) {
		this.m_controlMode = mode;
	}
}