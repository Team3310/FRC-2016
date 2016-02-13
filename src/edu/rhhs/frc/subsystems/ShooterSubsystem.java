package edu.rhhs.frc.subsystems;

import edu.rhhs.frc.RobotMap;
import edu.rhhs.frc.utility.CANTalonEncoder;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;

public class ShooterSubsystem extends MPSubsystem
{
	public static enum CarriageState {RELEASED, LOCKED };
	public static enum ShotPosition { SHORT, LONG };
	private static final long LOOP_PERIOD_MS = 10;
	private static final double ENCODER_TICKS_TO_WORLD = 0; //MAJOR TODO:
	private Solenoid shotPosition, carriageRelease;
	private CANTalonEncoder winch;
	private DigitalInput carriageRetracted1, carriageRetracted2;
	
	public ShooterSubsystem() {
		super(LOOP_PERIOD_MS);
		this.shotPosition = new Solenoid(RobotMap.SHOOTER_POSITION_MODULE_ID);
		this.carriageRelease = new Solenoid(RobotMap.CARRIAGE_RELEASE_MODULE_ID);
		this.winch = new CANTalonEncoder(RobotMap.SHOOTER_WINCH_MOTOR_ID, ENCODER_TICKS_TO_WORLD);
		this.carriageRetracted1 = new DigitalInput(RobotMap.CARRIAGE_RETRACTED1_PORT_ID);
		this.carriageRetracted2 = new DigitalInput(RobotMap.CARRIAGE_RETRACTED2_PORT_ID);
	}
	
	public void retractWinch() {
		//Set winch to a value.
		if(!carriageRetracted1.get() && !carriageRetracted2.get()) winch.set(0.5);
	}

	public boolean isRetracted() {
		return carriageRetracted1.get() && carriageRetracted2.get();
	}
	
	public void spoolOutWinch() {
		//Set winch to a value.
		winch.set(-0.5);
	}
	
	public void setCarriageRelease(CarriageState cs) {
		if(cs == CarriageState.RELEASED) carriageRelease.set(false);
		else if (cs == CarriageState.LOCKED) carriageRelease.set(true);
	}
	
	public void setShotPosition(ShotPosition sp) {
		if(sp == ShotPosition.LONG) shotPosition.set(true);
		else if(sp == ShotPosition.SHORT) shotPosition.set(false);
	}

	@Override
	protected void initDefaultCommand() {
		
	}
}