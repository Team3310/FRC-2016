package edu.rhhs.frc.utility;

public class MotionProfilePoint {
	public double time;
	public double position;
	public double velocity;
	public double acceleration;
	
	public void initialize() {
		time = 0;
		position = 0;
		velocity = 0;
		acceleration = 0;
	}
}
