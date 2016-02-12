package edu.rhhs.frc.subsystems;

import edu.rhhs.frc.RobotMap;
import edu.wpi.first.wpilibj.CANTalon;

public class ManipulatorSubsystem extends MPSubsystem
{
	private CANTalon leftArm, rightArm;
	
	public ManipulatorSubsystem() {
		super(10);
		this.leftArm = new CANTalon(RobotMap.MANIPULATOR_LEFT_MOTOR_ID);
		this.rightArm = new CANTalon(RobotMap.MANIPULATOR_RIGHT_MOTOR_ID);
	}
	
	public void setPosition(double position) {
		//add implementation
	}

	@Override
	protected void initDefaultCommand() {
		
	}
}