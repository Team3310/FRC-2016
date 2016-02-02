package edu.rhhs.frc.subsystems;

import edu.rhhs.frc.utility.CANTalonEncoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class MPSingleMotorTest extends MPSubsystem
{
	private CANTalonEncoder motor1;
	
	private static final long loopPeriodMs = 10;
	private static final double encoderTicksPerDegree = 13.4215;
	
	public MPSingleMotorTest() {
		super(loopPeriodMs);
		motor1 = new CANTalonEncoder(5, encoderTicksPerDegree);
		motor1.reverseOutput(true);
		addMotorController(motor1);

		setPID(10, 0, 0, 0, 0);
	}
    	
    public void initDefaultCommand() {
    }
    
    public void updateStatus() {
    	SmartDashboard.putNumber("Real Top Pos - Motor 5", motor1.getPositionWorld());
    }
}