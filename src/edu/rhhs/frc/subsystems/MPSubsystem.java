package edu.rhhs.frc.subsystems;

import java.util.ArrayList;

import edu.rhhs.frc.utility.CANTalonEncoder;
import edu.rhhs.frc.utility.ControlLoopable;
import edu.rhhs.frc.utility.ControlLooper;
import edu.rhhs.frc.utility.MotionProfileBoxCar;
import edu.rhhs.frc.utility.MotionProfilePoint;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.command.Subsystem;

public abstract class MPSubsystem extends Subsystem implements ControlLoopable {
	
	private ArrayList<CANTalonEncoder> motorControllers;
	
	private ControlLooper m_controlLoop;
	private long periodMs;
	
	private double Ka = 0.0;
	private double Kv = 0.0;
	
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
	
	// Must call this after all the controllers have been added
	public void setPID(double Kp, double Ki, double Kd, double Ka, double Kv) {
		this.Ka = Ka;
		this.Kv = Kv;
		
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
		// set talon control mode based on encoder position or gyro mode
		
		mp = new MotionProfileBoxCar(targetValue, maxVelocity, periodMs);
		for (CANTalonEncoder motorController : motorControllers) {
			motorController.setPosition(0);
			motorController.set(0);
			motorController.changeControlMode(TalonControlMode.Position);
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
			double Kf = 0.0;
			if (Math.abs(mpPoint.position) > 0.001) {
				Kf = (Ka * mpPoint.acceleration + Kv * mpPoint.velocity) / mpPoint.position;
			}
			
			// Talon PID MODE for encoders
		
			for (CANTalonEncoder motorController : motorControllers) {
				motorController.setF(Kf);
				motorController.setWorld(mpPoint.position);
			}
			
			// Gyro software MODE
			// 0) set the control mode
			// 1) switch Talons to vbus (already done above)
			// 2) KpGyro, KdGyro, KiGyro, KfGyro, KaGyro, KvGyro
			// 3) PID Calculate
			
			double error = setpoint - NavxGyroYawAngle;
			
			double KfGyro = Ka * mpPoint.acceleration + Kv * mpPoint.velocity ;
			
			double output = KpGyro*error + KiGyro*totalerror + KdGyro*(error-previouserror) + KfGyro;
			for (CANTalonEncoder motorController : motorControllers) {
				if (motorController.isRight) {
					motorController.set(output);
				}
				else {
					motorController.set(-output);
				}
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
	
	public double getMPPosition() {
		return currentPosition;
	}
	public double getMPVelocity() {
		return currentVelocity;
	}
}