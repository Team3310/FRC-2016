package edu.rhhs.frc.subsystems;

import java.util.ArrayList;

import edu.rhhs.frc.OI;
import edu.rhhs.frc.RobotMain;
import edu.rhhs.frc.RobotMap;
import edu.rhhs.frc.utility.BHRMathUtils;
import edu.rhhs.frc.utility.CANTalonEncoder;
import edu.rhhs.frc.utility.ControlLoopable;
import edu.rhhs.frc.utility.MPSoftwarePIDController;
import edu.rhhs.frc.utility.MPSoftwarePIDController.MPTurnType;
import edu.rhhs.frc.utility.MPTalonPIDController;
import edu.rhhs.frc.utility.MotionProfilePoint;
import edu.rhhs.frc.utility.PIDParams;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveTrain extends Subsystem implements ControlLoopable
{
	public static enum DriveTrainControlMode { JOYSTICK, MP_STRAIGHT, MP_TURN, TEST };
	public static enum SpeedShiftState { HI, LO };
	public static enum PTOShiftState { ENGAGED, DISENGAGED };

	public static final double TRACK_WIDTH_INCHES = 20;
	public static final double ENCODER_TICKS_TO_INCHES = 4096 / (4.0 * Math.PI); 

	// Motion profile max velocities and accel times
	public static final double MP_AUTON_MAX_STRAIGHT_VELOCITY_INCHES_PER_SEC = 60;
	public static final double MP_AUTON_MAX_TURN_RATE_DEG_PER_SEC = 180;
	
	public static final double MP_STRAIGHT_T1 = 600;
	public static final double MP_STRAIGHT_T2 = 300;
	public static final double MP_TURN_T1 = 400;
	public static final double MP_TURN_T2 = 200;
	
	// Motor controllers
	private ArrayList<CANTalonEncoder> motorControllers = new ArrayList<CANTalonEncoder>();	

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

	private boolean isFinished;
	private DriveTrainControlMode controlMode = DriveTrainControlMode.JOYSTICK;
	
	private MPTalonPIDController mpStraightController;
	private PIDParams mpStraightPIDParams = new PIDParams(0.1, 0, 0, 0.005, 0.03, 0.09);

	private MPSoftwarePIDController mpTurnController;
	private PIDParams mpTurnPIDParams = new PIDParams(0.025, 0, 0, 0.0004, 0.0035);

	private ADXRS450_Gyro gyro = new ADXRS450_Gyro();
	private boolean useGyroLock;
	private double gyroLockAngleDeg;
	private double kPGyro = 0.04;

	public DriveTrain() {
		try {
			leftDrive1 = new CANTalonEncoder(RobotMap.DRIVETRAIN_LEFT_MOTOR1_CAN_ID, ENCODER_TICKS_TO_INCHES, false, FeedbackDevice.QuadEncoder);
			leftDrive2 = new CANTalon(RobotMap.DRIVETRAIN_LEFT_MOTOR2_CAN_ID);
			leftDrive3 = new CANTalon(RobotMap.DRIVETRAIN_LEFT_MOTOR3_CAN_ID);
			
			rightDrive1 = new CANTalonEncoder(RobotMap.DRIVETRAIN_RIGHT_MOTOR1_CAN_ID, ENCODER_TICKS_TO_INCHES, true, FeedbackDevice.QuadEncoder);
			rightDrive2 = new CANTalon(RobotMap.DRIVETRAIN_RIGHT_MOTOR2_CAN_ID);
			rightDrive3 = new CANTalon(RobotMap.DRIVETRAIN_RIGHT_MOTOR3_CAN_ID);
			
			leftDrive2.changeControlMode(TalonControlMode.Follower);
			leftDrive2.set(leftDrive1.getDeviceID());
			leftDrive3.changeControlMode(TalonControlMode.Follower);
			leftDrive3.set(leftDrive1.getDeviceID());
			
			rightDrive2.changeControlMode(TalonControlMode.Follower);
			rightDrive2.set(rightDrive1.getDeviceID());
			rightDrive3.changeControlMode(TalonControlMode.Follower);
			rightDrive3.set(rightDrive1.getDeviceID());
	
			leftDrive1.reverseSensor(true);
			leftDrive1.reverseOutput(false);
			
			rightDrive1.reverseSensor(false);
			rightDrive1.reverseOutput(true);
			
			leftDrive1.enableBrakeMode(true);
			leftDrive2.enableBrakeMode(true);
			leftDrive3.enableBrakeMode(true);
			
			rightDrive1.enableBrakeMode(true);
			rightDrive2.enableBrakeMode(true);
			rightDrive3.enableBrakeMode(true);
			
			motorControllers.add(leftDrive1);
			motorControllers.add(rightDrive1);
			
			m_drive = new RobotDrive(leftDrive1, rightDrive1);
			m_drive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
			m_drive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
			m_drive.setSafetyEnabled(false);
			
			speedShift = new Solenoid(RobotMap.DRIVETRAIN_SPEEDSHIFT_PCM_ID);
			ptoShift = new DoubleSolenoid(RobotMap.DRIVETRAIN_WINCH_ENGAGE_PCM_ID, RobotMap.DRIVETRAIN_WINCH_DISENGAGE_PCM_ID);		
		}
		catch (Exception e) {
			System.err.println("An error occurred in the DriveTrain constructor");
		}
	}

	@Override
	public void initDefaultCommand() {
	}
	
	public double getGyroAngleDeg() {
		return gyro.getAngle();
	}
	
	public double getGyroRateDegPerSec() {
		return gyro.getRate();
	}
	
	public void setStraightMP(double distanceInches, double maxVelocity, boolean useGyroLock, double desiredAbsoluteAngle) {
		mpStraightController.setMPStraightTarget(0, distanceInches, maxVelocity, MP_STRAIGHT_T1, MP_STRAIGHT_T2, useGyroLock, BHRMathUtils.adjustAccumAngleToDesired(getGyroAngleDeg(), desiredAbsoluteAngle), true); 
		setControlMode(DriveTrainControlMode.MP_STRAIGHT);
	}
	
	public void setRelativeTurnMP(double relativeTurnAngleDeg, double turnRateDegPerSec, MPTurnType turnType) {
		mpTurnController.setMPTurnTarget(getGyroAngleDeg(), relativeTurnAngleDeg + getGyroAngleDeg(), turnRateDegPerSec, MP_TURN_T1, MP_TURN_T2, turnType, TRACK_WIDTH_INCHES);
		setControlMode(DriveTrainControlMode.MP_TURN);
	}
	
	public void setAbsoluteTurnMP(double absoluteTurnAngleDeg, double turnRateDegPerSec, MPTurnType turnType) {
		mpTurnController.setMPTurnTarget(getGyroAngleDeg(), BHRMathUtils.adjustAccumAngleToDesired(getGyroAngleDeg(), absoluteTurnAngleDeg), turnRateDegPerSec, MP_TURN_T1, MP_TURN_T2, turnType, TRACK_WIDTH_INCHES);
		setControlMode(DriveTrainControlMode.MP_TURN);
	}
	
	public void setControlMode(DriveTrainControlMode controlMode) {
 		this.controlMode = controlMode;
		if (controlMode == DriveTrainControlMode.JOYSTICK) {
			leftDrive1.changeControlMode(TalonControlMode.PercentVbus);
			rightDrive1.changeControlMode(TalonControlMode.PercentVbus);
		}
		else if (controlMode == DriveTrainControlMode.TEST) {
			leftDrive1.changeControlMode(TalonControlMode.PercentVbus);
			rightDrive1.changeControlMode(TalonControlMode.PercentVbus);
		}
		isFinished = false;
	}
	
	public void controlLoopUpdate() {
		if (controlMode == DriveTrainControlMode.JOYSTICK) {
			driveWithJoystick();
		}
		else if (!isFinished) {
			if (controlMode == DriveTrainControlMode.MP_STRAIGHT) {
				isFinished = mpStraightController.controlLoopUpdate(getGyroAngleDeg()); 
			}
			else if (controlMode == DriveTrainControlMode.MP_TURN) {
				isFinished = mpTurnController.controlLoopUpdate(getGyroAngleDeg()); 
			}
		}
	}
	
	public void setSpeed(double speed) {
		if (speed == 0) {
			setControlMode(DriveTrainControlMode.JOYSTICK);
		}
		else {
			setControlMode(DriveTrainControlMode.TEST);
			rightDrive1.set(speed);
			leftDrive1.set(speed);
		}
	}
	
	public void setGyroLock(boolean useGyroLock, boolean snapToAbsolute0or180) {
		if (snapToAbsolute0or180) {
			gyroLockAngleDeg = BHRMathUtils.adjustAccumAngleToClosest180(getGyroAngleDeg());
		}
		else {
			gyroLockAngleDeg = getGyroAngleDeg();
		}
		this.useGyroLock = useGyroLock;
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
		m_moveInput = OI.getInstance().getDriverJoystickPower().getY();
		m_steerInput = OI.getInstance().getDriverJoystickTurn().getX();

		m_moveOutput = adjustForSensitivity(m_moveScale, m_moveTrim,
				m_moveInput, m_moveNonLinear, MOVE_NON_LINEARITY);
		m_steerOutput = adjustForSensitivity(m_steerScale, m_steerTrim,
				m_steerInput, m_steerNonLinear, STEER_NON_LINEARITY);
		
		if (useGyroLock) {
			double yawError = gyroLockAngleDeg - getGyroAngleDeg();
			m_steerOutput = m_steerOutput * 0.1 + kPGyro * yawError;
		}
		
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

	public boolean isFinished() {
		return isFinished;
	}
	
	@Override
	public void setPeriodMs(long periodMs) {
		mpStraightController = new MPTalonPIDController(periodMs, mpStraightPIDParams, motorControllers);
		mpTurnController = new MPSoftwarePIDController(periodMs, mpTurnPIDParams, motorControllers);
	}
	
	public void updateStatus(RobotMain.OperationMode operationMode) {
		if (operationMode == RobotMain.OperationMode.TEST) {
			try {
				SmartDashboard.putNumber("Right Drive", rightDrive1.getPositionWorld());
				SmartDashboard.putNumber("Left Drive", leftDrive1.getPositionWorld());
				SmartDashboard.putNumber("Left Drive 1 Current", RobotMain.pdp.getCurrent(RobotMap.DRIVETRAIN_LEFT_MOTOR1_CAN_ID-2));
				SmartDashboard.putNumber("Left Drive 2 Current", RobotMain.pdp.getCurrent(RobotMap.DRIVETRAIN_LEFT_MOTOR2_CAN_ID-2));
				SmartDashboard.putNumber("Left Drive 3 Current", RobotMain.pdp.getCurrent(RobotMap.DRIVETRAIN_LEFT_MOTOR3_CAN_ID-2));
				SmartDashboard.putNumber("Right Drive 1 Current", RobotMain.pdp.getCurrent(RobotMap.DRIVETRAIN_RIGHT_MOTOR1_CAN_ID-2));
				SmartDashboard.putNumber("Right Drive 2 Current", RobotMain.pdp.getCurrent(RobotMap.DRIVETRAIN_RIGHT_MOTOR2_CAN_ID-2));
				SmartDashboard.putNumber("Right Drive 3 Current", RobotMain.pdp.getCurrent(RobotMap.DRIVETRAIN_RIGHT_MOTOR3_CAN_ID-2));
				SmartDashboard.putNumber("Yaw Angle", getGyroAngleDeg());
				MotionProfilePoint mpPoint = mpTurnController.getCurrentPoint(); 
				double delta = mpPoint != null ? getGyroAngleDeg() - mpTurnController.getCurrentPoint().position : 0;
				SmartDashboard.putNumber("Gyro Delta", delta);
			}
			catch (Exception e) {
				System.err.println("Drivetrain update status error");
			}
		}
	}	

}