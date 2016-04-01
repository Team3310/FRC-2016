package edu.rhhs.frc;

import edu.rhhs.frc.buttons.DigitalIOSwitch;
import edu.rhhs.frc.buttons.XBoxDPadTriggerButton;
import edu.rhhs.frc.buttons.XBoxTriggerButton;
import edu.rhhs.frc.commands.CameraOffset;
import edu.rhhs.frc.commands.CameraReadAndProcessImage;
import edu.rhhs.frc.commands.CameraReadImageTurnToBestTarget;
import edu.rhhs.frc.commands.CameraSaveImage;
import edu.rhhs.frc.commands.CameraTurnToBestTarget;
import edu.rhhs.frc.commands.CameraUpdateBestTarget;
import edu.rhhs.frc.commands.CameraUpdateDashboard;
import edu.rhhs.frc.commands.DriveTrainAbsoluteTurnMP;
import edu.rhhs.frc.commands.DriveTrainClimberSet;
import edu.rhhs.frc.commands.DriveTrainGyroCalibrate;
import edu.rhhs.frc.commands.DriveTrainGyroLock;
import edu.rhhs.frc.commands.DriveTrainGyroReset;
import edu.rhhs.frc.commands.DriveTrainHold;
import edu.rhhs.frc.commands.DriveTrainMPCancel;
import edu.rhhs.frc.commands.DriveTrainRelativeMaxTurnMP;
import edu.rhhs.frc.commands.DriveTrainRelativeTurnMP;
import edu.rhhs.frc.commands.DriveTrainSpeed;
import edu.rhhs.frc.commands.DriveTrainSpeedShift;
import edu.rhhs.frc.commands.DriveTrainStraightMP;
import edu.rhhs.frc.commands.DriveTrainStraightMPLaser;
import edu.rhhs.frc.commands.IntakeEject;
import edu.rhhs.frc.commands.IntakeFullyDeploy;
import edu.rhhs.frc.commands.IntakeFullyRetract;
import edu.rhhs.frc.commands.IntakeInnerPosition;
import edu.rhhs.frc.commands.IntakeInnerSpeed;
import edu.rhhs.frc.commands.IntakeLowBarPosition;
import edu.rhhs.frc.commands.IntakeLowGoal;
import edu.rhhs.frc.commands.IntakeOff;
import edu.rhhs.frc.commands.IntakeOuterPosition;
import edu.rhhs.frc.commands.IntakeOuterSpeed;
import edu.rhhs.frc.commands.ManipulatorArmSpeed;
import edu.rhhs.frc.commands.ManipulatorMoveMP;
import edu.rhhs.frc.commands.ManipulatorResetZero;
import edu.rhhs.frc.commands.ShooterCameraAlign;
import edu.rhhs.frc.commands.ShooterCarriageState;
import edu.rhhs.frc.commands.ShooterShootAndRetract;
import edu.rhhs.frc.commands.ShooterShootAndRetractCamera;
import edu.rhhs.frc.commands.ShooterShotPosition;
import edu.rhhs.frc.commands.ShooterWinchRetractAndSpoolOut;
import edu.rhhs.frc.commands.ShooterWinchSafeRelease;
import edu.rhhs.frc.commands.ShooterWinchSpeed;
import edu.rhhs.frc.commands.ShooterWinchSpoolOut;
import edu.rhhs.frc.controller.XboxController;
import edu.rhhs.frc.subsystems.DriveTrain;
import edu.rhhs.frc.subsystems.DriveTrain.ClimberState;
import edu.rhhs.frc.subsystems.DriveTrain.SpeedShiftState;
import edu.rhhs.frc.subsystems.Intake;
import edu.rhhs.frc.subsystems.Intake.LiftState;
import edu.rhhs.frc.subsystems.Manipulator.ArmSide;
import edu.rhhs.frc.subsystems.Manipulator.PresetPositions;
import edu.rhhs.frc.subsystems.Shooter.CarriageState;
import edu.rhhs.frc.subsystems.Shooter.ShotPosition;
import edu.rhhs.frc.utility.MPSoftwarePIDController.MPSoftwareTurnType;
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

	private Joystick m_driverJoystickPower;
	private Joystick m_driverJoystickTurn;
	private XboxController m_operatorXBox;

	private OI() {
		m_driverJoystickPower = new Joystick(RobotMap.DRIVER_JOYSTICK_1_USB_ID);
		m_driverJoystickTurn = new Joystick(RobotMap.DRIVER_JOYSTICK_2_USB_ID);
		m_operatorXBox = new XboxController(RobotMap.OPERATOR_XBOX_USB_ID);
		
		// Driver's sticks
        JoystickButton shiftDrivetrain = new JoystickButton(m_driverJoystickPower, 1);
        shiftDrivetrain.whenPressed(new DriveTrainSpeedShift(DriveTrain.SpeedShiftState.HI));
        shiftDrivetrain.whenReleased(new DriveTrainSpeedShift(DriveTrain.SpeedShiftState.LO));

        JoystickButton manipulatorDeploy1 = new JoystickButton(m_driverJoystickPower, 3);
        manipulatorDeploy1.whenPressed(new ManipulatorMoveMP(PresetPositions.FULLY_DEPLOYED));

        JoystickButton manipulatorRetract1 = new JoystickButton(m_driverJoystickPower, 4);
        manipulatorRetract1.whenPressed(new ManipulatorMoveMP(PresetPositions.RETRACTED));

        JoystickButton intakeFullyDeploy1 = new JoystickButton(m_driverJoystickPower, 5);
        intakeFullyDeploy1.whenPressed(new IntakeFullyDeploy());
        intakeFullyDeploy1.whenReleased(new IntakeOff());

        JoystickButton intakeFullyRetract1 = new JoystickButton(m_driverJoystickPower, 6);
        intakeFullyRetract1.whenPressed(new IntakeFullyRetract());

        JoystickButton gyroLock = new JoystickButton(m_driverJoystickTurn, 1);
        gyroLock.whenPressed(new DriveTrainGyroLock(true, true));
        gyroLock.whenReleased(new DriveTrainGyroLock(false, false));

//        JoystickButton manipulatorPartiallyDeploy1 = new JoystickButton(m_driverJoystickTurn, 3);
//        manipulatorPartiallyDeploy1.whenPressed(new ManipulatorMoveMP(PresetPositions.PARTIALLY_DEPLOYED));
        
        JoystickButton turn180 = new JoystickButton(m_driverJoystickTurn, 3);
        turn180.whenPressed(new DriveTrainRelativeMaxTurnMP(180, DriveTrain.MAX_TURN_RATE_DEG_PER_SEC, MPSoftwareTurnType.TANK));

//        JoystickButton shooterShortPosition1 = new JoystickButton(m_driverJoystickTurn, 4);
//        shooterShortPosition1.whenPressed(new ShooterShotPosition(ShotPosition.SHORT));

        JoystickButton intakeLowBarPosition1 = new JoystickButton(m_driverJoystickTurn, 5);
        intakeLowBarPosition1.whenPressed(new IntakeLowBarPosition());

        JoystickButton intakeEject1 = new JoystickButton(m_driverJoystickTurn, 6);
        intakeEject1.whenPressed(new IntakeEject());
        intakeEject1.whenReleased(new IntakeOff());
        
        //JoystickButton shooterShoot1 = new JoystickButton(m_driverJoystickTurn, 2);
        //shooterShoot1.whenPressed(new ShooterShootAndRetract());

        JoystickButton retractWinch1 = new JoystickButton(m_driverJoystickTurn, 11);
        retractWinch1.whenPressed(new ShooterWinchRetractAndSpoolOut());

        JoystickButton safeReleaseWinch1 = new JoystickButton(m_driverJoystickTurn, 12);
        safeReleaseWinch1.whenPressed(new ShooterWinchSafeRelease());
        
        // Operator's controller
        JoystickButton manipulatorDeploy = new JoystickButton(m_operatorXBox.getJoyStick(), XboxController.Y_BUTTON);
        manipulatorDeploy.whenPressed(new ManipulatorMoveMP(PresetPositions.FULLY_DEPLOYED));

        JoystickButton manipulatorPartialDeploy = new JoystickButton(m_operatorXBox.getJoyStick(), XboxController.B_BUTTON);
        manipulatorPartialDeploy.whenPressed(new ManipulatorMoveMP(PresetPositions.PARTIALLY_DEPLOYED));

        JoystickButton manipulatorRetract = new JoystickButton(m_operatorXBox.getJoyStick(), XboxController.A_BUTTON);
        manipulatorRetract.whenPressed(new ManipulatorMoveMP(PresetPositions.RETRACTED));

//        JoystickButton shooterShortPosition = new JoystickButton(m_operatorXBox.getJoyStick(), XboxController.X_BUTTON);
//        shooterShortPosition.whenPressed(new ShooterShotPosition(ShotPosition.SHORT));

        XBoxDPadTriggerButton intakeFullyRetract = new XBoxDPadTriggerButton(m_operatorXBox, XBoxDPadTriggerButton.UP);
        intakeFullyRetract.whenPressed(new IntakeFullyRetract());

        XBoxDPadTriggerButton intakeLowBarPosition = new XBoxDPadTriggerButton(m_operatorXBox, XBoxDPadTriggerButton.DOWN);
        intakeLowBarPosition.whenPressed(new IntakeLowBarPosition());
        
        XBoxDPadTriggerButton intakeLowGoal = new XBoxDPadTriggerButton(m_operatorXBox, XBoxDPadTriggerButton.LEFT);
        intakeLowGoal.whenPressed(new IntakeLowGoal());

        JoystickButton intakeFullyDeploy = new JoystickButton(m_operatorXBox.getJoyStick(), XboxController.RIGHT_BUMPER_BUTTON);
        intakeFullyDeploy.whenPressed(new IntakeFullyDeploy());
        intakeFullyDeploy.whenReleased(new IntakeOff());

        JoystickButton intakeEject = new JoystickButton(m_operatorXBox.getJoyStick(), XboxController.LEFT_BUMPER_BUTTON);
        intakeEject.whenPressed(new IntakeEject());
        intakeEject.whenReleased(new IntakeOff());
        
        XBoxTriggerButton shooterShoot = new XBoxTriggerButton(m_operatorXBox, XBoxTriggerButton.RIGHT_TRIGGER);
        shooterShoot.whenPressed(new ShooterShootAndRetract(false));

        XBoxTriggerButton shooterShootCamera = new XBoxTriggerButton(m_operatorXBox, XBoxTriggerButton.LEFT_TRIGGER);
        shooterShootCamera.whenPressed(new ShooterShootAndRetractCamera());

// TODO DISTANCE        XBoxTriggerButton motionProfileClimb = new XBoxTriggerButton(m_operatorXBox, XBoxTriggerButton.LEFT_AXIS_UP_TRIGGER);
//        motionProfileClimb.whenPressed(new ShooterShootAndRetract(false));

        XBoxTriggerButton cancelClimbMP = new XBoxTriggerButton(m_operatorXBox, XBoxTriggerButton.LEFT_AXIS_DOWN_TRIGGER);
        cancelClimbMP.whenPressed(new DriveTrainMPCancel());
        
        XBoxTriggerButton driveTrainDisengagePTO = new XBoxTriggerButton(m_operatorXBox, XBoxTriggerButton.LEFT_AXIS_LEFT_TRIGGER);
        driveTrainDisengagePTO.whenPressed(new ShooterShotPosition(ShotPosition.SHORT));

        XBoxTriggerButton driveTrainEngagePTO = new XBoxTriggerButton(m_operatorXBox, XBoxTriggerButton.LEFT_AXIS_RIGHT_TRIGGER);
        driveTrainEngagePTO.whenPressed(new ShooterShotPosition(ShotPosition.LONG));
        
        JoystickButton retractWinch = new JoystickButton(m_operatorXBox.getJoyStick(), XboxController.BACK_BUTTON);
        retractWinch.whenPressed(new ShooterWinchRetractAndSpoolOut());
        
        JoystickButton deployClimber = new JoystickButton(m_operatorXBox.getJoyStick(), XboxController.X_BUTTON);
        deployClimber.whenPressed(new DriveTrainClimberSet(ClimberState.DEPLOYED));
        
        JoystickButton retractClimber = new JoystickButton(m_operatorXBox.getJoyStick(), XboxController.B_BUTTON);
        retractClimber.whenPressed(new DriveTrainClimberSet(ClimberState.RETRACTED));

        JoystickButton cameraAlign = new JoystickButton(m_operatorXBox.getJoyStick(), XboxController.LEFT_JOYSTICK_BUTTON);
        cameraAlign.whenPressed(new ShooterCameraAlign());

        JoystickButton safeReleaseWinch = new JoystickButton(m_operatorXBox.getJoyStick(), XboxController.START_BUTTON);
        safeReleaseWinch.whenPressed(new ShooterWinchSafeRelease());

        // Pneumatics Test
		Button drivetrainSpeedShiftHi = new InternalButton();
		drivetrainSpeedShiftHi.whenPressed(new DriveTrainSpeedShift(SpeedShiftState.HI));
		SmartDashboard.putData("Speed Shift HI", drivetrainSpeedShiftHi);

		Button drivetrainSpeedShiftLo = new InternalButton();
		drivetrainSpeedShiftLo.whenPressed(new DriveTrainSpeedShift(SpeedShiftState.LO));
		SmartDashboard.putData("Speed Shift LO", drivetrainSpeedShiftLo);

		Button drivetrainClimberDeployed = new InternalButton();
		drivetrainClimberDeployed.whenPressed(new DriveTrainClimberSet(ClimberState.DEPLOYED));
		SmartDashboard.putData("Climber Deploy", drivetrainClimberDeployed);

		Button drivetrainClimberRetracted = new InternalButton();
		drivetrainClimberRetracted.whenPressed(new DriveTrainClimberSet(ClimberState.RETRACTED));
		SmartDashboard.putData("Climber Retract", drivetrainClimberRetracted);
		
		Button drivetrainPTOShiftEngaged = new InternalButton();
		drivetrainPTOShiftEngaged.whenPressed(new ShooterShotPosition(ShotPosition.LONG));
		SmartDashboard.putData("PTO ENGAGED", drivetrainPTOShiftEngaged);

		Button drivetrainPTOShiftDisengaged = new InternalButton();
		drivetrainPTOShiftDisengaged.whenPressed(new ShooterShotPosition(ShotPosition.SHORT));
		SmartDashboard.putData("PTO DISENGAGED", drivetrainPTOShiftDisengaged);

		Button outerIntakeUp = new InternalButton();
		outerIntakeUp.whenPressed(new IntakeOuterPosition(LiftState.UP));
		SmartDashboard.putData("Intake Outer UP", outerIntakeUp);

		Button outerIntakeDown = new InternalButton();
		outerIntakeDown.whenPressed(new IntakeOuterPosition(LiftState.DOWN));
		SmartDashboard.putData("Intake Outer DOWN", outerIntakeDown);

		Button innerIntakeUp = new InternalButton();
		innerIntakeUp.whenPressed(new IntakeInnerPosition(LiftState.UP));
		SmartDashboard.putData("Intake Inner UP", innerIntakeUp);

		Button innerIntakeDown = new InternalButton();
		innerIntakeDown.whenPressed(new IntakeInnerPosition(LiftState.DOWN));
		SmartDashboard.putData("Intake Inner DOWN", innerIntakeDown);

		Button shooterShotPositionLong = new InternalButton();
		shooterShotPositionLong.whenPressed(new ShooterShotPosition(ShotPosition.LONG));
		SmartDashboard.putData("Shot Position LONG", shooterShotPositionLong);

		Button shooterShotPositionShort = new InternalButton();
		shooterShotPositionShort.whenPressed(new ShooterShotPosition(ShotPosition.SHORT));
		SmartDashboard.putData("Shot Position SHORT", shooterShotPositionShort);

		Button shooterCarriageReleased = new InternalButton();
		shooterCarriageReleased.whenPressed(new ShooterCarriageState(CarriageState.RELEASED));
		SmartDashboard.putData("Carriage RELEASED", shooterCarriageReleased);

		Button shooterCarriageLocked = new InternalButton();
		shooterCarriageLocked.whenPressed(new ShooterCarriageState(CarriageState.LOCKED));
		SmartDashboard.putData("Carriage LOCKED", shooterCarriageLocked);
		
		// Motors
		Button manipulatorArmPositive = new InternalButton();
		manipulatorArmPositive.whileHeld(new ManipulatorArmSpeed(0.15, ArmSide.BOTH));
		manipulatorArmPositive.whenReleased(new ManipulatorArmSpeed(0, ArmSide.BOTH));
		SmartDashboard.putData("Manip Positive", manipulatorArmPositive);

		Button manipulatorArmNegative = new InternalButton();
		manipulatorArmNegative.whileHeld(new ManipulatorArmSpeed(-0.15, ArmSide.BOTH));
		manipulatorArmNegative.whenReleased(new ManipulatorArmSpeed(0, ArmSide.BOTH));
		SmartDashboard.putData("Manip Negative", manipulatorArmNegative);
		
		Button outerIntakeOn = new InternalButton();
		outerIntakeOn.whenPressed(new IntakeOuterSpeed(Intake.OUTER_INTAKE_LOAD_SPEED));
		SmartDashboard.putData("Outer Roller On", outerIntakeOn);

		Button outerIntakeOff = new InternalButton();
		outerIntakeOff.whenPressed(new IntakeOuterSpeed(0.0));
		SmartDashboard.putData("Outer Roller Off", outerIntakeOff);

		Button innerIntakeOn = new InternalButton();
		innerIntakeOn.whenPressed(new IntakeInnerSpeed(Intake.INNER_INTAKE_LOAD_SPEED));
		SmartDashboard.putData("Inner Roller On", innerIntakeOn);

		Button innerIntakeOff = new InternalButton();
		innerIntakeOff.whenPressed(new IntakeInnerSpeed(0.0));
		SmartDashboard.putData("Inner Roller Off", innerIntakeOff);

		Button shooterWinchPositive = new InternalButton();
		shooterWinchPositive.whileHeld(new ShooterWinchSpeed(0.5));
		shooterWinchPositive.whenReleased(new ShooterWinchSpeed(0.0));
		SmartDashboard.putData("Winch Speed Pos", shooterWinchPositive);

		Button shooterWinchNegative = new InternalButton();
		shooterWinchNegative.whileHeld(new ShooterWinchSpeed(-0.5));
		shooterWinchNegative.whenReleased(new ShooterWinchSpeed(0.0));
		SmartDashboard.putData("Winch Speed Neg", shooterWinchNegative);
		
		Button shooterWinchRetract = new InternalButton();
		shooterWinchRetract.whenPressed(new ShooterWinchRetractAndSpoolOut());
		SmartDashboard.putData("Winch Retract", shooterWinchRetract);
		
		Button shooterWinchSpoolOut = new InternalButton();
		shooterWinchSpoolOut.whenPressed(new ShooterWinchSpoolOut());
		SmartDashboard.putData("Winch Spool Out", shooterWinchSpoolOut);
		
		Button shooterShootAndRetract = new InternalButton();
		shooterShootAndRetract.whenPressed(new ShooterShootAndRetract(false));
		SmartDashboard.putData("Shoot and Retract", shooterShootAndRetract);
		
		Button drivePos05 = new InternalButton();
		drivePos05.whenPressed(new DriveTrainSpeed(0.5));
		SmartDashboard.putData("Drive 0.5", drivePos05);
		
		Button drivePos08 = new InternalButton();
		drivePos08.whenPressed(new DriveTrainSpeed(0.8));
		SmartDashboard.putData("Drive 0.8", drivePos08);
		
		Button drivePos10 = new InternalButton();
		drivePos10 .whenPressed(new DriveTrainSpeed(1.0));
		SmartDashboard.putData("Drive 1.0", drivePos10 );
		
		Button driveOff = new InternalButton();
		driveOff.whenPressed(new DriveTrainSpeed(0.0));
		SmartDashboard.putData("Drive off", driveOff);
		
		Button driveMPLaser = new InternalButton();
		driveMPLaser.whenPressed(new DriveTrainStraightMPLaser(36, DriveTrain.MP_LASER_SEARCH_VELOCITY_INCHES_PER_SEC, true, false, 0));
		SmartDashboard.putData("Drive Straight Laser", driveMPLaser);
		
		Button driveMP = new InternalButton();
		driveMP.whenPressed(new DriveTrainStraightMP(96, DriveTrain.MP_AUTON_MAX_STRAIGHT_VELOCITY_INCHES_PER_SEC, true, false, 0));
		SmartDashboard.putData("Drive Straight", driveMP);
		
		Button turnRelativeMP = new InternalButton();
		turnRelativeMP.whenPressed(new DriveTrainRelativeTurnMP(60, DriveTrain.MP_AUTON_MAX_TURN_RATE_DEG_PER_SEC, MPSoftwareTurnType.TANK));
		SmartDashboard.putData("Turn Relative", turnRelativeMP);
		
		Button turnAbsoluteMP = new InternalButton();
		turnAbsoluteMP.whenPressed(new DriveTrainAbsoluteTurnMP(0, DriveTrain.MP_AUTON_MAX_TURN_RATE_DEG_PER_SEC, MPSoftwareTurnType.TANK));
		SmartDashboard.putData("Turn Absolute", turnAbsoluteMP);
		
		Button armMPDeploy = new InternalButton();
		armMPDeploy.whenPressed(new ManipulatorMoveMP(PresetPositions.FULLY_DEPLOYED));
		SmartDashboard.putData("Arm Deploy", armMPDeploy);

		Button armMPPartialDeploy = new InternalButton();
		armMPPartialDeploy.whenPressed(new ManipulatorMoveMP(PresetPositions.PARTIALLY_DEPLOYED));
		SmartDashboard.putData("Arm Partial Deploy", armMPPartialDeploy);

		Button armMPZero = new InternalButton();
		armMPZero.whenPressed(new ManipulatorMoveMP(PresetPositions.ZERO));
		SmartDashboard.putData("Arm Zero Position", armMPZero);

		Button armMPRetract = new InternalButton();
		armMPRetract.whenPressed(new ManipulatorMoveMP(PresetPositions.RETRACTED));
		SmartDashboard.putData("Arm Retract", armMPRetract);

		Button resetArmZero = new InternalButton();
		resetArmZero.whenPressed(new ManipulatorResetZero());
		SmartDashboard.putData("Reset Arm Zero", resetArmZero);
		
		Button gyroReset = new InternalButton();
		gyroReset.whenPressed(new DriveTrainGyroReset());
		SmartDashboard.putData("Gyro Reset", gyroReset);
		
		Button gyroCalibrate = new InternalButton();
		gyroCalibrate.whenPressed(new DriveTrainGyroCalibrate());
		SmartDashboard.putData("Gyro Calibrate", gyroCalibrate);
		
		Button drivetrainHoldOn = new InternalButton();
		drivetrainHoldOn.whenPressed(new DriveTrainHold(true));
		SmartDashboard.putData("Drive Hold On", drivetrainHoldOn);
		
		Button drivetrainHoldOff = new InternalButton();
		drivetrainHoldOff.whenPressed(new DriveTrainHold(false));
		SmartDashboard.putData("Drive Hold Off", drivetrainHoldOff);
		
		Button cameraUpdateDashboard = new InternalButton();
		cameraUpdateDashboard.whenPressed(new CameraUpdateDashboard());
		SmartDashboard.putData("Camera Update", cameraUpdateDashboard);
		
		Button cameraSaveImage = new InternalButton();
		cameraSaveImage.whenPressed(new CameraSaveImage());
		SmartDashboard.putData("Camera Save", cameraSaveImage);
		
		Button cameraReadImage = new InternalButton();
		cameraReadImage.whenPressed(new CameraReadAndProcessImage());
		SmartDashboard.putData("Camera Read", cameraReadImage);
		
		Button cameraReadImageTurnToBestTarget = new InternalButton();
		cameraReadImageTurnToBestTarget.whenPressed(new CameraReadImageTurnToBestTarget());
		SmartDashboard.putData("Camera Read Turn", cameraReadImageTurnToBestTarget);
		
		Button cameraTurnToBestTarget = new InternalButton();
		cameraTurnToBestTarget.whenPressed(new CameraTurnToBestTarget());
		SmartDashboard.putData("Camera Turn To Best", cameraTurnToBestTarget);
		
		Button cameraUpdateBestTarget = new InternalButton();
		cameraUpdateBestTarget.whenPressed(new CameraUpdateBestTarget());
		SmartDashboard.putData("Camera Update Best", cameraUpdateBestTarget);
		
		Button incrementCameraOffsetPos = new InternalButton();
		incrementCameraOffsetPos.whenPressed(new CameraOffset(0.5));
		SmartDashboard.putData("Camera Offset Pos", incrementCameraOffsetPos);
		
		Button incrementCameraOffsetNeg = new InternalButton();
		incrementCameraOffsetNeg.whenPressed(new CameraOffset(-0.5));
		SmartDashboard.putData("Camera Offset Neg", incrementCameraOffsetNeg);
		
		DigitalIOSwitch cdfSwitch = new DigitalIOSwitch(RobotMap.CDF_SENSOR_DIO_PORT_ID);
		cdfSwitch.whenPressed(new ManipulatorMoveMP(PresetPositions.FULLY_DEPLOYED, true));
	}
	
	public Joystick getDriverJoystickPower() {
		return m_driverJoystickPower;
	}
	
	public Joystick getDriverJoystickTurn() {
		return m_driverJoystickTurn;
	}

	public XboxController getOperatorXBox() {
		return m_operatorXBox;
	}

	public static OI getInstance() {
		if(instance == null) {
			instance = new OI();
		}
		return instance;
	}
}