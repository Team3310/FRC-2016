package edu.rhhs.frc;

import edu.rhhs.frc.buttons.XBoxDPadTriggerButton;
import edu.rhhs.frc.buttons.XBoxTriggerButton;
import edu.rhhs.frc.commands.DriveTrainPTOShift;
import edu.rhhs.frc.commands.DriveTrainSpeed;
import edu.rhhs.frc.commands.DriveTrainSpeedShift;
import edu.rhhs.frc.commands.DriveTrainStraightMP;
import edu.rhhs.frc.commands.DriveTrainTurnMP;
import edu.rhhs.frc.commands.IntakeEject;
import edu.rhhs.frc.commands.IntakeFullyDeploy;
import edu.rhhs.frc.commands.IntakeFullyRetract;
import edu.rhhs.frc.commands.IntakeInnerPosition;
import edu.rhhs.frc.commands.IntakeInnerSpeed;
import edu.rhhs.frc.commands.IntakeLowBarPosition;
import edu.rhhs.frc.commands.IntakeOff;
import edu.rhhs.frc.commands.IntakeOuterPosition;
import edu.rhhs.frc.commands.IntakeOuterSpeed;
import edu.rhhs.frc.commands.ManipulatorArmSpeed;
import edu.rhhs.frc.commands.ManipulatorMoveMP;
import edu.rhhs.frc.commands.ManipulatorResetZero;
import edu.rhhs.frc.commands.ShooterCarriageState;
import edu.rhhs.frc.commands.ShooterShootAndRetract;
import edu.rhhs.frc.commands.ShooterShotPosition;
import edu.rhhs.frc.commands.ShooterWinchRetractAndSpoolOut;
import edu.rhhs.frc.commands.ShooterWinchSafeRelease;
import edu.rhhs.frc.commands.ShooterWinchSpeed;
import edu.rhhs.frc.commands.ShooterWinchSpoolOut;
import edu.rhhs.frc.controller.XboxController;
import edu.rhhs.frc.subsystems.DriveTrain;
import edu.rhhs.frc.subsystems.DriveTrain.PTOShiftState;
import edu.rhhs.frc.subsystems.DriveTrain.SpeedShiftState;
import edu.rhhs.frc.subsystems.Intake.LiftState;
import edu.rhhs.frc.subsystems.Manipulator.PresetPositions;
import edu.rhhs.frc.subsystems.Shooter.CarriageState;
import edu.rhhs.frc.subsystems.Shooter.ShotPosition;
import edu.rhhs.frc.utility.MPTalonPIDController.MPTurnType;
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

	private Joystick m_driverJoystick1;
	private Joystick m_driverJoystick2;
	private XboxController m_operatorXBox;

	private OI() {
		m_driverJoystick1 = new Joystick(RobotMap.DRIVER_JOYSTICK_1_USB_ID);
		m_driverJoystick2 = new Joystick(RobotMap.DRIVER_JOYSTICK_2_USB_ID);
		m_operatorXBox = new XboxController(RobotMap.OPERATOR_XBOX_USB_ID);
		
		// Driver's sticks
        JoystickButton shiftDrivetrain = new JoystickButton(m_driverJoystick1, 1);
        shiftDrivetrain.whenPressed(new DriveTrainSpeedShift(DriveTrain.SpeedShiftState.HI));
        shiftDrivetrain.whenReleased(new DriveTrainSpeedShift(DriveTrain.SpeedShiftState.LO));

        JoystickButton manipulatorDeploy1 = new JoystickButton(m_driverJoystick1, 3);
        manipulatorDeploy1.whenPressed(new ManipulatorMoveMP(PresetPositions.FULLY_DEPLOYED));

        JoystickButton manipulatorRetract1 = new JoystickButton(m_driverJoystick1, 4);
        manipulatorRetract1.whenPressed(new ManipulatorMoveMP(PresetPositions.RETRACTED));

        JoystickButton intakeFullyDeploy1 = new JoystickButton(m_driverJoystick1, 5);
        intakeFullyDeploy1.whenPressed(new IntakeFullyDeploy());
        intakeFullyDeploy1.whenReleased(new IntakeOff());

        JoystickButton intakeFullyRetract1 = new JoystickButton(m_driverJoystick1, 6);
        intakeFullyRetract1.whenPressed(new IntakeFullyRetract());

        JoystickButton manipulatorPartiallyDeploy1 = new JoystickButton(m_driverJoystick2, 3);
        manipulatorPartiallyDeploy1.whenPressed(new ManipulatorMoveMP(PresetPositions.PARTIALLY_DEPLOYED));

        JoystickButton shooterShortPosition1 = new JoystickButton(m_driverJoystick2, 4);
        shooterShortPosition1.whenPressed(new ShooterShotPosition(ShotPosition.SHORT));

        JoystickButton intakeLowBarPosition1 = new JoystickButton(m_driverJoystick2, 5);
        intakeLowBarPosition1.whenPressed(new IntakeLowBarPosition());

        JoystickButton intakeEject1 = new JoystickButton(m_driverJoystick2, 6);
        intakeEject1.whenPressed(new IntakeEject());
        intakeEject1.whenReleased(new IntakeOff());
        
        JoystickButton shooterShoot1 = new JoystickButton(m_driverJoystick2, 2);
        shooterShoot1.whenPressed(new ShooterShootAndRetract());

        JoystickButton retractWinch1 = new JoystickButton(m_driverJoystick2, 11);
        retractWinch1.whenPressed(new ShooterWinchRetractAndSpoolOut());

        JoystickButton safeReleaseWinch1 = new JoystickButton(m_driverJoystick2, 12);
        safeReleaseWinch1.whenPressed(new ShooterWinchSafeRelease());
        
        // Operator's controller
        JoystickButton manipulatorDeploy = new JoystickButton(m_operatorXBox.getJoyStick(), XboxController.Y_BUTTON);
        manipulatorDeploy.whenPressed(new ManipulatorMoveMP(PresetPositions.FULLY_DEPLOYED));

        JoystickButton manipulatorPartialDeploy = new JoystickButton(m_operatorXBox.getJoyStick(), XboxController.B_BUTTON);
        manipulatorPartialDeploy.whenPressed(new ManipulatorMoveMP(PresetPositions.PARTIALLY_DEPLOYED));

        JoystickButton manipulatorRetract = new JoystickButton(m_operatorXBox.getJoyStick(), XboxController.A_BUTTON);
        manipulatorRetract.whenPressed(new ManipulatorMoveMP(PresetPositions.RETRACTED));

        JoystickButton shooterShortPosition = new JoystickButton(m_operatorXBox.getJoyStick(), XboxController.X_BUTTON);
        shooterShortPosition.whenPressed(new ShooterShotPosition(ShotPosition.SHORT));

        XBoxDPadTriggerButton intakeFullyRetract = new XBoxDPadTriggerButton(m_operatorXBox, XBoxDPadTriggerButton.UP);
        intakeFullyRetract.whenPressed(new IntakeFullyRetract());

        XBoxDPadTriggerButton intakeLowBarPosition = new XBoxDPadTriggerButton(m_operatorXBox, XBoxDPadTriggerButton.DOWN);
        intakeLowBarPosition.whenPressed(new IntakeLowBarPosition());

        JoystickButton intakeFullyDeploy = new JoystickButton(m_operatorXBox.getJoyStick(), XboxController.RIGHT_BUMPER_BUTTON);
        intakeFullyDeploy.whenPressed(new IntakeFullyDeploy());
        intakeFullyDeploy.whenReleased(new IntakeOff());

        JoystickButton intakeEject = new JoystickButton(m_operatorXBox.getJoyStick(), XboxController.LEFT_BUMPER_BUTTON);
        intakeEject.whenPressed(new IntakeEject());
        intakeEject.whenReleased(new IntakeOff());
        
        XBoxTriggerButton shooterShoot = new XBoxTriggerButton(m_operatorXBox, XBoxTriggerButton.RIGHT_TRIGGER);
        shooterShoot.whenPressed(new ShooterShootAndRetract());

        JoystickButton retractWinch = new JoystickButton(m_operatorXBox.getJoyStick(), XboxController.BACK_BUTTON);
        retractWinch.whenPressed(new ShooterWinchRetractAndSpoolOut());

        JoystickButton safeReleaseWinch = new JoystickButton(m_operatorXBox.getJoyStick(), XboxController.START_BUTTON);
        safeReleaseWinch.whenPressed(new ShooterWinchSafeRelease());

        // Pneumatics Test
		Button drivetrainSpeedShiftHi = new InternalButton();
		drivetrainSpeedShiftHi.whenPressed(new DriveTrainSpeedShift(SpeedShiftState.HI));
		SmartDashboard.putData("Drivetrain speed shift HI", drivetrainSpeedShiftHi);

		Button drivetrainSpeedShiftLo = new InternalButton();
		drivetrainSpeedShiftLo.whenPressed(new DriveTrainSpeedShift(SpeedShiftState.LO));
		SmartDashboard.putData("Drivetrain speed shift LO", drivetrainSpeedShiftLo);

		Button drivetrainPTOShiftEngaged = new InternalButton();
		drivetrainPTOShiftEngaged.whenPressed(new DriveTrainPTOShift(PTOShiftState.ENGAGED));
		SmartDashboard.putData("Drivetrain PTO shift ENGAGED", drivetrainPTOShiftEngaged);

		Button drivetrainPTOShiftDisengaged = new InternalButton();
		drivetrainPTOShiftDisengaged.whenPressed(new DriveTrainPTOShift(PTOShiftState.DISENGAGED));
		SmartDashboard.putData("Drivetrain PTO shift DISENGAGED", drivetrainPTOShiftDisengaged);

		Button outerIntakeUp = new InternalButton();
		outerIntakeUp.whenPressed(new IntakeOuterPosition(LiftState.UP));
		SmartDashboard.putData("Intake outer UP", outerIntakeUp);

		Button outerIntakeDown = new InternalButton();
		outerIntakeDown.whenPressed(new IntakeOuterPosition(LiftState.DOWN));
		SmartDashboard.putData("Intake outer DOWN", outerIntakeDown);

		Button innerIntakeUp = new InternalButton();
		innerIntakeUp.whenPressed(new IntakeInnerPosition(LiftState.UP));
		SmartDashboard.putData("Intake inner UP", innerIntakeUp);

		Button innerIntakeDown = new InternalButton();
		innerIntakeDown.whenPressed(new IntakeInnerPosition(LiftState.DOWN));
		SmartDashboard.putData("Intake inner DOWN", innerIntakeDown);

		Button shooterShotPositionLong = new InternalButton();
		shooterShotPositionLong.whenPressed(new ShooterShotPosition(ShotPosition.LONG));
		SmartDashboard.putData("Shooter shot position LONG", shooterShotPositionLong);

		Button shooterShotPositionShort = new InternalButton();
		shooterShotPositionShort.whenPressed(new ShooterShotPosition(ShotPosition.SHORT));
		SmartDashboard.putData("Shooter shot position SHORT", shooterShotPositionShort);

		Button shooterCarriageReleased = new InternalButton();
		shooterCarriageReleased.whenPressed(new ShooterCarriageState(CarriageState.RELEASED));
		SmartDashboard.putData("Shooter carriage RELEASED", shooterCarriageReleased);

		Button shooterCarriageLocked = new InternalButton();
		shooterCarriageLocked.whenPressed(new ShooterCarriageState(CarriageState.LOCKED));
		SmartDashboard.putData("Shooter carriage LOCKED", shooterCarriageLocked);
		
		// Motors
		Button manipulatorArmPositive = new InternalButton();
		manipulatorArmPositive.whenPressed(new ManipulatorArmSpeed(0.2));
		SmartDashboard.putData("Manipulator speed positive", manipulatorArmPositive);

		Button manipulatorArmNegative = new InternalButton();
		manipulatorArmNegative.whenPressed(new ManipulatorArmSpeed(-0.2));
		SmartDashboard.putData("Manipulator speed negative", manipulatorArmNegative);
		
		Button manipulatorArmOff = new InternalButton();
		manipulatorArmOff.whenPressed(new ManipulatorArmSpeed(0.0));
		SmartDashboard.putData("Manipulator speed off", manipulatorArmOff);
		
		Button outerIntakeOn = new InternalButton();
		outerIntakeOn.whenPressed(new IntakeOuterSpeed(1.0));
		SmartDashboard.putData("Intake outer roller on", outerIntakeOn);

		Button outerIntakeOff = new InternalButton();
		outerIntakeOff.whenPressed(new IntakeOuterSpeed(0.0));
		SmartDashboard.putData("Intake outer roller off", outerIntakeOff);

		Button innerIntakeOn = new InternalButton();
		innerIntakeOn.whenPressed(new IntakeInnerSpeed(0.7));
		SmartDashboard.putData("Intake inner roller on", innerIntakeOn);

		Button innerIntakeOff = new InternalButton();
		innerIntakeOff.whenPressed(new IntakeInnerSpeed(0.0));
		SmartDashboard.putData("Intake inner roller off", innerIntakeOff);

		Button shooterWinchPositive = new InternalButton();
		shooterWinchPositive.whenPressed(new ShooterWinchSpeed(1.0));
		SmartDashboard.putData("Shooter winch speed positive", shooterWinchPositive);

		Button shooterWinchNegative = new InternalButton();
		shooterWinchNegative.whenPressed(new ShooterWinchSpeed(-1.0));
		SmartDashboard.putData("Shooter winch speed negative", shooterWinchNegative);
		
		Button shooterWinchOff = new InternalButton();
		shooterWinchOff.whenPressed(new ShooterWinchSpeed(0.0));
		SmartDashboard.putData("Shooter winch speed off", shooterWinchOff);
		
		Button shooterWinchRetract = new InternalButton();
		shooterWinchRetract.whenPressed(new ShooterWinchRetractAndSpoolOut());
		SmartDashboard.putData("Shooter winch retract", shooterWinchRetract);
		
		Button shooterWinchSpoolOut = new InternalButton();
		shooterWinchSpoolOut.whenPressed(new ShooterWinchSpoolOut());
		SmartDashboard.putData("Shooter winch spool out", shooterWinchSpoolOut);
		
		Button drivePos05 = new InternalButton();
		drivePos05.whenPressed(new DriveTrainSpeed(0.5));
		SmartDashboard.putData("Drive positive 0.5", drivePos05);
		
		Button drivePos08 = new InternalButton();
		drivePos08.whenPressed(new DriveTrainSpeed(0.8));
		SmartDashboard.putData("Drive positive 0.8", drivePos08);
		
		Button drivePos10 = new InternalButton();
		drivePos10 .whenPressed(new DriveTrainSpeed(1.0));
		SmartDashboard.putData("Drive positive 1.0", drivePos10 );
		
		Button driveOff = new InternalButton();
		driveOff.whenPressed(new DriveTrainSpeed(0.0));
		SmartDashboard.putData("Drive off", driveOff);
		
		Button driveMP = new InternalButton();
		driveMP.whenPressed(new DriveTrainStraightMP(48, 15, false, 0));
		SmartDashboard.putData("MotionProfile Drive", driveMP);
		
		Button turnMP = new InternalButton();
		turnMP.whenPressed(new DriveTrainTurnMP(90, 45, MPTurnType.TANK));
		SmartDashboard.putData("MotionProfile Turn", turnMP);
		
		Button armMPDeploy = new InternalButton();
		armMPDeploy.whenPressed(new ManipulatorMoveMP(PresetPositions.FULLY_DEPLOYED));
		SmartDashboard.putData("MotionProfile Arm Deploy", armMPDeploy);

		Button armMPPartialDeploy = new InternalButton();
		armMPPartialDeploy.whenPressed(new ManipulatorMoveMP(PresetPositions.PARTIALLY_DEPLOYED));
		SmartDashboard.putData("MotionProfile Arm Partial Deploy", armMPPartialDeploy);

		Button armMPZero = new InternalButton();
		armMPZero.whenPressed(new ManipulatorMoveMP(PresetPositions.ZERO));
		SmartDashboard.putData("MotionProfile Arm Zero Position", armMPZero);

		Button armMPRetract = new InternalButton();
		armMPRetract.whenPressed(new ManipulatorMoveMP(PresetPositions.RETRACTED));
		SmartDashboard.putData("MotionProfile Arm Retract", armMPRetract);

		Button resetArmZero = new InternalButton();
		resetArmZero.whenPressed(new ManipulatorResetZero());
		SmartDashboard.putData("Reset Arm Zero", resetArmZero);
	}
	
	public Joystick getDriverJoystick1() {
		return m_driverJoystick1;
	}
	
	public Joystick getDriverJoystick2() {
		return m_driverJoystick2;
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