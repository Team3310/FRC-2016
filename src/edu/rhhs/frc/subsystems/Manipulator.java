package edu.rhhs.frc.subsystems;

import edu.rhhs.frc.RobotMap;
import edu.rhhs.frc.utility.CANTalonEncoder;
import edu.rhhs.frc.utility.ControlLoopable;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Manipulator extends Subsystem implements ControlLoopable
{
	private static final double ENCODER_TICKS_TO_WORLD = 4000; //MAJOR TODO
	private CANTalonEncoder leftArm, rightArm;
	
	public Manipulator() {
		leftArm = new CANTalonEncoder(RobotMap.MANIPULATOR_LEFT_MOTOR_ID, ENCODER_TICKS_TO_WORLD, false);
		rightArm = new CANTalonEncoder(RobotMap.MANIPULATOR_RIGHT_MOTOR_ID, ENCODER_TICKS_TO_WORLD, true);
	}
	
	public void setPosition(double position) {
		//Add implementation
//		this.setMPTarget(position, 0); //MAJOR TODO: Velocity
	}
	
	public void setArmSpeed(double speed) {
		leftArm.set(-speed);
		rightArm.set(speed);
	}

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