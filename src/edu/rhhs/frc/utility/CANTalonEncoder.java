package edu.rhhs.frc.utility;

import edu.wpi.first.wpilibj.CANTalon;

public class CANTalonEncoder extends CANTalon 
{
	private double encoderTicksToWorld;
	private boolean isRight = true;
	
	public CANTalonEncoder(int deviceNumber, double encoderTicksToWorld) {
		super(deviceNumber);
		this.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		this.encoderTicksToWorld = encoderTicksToWorld;
	}

	public CANTalonEncoder(int deviceNumber, double encoderTicksToWorld, boolean isRight) {
		super(deviceNumber);
		this.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		this.encoderTicksToWorld = encoderTicksToWorld;
		this.isRight = isRight;
	}

    public boolean isRight() {
		return isRight;
	}

	public void setRight(boolean isRight) {
		this.isRight = isRight;
	}

	public double convertEncoderTicksToWorld(double encoderTicks) {
    	return encoderTicks / encoderTicksToWorld;
    }

    public double convertEncoderWorldToTicks(double worldValue) {
    	return worldValue * encoderTicksToWorld;
    }
    
    public void setWorld(double worldValue) {
    	this.set(convertEncoderWorldToTicks(worldValue));
    }
    
    public void setPositionWorld(double worldValue) {
    	this.setPosition(convertEncoderWorldToTicks(worldValue));
    }
    
    public double getPositionWorld() {
    	return convertEncoderTicksToWorld(this.getPosition());
    }

}
