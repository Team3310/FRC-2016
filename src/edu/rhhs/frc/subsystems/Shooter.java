package edu.rhhs.frc.subsystems;

import edu.rhhs.frc.RobotMain;
import edu.rhhs.frc.RobotMap;
import edu.rhhs.frc.utility.CANTalonEncoder;
import edu.rhhs.frc.utility.ControlLoopable;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Shooter extends Subsystem implements ControlLoopable
{
	public static enum CarriageState { RELEASED, LOCKED };
	public static enum ShotPosition { SHORT, LONG };
	private static final double ENCODER_TICKS_TO_WORLD = 0; //MAJOR TODO:

	private CANTalonEncoder winch;
	private Solenoid shotPosition, carriageRelease;
	private DigitalInput carriageRetracted;
	
	public Shooter() {
		try {
			winch = new CANTalonEncoder(RobotMap.SHOOTER_WINCH_MOTOR_CAN_ID, ENCODER_TICKS_TO_WORLD, FeedbackDevice.CtreMagEncoder_Relative);
			winch.enableBrakeMode(true);
			
			shotPosition = new Solenoid(RobotMap.SHOOTER_POSITION_PCM_ID);
			carriageRelease = new Solenoid(RobotMap.CARRIAGE_RELEASE_PCM_ID);
			
			carriageRetracted = new DigitalInput(RobotMap.CARRIAGE_RETRACTED_DIO_PORT_ID);
		} 
		catch (Exception e) {
			System.err.println("An error occurred in the Shooter constructor");
		}
	}
	
	public void retractWinch() {
		//Set winch to a value.
		if (!isRetracted()) {
			winch.set(0.5);
		}
	}

	public boolean isRetracted() {
		return carriageRetracted.get();
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
	
	@Override
	public void controlLoopUpdate() {
		
	}

	@Override
	public void setPeriodMs(long periodMs) {
		
	}
	
	public void updateStatus(RobotMain.OperationMode operationMode) {
		if (operationMode == RobotMain.OperationMode.TEST) {
			SmartDashboard.putNumber("Winch Pos", winch.getPosition());
		}
	}
}