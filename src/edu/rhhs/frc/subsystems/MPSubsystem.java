package edu.rhhs.frc.subsystems;

import java.util.ArrayList;
import edu.rhhs.frc.RobotMain;
import edu.rhhs.frc.utility.CANTalonEncoder;
import edu.rhhs.frc.utility.ControlLoopable;
import edu.rhhs.frc.utility.ControlLooper;
import edu.rhhs.frc.utility.MotionProfileBoxCar;
import edu.rhhs.frc.utility.MotionProfilePoint;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.command.Subsystem;

public abstract class MPSubsystem extends Subsystem implements ControlLoopable
{	
	private ArrayList<CANTalonEncoder> motorControllers;
	
	private ControlLooper m_controlLoop;
	private long periodMs;
	
	private double Ka = 0.0;
	private double Kv = 0.0;
	private double Kg = 0.0;
	
	private MotionProfileBoxCar mp;
	private MotionProfilePoint mpPoint;
	
	private double currentPosition;
	private double lastPosition;
	private double lastTime;
	private double currentVelocity;

	
	public MPSubsystem(long periodMs) {
		motorControllers = new ArrayList<CANTalonEncoder>();

		this.periodMs = periodMs;
		
		m_controlLoop = new ControlLooper(this, periodMs);
		m_controlLoop.start();
	}
    
	public void addMotorController(CANTalonEncoder motorController) {
		motorControllers.add(motorController);	
	}
	
	public void addMotorController(CANTalonEncoder motorController, Boolean isRight) {
		motorControllers.add(motorController);	
		motorController.setRight(isRight);	
	}
	
	// Must call this after all the controllers have been added
	public void setPID(double Kp, double Ki, double Kd, double Ka, double Kv, double Kg) {
		this.Ka = Ka;
		this.Kv = Kv;
		this.Kg = Kg;
		
		for (CANTalonEncoder motorController : motorControllers) {
			motorController.setPID(Kp, Ki, Kd);
		}
	}
	
	public synchronized void enableControlLoop() {
		m_controlLoop.enable();
	}

	public synchronized void disableControlLoop() {
		m_controlLoop.disable();
	}

	public synchronized boolean isControlLoopEnabled() {
		return m_controlLoop.isEnabled();
	}
	
	public void setMPTarget(double targetValue, double maxVelocity) {
		if (isControlLoopEnabled()) {
			disableControlLoop();
		}
		
		// Set up the motion profile 
		mp = new MotionProfileBoxCar(targetValue, maxVelocity, periodMs);
		for (CANTalonEncoder motorController : motorControllers) {
			motorController.setPosition(0);
			motorController.set(0);
			motorController.changeControlMode(TalonControlMode.Position);
			RobotMain.driveTrain.setYawAngleZero();
		}
		enableControlLoop();
	}

    public void resetEncoder() {
		for (CANTalonEncoder motorController : motorControllers) {
			motorController.setPosition(0);
		}
    }
    
	public void controlLoopUpdate() {
		if (!isControlLoopEnabled()) return;
		mpPoint = mp.getNextPoint(mpPoint);
		if (mpPoint != null) {
			currentPosition = mpPoint.position;
			currentVelocity = 1000 * (currentPosition - lastPosition) / (System.currentTimeMillis() - lastTime);
			double KfLeft = 0.0;
			double KfRight = 0.0;
			double gyroAngleDeg = RobotMain.driveTrain.getYawAngleDeg();
			if (Math.abs(mpPoint.position) > 0.001) {
				KfLeft = (Ka * mpPoint.acceleration + Kv * mpPoint.velocity + Kg * gyroAngleDeg) / mpPoint.position;
				KfRight = (Ka * mpPoint.acceleration + Kv * mpPoint.velocity - Kg * gyroAngleDeg) / mpPoint.position;
			}
			for (CANTalonEncoder motorController : motorControllers) {
				if (motorController.isRight()) {
					motorController.setF(KfRight);
				}
				else {
					motorController.setF(KfLeft);
				}
				motorController.setWorld(mpPoint.position);
			}
			lastTime = System.currentTimeMillis();
			lastPosition = currentPosition;
		}
		else {
			disableControlLoop();
			for (CANTalonEncoder motorController : motorControllers) {
				motorController.changeControlMode(TalonControlMode.PercentVbus);
			}
			return;
		}
	}
	
	public double getMPTarget() {
		return mp.getTargetDistance();
	}

	public double getMPPosition() {
		return currentPosition;
	}

	public double getMPVelocity() {
		return currentVelocity;
	}
}