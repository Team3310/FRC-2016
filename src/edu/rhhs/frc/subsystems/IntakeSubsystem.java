package edu.rhhs.frc.subsystems;

import edu.rhhs.frc.RobotMap;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

public class IntakeSubsystem extends Subsystem
{
	public static enum LiftState { UP, DOWN };
	private Solenoid outerLift, innerLift;
	private CANTalon outerRoller, innerRoller;
	
	public IntakeSubsystem() {
		this.outerLift = new Solenoid(RobotMap.INTAKE_OUTER_LIFT_MODULE_ID);
		this.innerLift = new Solenoid(RobotMap.INTAKE_INNER_LIFT_MODULE_ID);
		this.outerRoller = new CANTalon(RobotMap.INTAKE_OUTER_ROLLER_MOTOR_ID);
		this.innerRoller = new CANTalon(RobotMap.INTAKE_INNER_ROLLER_MOTOR_ID);
	}
	
	public void setSpeedOuter(double speed) {
		outerRoller.set(speed);
	}
	
	public void setSpeedInner(double speed) {
		innerRoller.set(speed);
	}
	
	public void setPositionOuter(LiftState ls) {
		//Add implementation
		if(ls == LiftState.UP) outerLift.set(true);
		else if(ls == LiftState.DOWN) outerLift.set(false);
	}
	
	public void setPositionInner(LiftState ls) {
		if(ls == LiftState.UP) innerLift.set(true);
		else if(ls == LiftState.DOWN) innerLift.set(false);
	}

	@Override
	protected void initDefaultCommand() {
		
	}
	
	public void updateStatus() {
		
	}
}