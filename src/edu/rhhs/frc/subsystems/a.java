package edu.rhhs.frc.subsystems;

public class a
{
	/*
	 * public void controlLoopUpdate() {
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
	 */
}