package edu.rhhs.frc.subsystems;

import edu.rhhs.frc.RobotMap;
import edu.rhhs.frc.utility.CANTalonEncoder;
import edu.rhhs.frc.utility.ControlLoopable;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Shooter extends Subsystem implements ControlLoopable
{
	public static enum CarriageState { RELEASED, LOCKED };
	public static enum ShotPosition { SHORT, LONG };
	private static final double ENCODER_TICKS_TO_WORLD = 0; //MAJOR TODO:
	private Solenoid shotPosition, carriageRelease;
	private CANTalonEncoder winch;
	private DigitalInput carriageRetracted1, carriageRetracted2;
	
	public Shooter() {
		this.shotPosition = new Solenoid(RobotMap.SHOOTER_POSITION_MODULE_ID);
		this.carriageRelease = new Solenoid(RobotMap.CARRIAGE_RELEASE_MODULE_ID);
		this.winch = new CANTalonEncoder(RobotMap.SHOOTER_WINCH_MOTOR_ID, ENCODER_TICKS_TO_WORLD);
		this.carriageRetracted1 = new DigitalInput(RobotMap.CARRIAGE_RETRACTED1_PORT_ID);
		this.carriageRetracted2 = new DigitalInput(RobotMap.CARRIAGE_RETRACTED2_PORT_ID);
	}
	
	public void retractWinch() {
		//Set winch to a value.
		if(!isRetracted()) winch.set(0.5);
	}

	public boolean isRetracted() {
		return carriageRetracted1.get() && carriageRetracted2.get();
	}
	
	public void spoolOutWinch() {
		//Set winch to a value.
		winch.set(-0.5);
	}
	
	public void setWinchSpeed(double speed) {
		winch.set(-speed);
	}
	
	public void setCarriageRelease(CarriageState state) {
		if(state == CarriageState.LOCKED) {
			carriageRelease.set(false);
		}
		else if (state == CarriageState.RELEASED) {
			carriageRelease.set(true);
		}
	}
	
	public void setShotPosition(ShotPosition position) {
		if(position == ShotPosition.LONG) {
			shotPosition.set(true);
		}
		else if(position == ShotPosition.SHORT) {
			shotPosition.set(false);
		}
	}

	@Override
	protected void initDefaultCommand() {
		
	}
	
	public void updateStatus() {
		
	}

	@Override
	public void controlLoopUpdate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPeriodMs(long periodMs) {
		// TODO Auto-generated method stub
		
	}
}