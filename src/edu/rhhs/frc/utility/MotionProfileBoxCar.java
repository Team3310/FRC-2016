package edu.rhhs.frc.utility;

public class MotionProfileBoxCar {
	
	static double DEFAULT_T1 = 200;	// millisecond
	static double DEFAULT_T2 = 100; // millisecond
	
	private double targetDistance;  // any distance unit
	private double maxVelocity;		// velocity unit consistent with targetDistance
	private double itp;	  			// millisecond
	private double t1 = DEFAULT_T1;
	private double t2 = DEFAULT_T2;
	
	private double t4;
	private int numFilter1Boxes;
	private int numFilter2Boxes;
	private int numPoints;
	
	private int numITP;
	private double filter1;
	private double filter2;
	private double previousPosition;
	private double previousVelocity;
	private double deltaFilter1;
	private double[] filter2Window;
	private int windowIndex;
	private int pointIndex;

	public MotionProfileBoxCar(double targetDistance, double maxVelocity, double itp) {
		this.targetDistance = targetDistance;
		this.maxVelocity = maxVelocity;
		this.itp = itp;
		
		initializeProfile();
	} 
	
	private void initializeProfile() {
		t4 = targetDistance/maxVelocity * 1000;
		numFilter1Boxes = (int)Math.ceil(t1/itp);
		numFilter2Boxes = (int)Math.ceil(t2/itp);
		numPoints = (int)(t4/itp);

		numITP = numPoints + numFilter1Boxes + numFilter2Boxes;
		filter1 = 0;
		filter2 = 0;
		previousVelocity = 0;
		deltaFilter1 = 1.0/numFilter1Boxes;
		filter2Window = new double[numFilter2Boxes];
		windowIndex = 0;
		pointIndex = 0;
	}
	
	public MotionProfilePoint getNextPoint(MotionProfilePoint point) {
		if (point == null) {
			point = new MotionProfilePoint();
		}
		
		if (pointIndex == 0) {
			point.initialize();
			pointIndex++;
			return point;
		}
		else if (pointIndex > numITP) {
			return null;
		}
		
		int input = (pointIndex - 1) < numPoints ? 1 : 0;		
		if (input > 0) {
			filter1 = Math.min(1, filter1 + deltaFilter1);
		}
		else {
			filter1 = Math.max(0, filter1 - deltaFilter1);				
		}
		
		double firstFilter1InWindow = filter2Window[windowIndex];
		if (pointIndex < numFilter2Boxes) {
			firstFilter1InWindow = 0;
		}
		filter2Window[windowIndex] = filter1;
		
		filter2 += (filter1 - firstFilter1InWindow) / numFilter2Boxes;
		
		point.time = pointIndex * itp / 1000.0;
		point.velocity = filter2 * maxVelocity;
		point.position = previousPosition + (point.velocity + previousVelocity) /  2 * itp / 1000;
		point.acceleration = (point.velocity - previousVelocity) / itp * 1000;
					
		previousVelocity = point.velocity;
		previousPosition = point.position;
		windowIndex++;
		if (windowIndex == numFilter2Boxes) {
			windowIndex = 0;
		}	
		
		pointIndex++;
		
		return point;
	}

	public double getTargetDistance() {
		return targetDistance;
	}

	public double getMaxVelocity() {
		return maxVelocity;
	}

	public double getItp() {
		return itp;
	}

	public double getT1() {
		return t1;
	}

	public void setT1(double t1) {
		this.t1 = t1;
	}

	public double getT2() {
		return t2;
	}

	public void setT2(double t2) {
		this.t2 = t2;
	}

	public static void main(String[] args) {
		long startTime = System.nanoTime();
		
		MotionProfileBoxCar mp = new MotionProfileBoxCar(4, 10, 10);
//		System.out.println("Time, Position, Velocity, Acceleration");
		MotionProfilePoint point = new MotionProfilePoint();
		while(mp.getNextPoint(point) != null) {
//			System.out.println(point.time + ", " + point.position + ", " + point.velocity + ", " + point.acceleration);
		}
		
		long deltaTime = System.nanoTime() - startTime;
		System.out.println("Time Box Car = " + (double)deltaTime * 1E-6 + " ms");
	}

}
