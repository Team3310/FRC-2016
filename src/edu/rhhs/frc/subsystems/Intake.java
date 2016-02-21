package edu.rhhs.frc.subsystems;

import edu.rhhs.frc.RobotMap;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Intake extends Subsystem
{
	public static enum LiftState { UP, DOWN };
	private Solenoid outerLift, innerLift;
	private CANTalon outerRoller, innerRoller;
	
	public Intake() {
		try {
			outerLift = new Solenoid(RobotMap.INTAKE_OUTER_LIFT_MODULE_ID);
			innerLift = new Solenoid(RobotMap.INTAKE_INNER_LIFT_MODULE_ID);
			outerRoller = new CANTalon(RobotMap.INTAKE_OUTER_ROLLER_MOTOR_ID);
			innerRoller = new CANTalon(RobotMap.INTAKE_INNER_ROLLER_MOTOR_ID);
		} 
		catch (Exception e) {
			System.out.println("test");
		}
	}
	
	public void setSpeedOuter(double speed) {
		outerRoller.set(-speed);
	}
	
	public void setSpeedInner(double speed) {
		innerRoller.set(speed);
	}
	
	public void setPositionOuter(LiftState state) {
		//Add implementation
		if(state == LiftState.UP) {
			outerLift.set(false);
		}
		else if(state == LiftState.DOWN) {
			outerLift.set(true);
		}
	}
	
	public void setPositionInner(LiftState state) {
		if(state == LiftState.UP) {
			innerLift.set(true);
		}
		else if(state == LiftState.DOWN) {
			innerLift.set(false);
		}
	}

	@Override
	protected void initDefaultCommand() {
		
	}
	
	public void updateStatus() {
		
	}
}