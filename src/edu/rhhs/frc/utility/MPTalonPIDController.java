package edu.rhhs.frc.utility;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;

public class MPTalonPIDController
{	
	public static enum MPControlMode { STRAIGHT, TURN };
	public static enum MPTurnType { TANK, LEFT_SIDE_ONLY, RIGHT_SIDE_ONLY };

	protected ArrayList<CANTalonEncoder> motorControllers;	
	protected long periodMs;
	protected PIDParams pidParams;	
	protected MotionProfileBoxCar mp;
	protected MotionProfilePoint mpPoint;
	protected boolean useGyroLock;
	protected double startGyroAngle;
	protected MPControlMode controlMode;
	protected MPTurnType turnType;
	
	public MPTalonPIDController(long periodMs, PIDParams pidParams, ArrayList<CANTalonEncoder> motorControllers) 
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
	
	public void setMPTarget(double startValue, double targetValue, double maxVelocity) {
		setMPStraightTarget(startValue, targetValue, maxVelocity, false, 0, false);
	}

	public void setMPTarget(double startValue, double targetValue, double maxVelocity, boolean resetEncoder) {
		setMPStraightTarget(startValue, targetValue, maxVelocity, false, 0, resetEncoder);
	}

	public void setMPStraightTarget(double startValue, double targetValue, double maxVelocity, boolean useGyroLock, double desiredAngle, boolean resetEncoder) {
		controlMode = MPControlMode.STRAIGHT;
		this.startGyroAngle = desiredAngle;
		this.useGyroLock = useGyroLock;
		
		// Set up the motion profile 
		mp = new MotionProfileBoxCar(startValue, targetValue, maxVelocity, periodMs);
		for (CANTalonEncoder motorController : motorControllers) {
			if (resetEncoder) {
				motorController.setPosition(0);
			}
			motorController.set(startValue);
			motorController.changeControlMode(TalonControlMode.Position);
		}
	}
	
	public void setMPTurnTarget(double startAngleDeg, double targetAngleDeg, double maxTurnRateDegPerSec, MPTurnType turnType, double trackWidth) {
		controlMode = MPControlMode.TURN;
		this.turnType = turnType;
		this.startGyroAngle = startAngleDeg;
		this.useGyroLock = true;
		
		double trackDistance = calcTrackDistance(targetAngleDeg - startAngleDeg, turnType, trackWidth);

		// Set up the motion profile 
		mp = new MotionProfileBoxCar(0, trackDistance, maxTurnRateDegPerSec, periodMs);
		for (CANTalonEncoder motorController : motorControllers) {
			motorController.setPosition(0);
			motorController.set(0);
			motorController.changeControlMode(TalonControlMode.Position);
		}
	}
	
	private double calcTrackDistance(double deltaAngleDeg, MPTurnType turnType, double trackWidth) {
		double trackDistance = deltaAngleDeg / 360.0 * Math.PI * trackWidth;
		if (turnType == MPTurnType.TANK) {
			return trackDistance;
		}
		else if (turnType == MPTurnType.LEFT_SIDE_ONLY) {
			return trackDistance * 2.0;
		}
		else if (turnType == MPTurnType.RIGHT_SIDE_ONLY) {
			return -trackDistance * 2.0;
		}
		return 0.0;
	}
	
	public void setZeroPosition() {
		for (CANTalonEncoder motorController : motorControllers) {
			motorController.setPosition(0);
			motorController.set(0);
			motorController.changeControlMode(TalonControlMode.Position);
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
		
		// Calculate the motion profile feed forward and gyro feedback terms
		double KfLeft = 0.0;
		double KfRight = 0.0;
		double gyroDelta = useGyroLock ? currentGyroAngle - startGyroAngle : 0;

		if (Math.abs(mpPoint.position) > 0.001) {
			KfLeft = (pidParams.kA * mpPoint.acceleration + pidParams.kV * mpPoint.velocity - pidParams.kG * gyroDelta) / mpPoint.position;
			KfRight = (pidParams.kA * mpPoint.acceleration + pidParams.kV * mpPoint.velocity + pidParams.kG * gyroDelta) / mpPoint.position;
		}
		
		// Update the set points and Kf gains
		if (controlMode == MPControlMode.STRAIGHT) {
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
		}
		
		else {
			for (CANTalonEncoder motorController : motorControllers) {
				if (turnType == MPTurnType.TANK) {
					if (motorController.isRight()) {
						motorController.setF(KfRight);
						motorController.setWorld(-mpPoint.position);
					}
					else {
						motorController.setF(KfLeft);
						motorController.setWorld(mpPoint.position);
					}
				}
				else if (turnType == MPTurnType.LEFT_SIDE_ONLY) {
					if (!motorController.isRight()) {
						motorController.setF(KfLeft);
						motorController.setWorld(mpPoint.position);
					}
				}
				else if (turnType == MPTurnType.RIGHT_SIDE_ONLY) {
					if (motorController.isRight()) {
						motorController.setF(KfRight);
						motorController.setWorld(mpPoint.position);
					}
				}
			}
		}
		
		return false;
	}
	
	public MotionProfilePoint getCurrentPoint() {
		return mpPoint;
	}
}