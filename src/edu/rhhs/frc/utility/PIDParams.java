package edu.rhhs.frc.utility;

public class PIDParams 
{
    public double kP = 0;
    public double kI = 0;
    public double kD = 0;
    public double kF = 0; 
    public double kA = 0;
    public double kV = 0;
    public double kG = 0;
    public int iZone = 0;
    public double rampRatePID = 0;

    public PIDParams() {	
    }
    
    public PIDParams(double kP, double kI, double kD) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
    }
    
    public PIDParams(double kP, double kI, double kD, double kA, double kV) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        this.kA = kA;
        this.kV = kV;
    }
    
    public PIDParams(double kP, double kI, double kD, double kA, double kV, double kG) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        this.kA = kA;
        this.kV = kV;
        this.kG = kG;
    }
}