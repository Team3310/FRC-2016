package edu.rhhs.frc.vision;

import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;

/**
 * @author rhhs
 * 
 * This is a helper class to describe the details of a target.  Based on the camera
 * field of view (FOV), image width in pixels, and a known target size in ft, it
 * calculates the distance from the camera focal point to the target and the angle
 * from the center of image to the selected target.  It is set up to use the target
 * width but could also calculate the distance based on the target/image height.
 * 
 */
public class Target {
    
    public static final double TARGET_HEIGHT_FT = 1.0;
    public static final double TARGET_WIDTH_FT = 20.0/12.0;
    public static final double TARGET_ASPECT_RATIO = TARGET_HEIGHT_FT / TARGET_WIDTH_FT;
            
    public static final double OPTIMAL_RECT = 0.2;
    public static final double OPTIMAL_AR = TARGET_ASPECT_RATIO;
    public static final double OPTIMAL_XX = 0.9;
    public static final double OPTIMAL_YY = 0.11;
    public static final double WEIGHT_FACTOR_RECT = 1;
    public static final double WEIGHT_FACTOR_AR = 1;
    public static final double WEIGHT_FACTOR_XX = 10;
    public static final double WEIGHT_FACTOR_YY = 10;
    
    private static final double VALID_COMPOSITE_SCORE_MAX = 10;

    private double m_cameraFOVHorizontalAngleDeg;  // M206 54 deg actual
    private double m_cameraDistanceFt;
    private double m_horizontalAngleToSelectedTargetDeg;
    
    private boolean m_calculationsPerformed;

    private ParticleAnalysisReport m_report;
    
    public Target(ParticleAnalysisReport report, double cameraFOVHorizontalAngleDeg) {
        this.m_cameraFOVHorizontalAngleDeg = cameraFOVHorizontalAngleDeg;       
        this.m_report = report;
        this.m_calculationsPerformed = false;
    }
    
    public ParticleAnalysisReport getReport() {
        return m_report;
    }
    
    public double getRectangleScore() {
        return (m_report.particleArea / ((double)m_report.boundingRectWidth * (double)m_report.boundingRectHeight) - OPTIMAL_RECT) * WEIGHT_FACTOR_RECT;
    } 
    
    public double getXXRatioScore() {
        return (m_report.center_mass_x_normalized - OPTIMAL_XX) * WEIGHT_FACTOR_XX;
    }
    
    public double getYYRatioScore() {
        return (m_report.center_mass_x_normalized - OPTIMAL_YY) * WEIGHT_FACTOR_YY;
    }
    
    public double getAspectRatioScore() {
        return (m_report.boundingRectHeight / (double)m_report.boundingRectWidth - OPTIMAL_AR) * WEIGHT_FACTOR_AR;
    }
    
    public double getCompositeScore() {
        return Math.abs(getRectangleScore()) + Math.abs(getXXRatioScore()) + Math.abs(getYYRatioScore()) + Math.abs(getAspectRatioScore());
    }
    
    public boolean isValid() {
        return (getCompositeScore() < VALID_COMPOSITE_SCORE_MAX);
    }
    
    public double getCameraDistanceFt() {
        updateCalculations();
        return m_cameraDistanceFt;
    }
    
    public double getHorizontalAngleToSelectedTargetDeg() {
        updateCalculations();
        return m_horizontalAngleToSelectedTargetDeg;
    }
    
    private void updateCalculations() {
        if (m_calculationsPerformed == true) {
            return;
        }
        double tanHalfFOV = Math.tan(Math.toRadians(m_cameraFOVHorizontalAngleDeg / 2));

        // Account for targets off center
        double focalDistancePixels = (double)m_report.imageWidth / 2 / tanHalfFOV;
        double targetCenterPixels = (double)m_report.boundingRectLeft + (double)m_report.boundingRectWidth / 2;
        double targetOffsetPixels =  targetCenterPixels - (double)m_report.imageWidth / 2;
        double offsetAngle = Math.atan(targetOffsetPixels / focalDistancePixels);

        // Calculate the distance from the camera focal point to the target
        double imageWidthFt = Math.cos(offsetAngle) * (double)m_report.imageWidth * TARGET_WIDTH_FT / (double)m_report.boundingRectWidth;
        m_cameraDistanceFt = imageWidthFt / 2.0 / tanHalfFOV;

        // Calculate the angle from the center of the image to the selected target
        double targetOffsetFt = imageWidthFt * targetOffsetPixels / (double)m_report.imageWidth;
        m_horizontalAngleToSelectedTargetDeg = Math.atan2(targetOffsetFt, m_cameraDistanceFt) * 180.0 / Math.PI;
        
        m_calculationsPerformed = true;
    }   
}
