package edu.rhhs.frc.subsystems;

import edu.rhhs.frc.RobotMap;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

public class PneumaticShooter extends Subsystem
{
	public static enum ModuleState { EXTENDED, RETRACTED };
	private Solenoid shooter1, shooter2;
	
	public PneumaticShooter() {
		this.shooter1 = new Solenoid(RobotMap.SHOOTER1_EXTEND_MODULE_ID);
		this.shooter2 = new Solenoid(RobotMap.SHOOTER2_EXTEND_MODULE_ID);
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
		shooter1.set(v);
		shooter2.set(v);
	}

	@Override
	protected void initDefaultCommand() {
		
	}
}