package edu.rhhs.frc;

import edu.rhhs.frc.commands.DriveTrainPTOShift;
import edu.rhhs.frc.commands.DriveTrainSpeed;
import edu.rhhs.frc.commands.DriveTrainSpeedShift;
import edu.rhhs.frc.commands.DriveTrainStraightMP;
import edu.rhhs.frc.commands.IntakeInnerPosition;
import edu.rhhs.frc.commands.IntakeInnerSpeed;
import edu.rhhs.frc.commands.IntakeOuterPosition;
import edu.rhhs.frc.commands.IntakeOuterSpeed;
import edu.rhhs.frc.commands.ManipulatorArmSpeed;
import edu.rhhs.frc.commands.ManipulatorMoveMP;
import edu.rhhs.frc.commands.ManipulatorResetZero;
import edu.rhhs.frc.commands.ShooterCarriageState;
import edu.rhhs.frc.commands.ShooterShotPosition;
import edu.rhhs.frc.commands.ShooterWinchSpeed;
import edu.rhhs.frc.subsystems.DriveTrain.PTOShiftState;
import edu.rhhs.frc.subsystems.DriveTrain.SpeedShiftState;
import edu.rhhs.frc.subsystems.Intake.LiftState;
import edu.rhhs.frc.subsystems.Shooter.CarriageState;
import edu.rhhs.frc.subsystems.Shooter.ShotPosition;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.InternalButton;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI 
{
	private static OI instance;

	private Joystick m_joystick1;
	private Joystick m_joystick2;

	private OI() {
		m_joystick1 = new Joystick(0);
		m_joystick2 = new Joystick(1);
				
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
		manipulatorArmPositive.whenPressed(new ManipulatorArmSpeed(0.06));
		SmartDashboard.putData("Manipulator speed positive", manipulatorArmPositive);

		Button manipulatorArmNegative = new InternalButton();
		manipulatorArmNegative.whenPressed(new ManipulatorArmSpeed(-0.06));
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
		
		Button drivePos = new InternalButton();
		drivePos.whenPressed(new DriveTrainSpeed(1.0));
		SmartDashboard.putData("Drive positive", drivePos);
		
		Button driveOff = new InternalButton();
		driveOff.whenPressed(new DriveTrainSpeed(0.0));
		SmartDashboard.putData("Drive off", driveOff);
		
		Button driveMP = new InternalButton();
		driveMP.whenPressed(new DriveTrainStraightMP(48, 15, false, 0));
		SmartDashboard.putData("MotionProfile Drive", driveMP);
		
		Button armMP = new InternalButton();
		armMP.whenPressed(new ManipulatorMoveMP(45, 5));
		SmartDashboard.putData("MotionProfile Arm", armMP);

		Button resetArmZero = new InternalButton();
		resetArmZero.whenPressed(new ManipulatorResetZero());
		SmartDashboard.putData("Reset Arm Zero", resetArmZero);
	}
	
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