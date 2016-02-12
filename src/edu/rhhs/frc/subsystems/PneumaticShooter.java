package edu.rhhs.frc.subsystems;

import edu.rhhs.frc.RobotMap;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

public class PneumaticShooter extends Subsystem
{
	public static enum ModuleState { EXTENDED, RETRACTED };
	public static enum CarriageState {RELEASED, LOCKED };
	public static enum ShotPosition { LOWER, UPPER };
	private Solenoid shotPosition, carriageRelease;
	private CANTalon winch;
	
	public PneumaticShooter() {
		this.shotPosition = new Solenoid(RobotMap.SHOOTER_POSITION_MODULE_ID);
		this.carriageRelease = new Solenoid(RobotMap.CARRIAGE_RELEASE_MODULE_ID);
		this.winch = new CANTalon(RobotMap.SHOOTER_WINCH_MOTOR_ID);
	}
	
	public void retractWinch() {
		//Set winch to a value.
	}
	
	public void spoolOutWinch() {
		//Set winch to a value.
	}
	
	public void setCarriageRelease(CarriageState cs) {
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
		shotPosition.set(v);
		carriageRelease.set(v);
	}

	@Override
	protected void initDefaultCommand() {
		
	}
}