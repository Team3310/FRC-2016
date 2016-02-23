package edu.rhhs.frc.utility;

import java.util.ArrayList;

import edu.rhhs.frc.RobotMain;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;

public class MotionProfileController
{	
	protected ArrayList<CANTalonEncoder> motorControllers;	
	protected long periodMs;
	protected PIDParams pidParams;	
	protected MotionProfileBoxCar mp;
	protected MotionProfilePoint mpPoint;
	protected boolean useGyroLock;
	protected double startGyroAngle;
	
	public MotionProfileController(long periodMs, PIDParams pidParams, ArrayList<CANTalonEncoder> motorControllers) 
	{
		this.motorControllers = motorControllers;
		this.periodMs = periodMs;
		setPID(pidParams);
	}
    
	public void setPID(PIDParams pidParams) {
		this.pidParams = pidParams;
		
		for (CANTalonEncoder motorController : motorControllers) {
			motorController.setPID(pidParams.kP, pidParams.kI, pidParams.kD);
		}
	}
	
	public void setMPTarget(double targetValue, double maxVelocity) {
		setMPTarget(targetValue, maxVelocity, false, 0);
	}

	public void setMPTarget(double targetValue, double maxVelocity, boolean useGyroLock, double desiredAngle) {
		// Set up the motion profile 
		mp = new MotionProfileBoxCar(targetValue, maxVelocity, periodMs);
		for (CANTalonEncoder motorController : motorControllers) {
			motorController.setPosition(0);
			motorController.set(0);
			motorController.changeControlMode(TalonControlMode.Position);
		}
	}
	
	public void setZeroPosition() {
		
	}

	public boolean controlLoopUpdate() {
		return controlLoopUpdate(0);
	}
	
	public boolean controlLoopUpdate(double currentGyroAngle) {
		mpPoint = mp.getNextPoint(mpPoint);
		
		// Check if we are finished
		if (mpPoint == null) {
			return true;
		}
		
		// Calculate the motion profile feed forward and gyro feedback terms
		double KfLeft = 0.0;
		double KfRight = 0.0;
		double gyroDelta = useGyroLock ? currentGyroAngle - startGyroAngle : 0;
		if (Math.abs(mpPoint.position) > 0.001) {
			KfLeft = (pidParams.kA * mpPoint.acceleration + pidParams.kV * mpPoint.velocity + pidParams.kG * gyroDelta) / mpPoint.position;
			KfRight = (pidParams.kA * mpPoint.acceleration + pidParams.kV * mpPoint.velocity - pidParams.kG * gyroDelta) / mpPoint.position;
		}

		// Update the controllers Kf and set point.
		for (CANTalonEncoder motorController : motorControllers) {
			if (motorController.isRight()) {
				motorController.setF(KfRight);
			}
			else {
				motorController.setF(KfLeft);
			}
			motorController.setWorld(mpPoint.position);
		}
		
		return false;
	}
}