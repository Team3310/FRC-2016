package edu.rhhs.frc.utility;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;

public class MPSoftwarePIDController
{	
	public static enum MPTurnType { TANK, LEFT_SIDE_ONLY, RIGHT_SIDE_ONLY };

	protected ArrayList<CANTalonEncoder> motorControllers;	
	protected long periodMs;
	protected PIDParams pidParams;	
	protected MotionProfileBoxCar mp;
	protected MotionProfilePoint mpPoint;
	protected boolean useGyroLock;
	protected double startGyroAngle;
	protected double targetGyroAngle;
	protected MPTurnType turnType;
	
	public MPSoftwarePIDController(long periodMs, PIDParams pidParams, ArrayList<CANTalonEncoder> motorControllers) 
	{
		this.motorControllers = motorControllers;
		this.periodMs = periodMs;
		setPID(pidParams);
	}
    
	public void setPID(PIDParams pidParams) {
		this.pidParams = pidParams;
	}
	
	public void setMPTurnTarget(double startAngleDeg, double targetAngleDeg, double maxTurnRateDegPerSec, double t1, double t2, MPTurnType turnType, double trackWidth) {
		this.turnType = turnType;
		this.startGyroAngle = startAngleDeg;
		this.targetGyroAngle = targetAngleDeg;
		this.useGyroLock = true;
		
		// Set up the motion profile 
		mp = new MotionProfileBoxCar(startAngleDeg, targetAngleDeg, maxTurnRateDegPerSec, periodMs, t1, t2);
		for (CANTalonEncoder motorController : motorControllers) {
			motorController.changeControlMode(TalonControlMode.PercentVbus);
		}
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
		
		// Update the set points and Kf gains
		double error = mpPoint.position - currentGyroAngle;

		// Calculate the motion profile feed forward and gyro feedback terms
		double output =  pidParams.kP * error + pidParams.kA * mpPoint.acceleration + pidParams.kV * mpPoint.velocity;
		
		// Update the controllers Kf and set point.
		for (CANTalonEncoder motorController : motorControllers) {
			motorController.set(-output);
		}
		
		return false;
	}
	
	public MotionProfilePoint getCurrentPoint() {
		return mpPoint;
	}
}