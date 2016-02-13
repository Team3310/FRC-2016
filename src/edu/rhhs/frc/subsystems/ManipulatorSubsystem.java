package edu.rhhs.frc.subsystems;

import edu.rhhs.frc.RobotMap;
import edu.rhhs.frc.utility.CANTalonEncoder;

public class ManipulatorSubsystem extends MPSubsystem
{
	private static final long LOOP_PERIOD_MS = 10;
	private static final double ENCODER_TICKS_TO_WORLD = 0; //MAJOR TODO
	private CANTalonEncoder leftArm, rightArm;
	
	public ManipulatorSubsystem() {
		super(LOOP_PERIOD_MS);
		this.leftArm = new CANTalonEncoder(RobotMap.MANIPULATOR_LEFT_MOTOR_ID, ENCODER_TICKS_TO_WORLD, false);
		addMotorController(leftArm);
		this.rightArm = new CANTalonEncoder(RobotMap.MANIPULATOR_RIGHT_MOTOR_ID, ENCODER_TICKS_TO_WORLD, true);
		addMotorController(rightArm);
	}
	
	public void setPosition(double position) {
		//Add implementation
		this.setMPTarget(position, 0); //MAJOR TODO: Velocity
	}

	@Override
	protected void initDefaultCommand() {
		
	}
	
	public void updateStatus() {
		
	}
}