package edu.rhhs.frc.utility;

import java.util.ArrayList;

import edu.rhhs.frc.utility.MPSoftwarePIDController.MPSoftwareTurnType;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;

public class SoftwarePIDController
{	
	protected ArrayList<CANTalonEncoder> motorControllers;	
	protected PIDParams pidParams;	
	protected boolean useGyroLock;
	protected double targetGyroAngle;
	protected MPSoftwareTurnType turnType;
	
	protected double minTurnOutput = 0.002;
	protected double maxError;
	protected double maxPrevError;
	protected double prevError = 0.0; // the prior error (used to compute velocity)
	protected double totalError = 0.0; // the sum of the errors for use in the integral calc
	
	public SoftwarePIDController( PIDParams pidParams, ArrayList<CANTalonEncoder> motorControllers) 
	{
		this.motorControllers = motorControllers;
		setPID(pidParams);
	}
    
	public void setPID(PIDParams pidParams) {
		this.pidParams = pidParams;
	}
	
	public void setPIDTurnTarget(double targetAngleDeg, double maxError, double maxPrevError, MPSoftwareTurnType turnType) {
		this.turnType = turnType;
		this.targetGyroAngle = targetAngleDeg;
		this.useGyroLock = true;
		this.maxError = maxError;
		this.maxPrevError = maxPrevError;
		
		for (CANTalonEncoder motorController : motorControllers) {
			motorController.changeControlMode(TalonControlMode.PercentVbus);
		}
		
		prevError = 0.0;
		totalError = 0.0;
	}
	
	public boolean controlLoopUpdate() {
		return controlLoopUpdate(0);
	}
	
	public boolean controlLoopUpdate(double currentGyroAngle) {
		// Calculate the motion profile feed forward and error feedback terms
		double error = targetGyroAngle - currentGyroAngle;
		double deltaLastError = (error - prevError);
		
		// Check if we are finished
		if (Math.abs(error) < maxError && Math.abs(deltaLastError) < maxPrevError) {
			return true;
		}
		
		if (Math.abs(targetGyroAngle - currentGyroAngle) < pidParams.iZone) {
			totalError += error;
		}
		else {
			totalError = 0;
		}
		
		double output =  pidParams.kP * error + pidParams.kI * totalError + pidParams.kD * deltaLastError;
		double turnBoost = output < 0 ? -minTurnOutput : minTurnOutput;
		output += turnBoost;
		
		prevError = error;
			
		// Update the controllers set point.
		if (turnType == MPSoftwareTurnType.TANK) {
			for (CANTalonEncoder motorController : motorControllers) {
				motorController.set(-output);
			}
		}
		else if (turnType == MPSoftwareTurnType.LEFT_SIDE_ONLY) {
			for (CANTalonEncoder motorController : motorControllers) {
				if (motorController.isRight()) {
					motorController.set(0);
				}
				else {
					motorController.set(-2.0 * output);					
				}
			}
		}
		else if (turnType == MPSoftwareTurnType.RIGHT_SIDE_ONLY) {
			for (CANTalonEncoder motorController : motorControllers) {
				if (motorController.isRight()) {
					motorController.set(-2.0 * output);
				}
				else {
					motorController.set(0);					
				}
			}
		}

		return false;
	}	
}