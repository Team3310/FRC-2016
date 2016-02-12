package edu.rhhs.frc.subsystems;

import edu.rhhs.frc.RobotMap;
import edu.rhhs.frc.subsystems.PneumaticShooter.CarriageState;
import edu.rhhs.frc.subsystems.PneumaticShooter.ModuleState;
import edu.rhhs.frc.subsystems.PneumaticShooter.ShotPosition;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

public class IntakeSubsystem extends Subsystem
{
	public static enum IntakePosition { UP, DOWN };
	private Solenoid outerLift, innerLift;
	private CANTalon outerRoller, innerRoller;
	
	public IntakeSubsystem() {
		this.outerRoller = new CANTalon(RobotMap.INTAKE_OUTER_ROLLER_MOTOR_ID);
		this.innerRoller = new CANTalon(RobotMap.INTAKE_INNER_ROLLER_MOTOR_ID);
		//this.outerRoller = new Solenoid(RobotMap.SHOOTER_POSITION_MODULE_ID);
		//this.innerRoller = new Solenoid(RobotMap.CARRIAGE_RELEASE_MODULE_ID);
	}
	
	public void setSpeedOuter(double speed) {
		outerRoller.set(speed);
	}
	
	public void setSpeedInner(double speed) {
		innerRoller.set(speed);
	}
	
	public void setPositionOuter(CarriageState cs) {
		//add implementation
	}
	
	public void setShotPosition(ShotPosition sp) {
		
	}
	
	public void setState(ModuleState ms) {
		boolean v;
		switch(ms) {
			case EXTENDED:
				v = true;
				break;
			case RETRACTED:
				v = false;
				break;
			default:
				v = false;
				break;
		}
		outerLift.set(v);
		innerLift.set(v);
	}

	@Override
	protected void initDefaultCommand() {
		
	}
}