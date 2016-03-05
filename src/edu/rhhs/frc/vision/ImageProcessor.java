package edu.rhhs.frc.vision;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.ParticleFilterCriteria2;

import edu.wpi.first.wpilibj.image.BinaryImage;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.NIVisionException;
import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;

/**
 * @author rhhs
 * 
 * This class takes an image as input and applies various filters and
 * techniques to identify the targets
 */
public class ImageProcessor {
    
    private static final int NUM_SMALL_OBJECT_EROSIONS = 2;
    
    private static final int THRESHOLD_HUE_MIN = 82;
    private static final int THRESHOLD_HUE_MAX = 137;
    private static final int THRESHOLD_SATURATION_MIN = 35;
    private static final int THRESHOLD_SATURATION_MAX = 255;
    private static final int THRESHOLD_LUMINANCE_MIN = 60;
    private static final int THRESHOLD_LUMINANCE_MAX = 255;
    
    private static final int PARTICLE_SEARCH_HEIGHT_MIN = 20;
    private static final int PARTICLE_SEARCH_HEIGHT_MAX = 400;
    private static final int PARTICLE_SEARCH_WIDTH_MIN = 20;
    private static final int PARTICLE_SEARCH_WIDTH_MAX = 400;
    
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
    
    public static final double CAMERA_FOV_HORIZONTAL_ANGLE = 48.0;  // M206 54 deg actual
    public static final double CAMERA_FOV_VERTICAL_ANGLE = 36.9;  // M206 41.8 deg actual
        
    public ImageProcessor() {
    	
    }

    public TargetInfo findBestTarget(ColorImage cameraImage, String outputFilename) { 
    	ParticleAnalysisReport[] reports = identifyRectangularTargets(cameraImage, outputFilename);
    	
    	ParticleAnalysisReport bestTarget = null;
    	double bestCompositeScore = 10000;
        for (ParticleAnalysisReport currentReport: reports) { 
        	double currentCompositeScore = getCompositeScore(currentReport);
            if (currentCompositeScore < bestCompositeScore) {
            	bestCompositeScore = currentCompositeScore;
            	bestTarget = currentReport;
            }
        }
        
        return new TargetInfo(bestTarget, bestCompositeScore);
    }
 
    
    private ParticleAnalysisReport[] identifyRectangularTargets(ColorImage originalImage, String outputFilename) {
        if (originalImage == null) {
            return null;
        }

        BinaryImage thresholdImage = null;
        BinaryImage bigObjectsImage = null;
        ParticleAnalysisReport[] reports = null;
        try {
            thresholdImage = originalImage.thresholdHSL(THRESHOLD_HUE_MIN, THRESHOLD_HUE_MAX, THRESHOLD_SATURATION_MIN, THRESHOLD_SATURATION_MAX, THRESHOLD_LUMINANCE_MIN, THRESHOLD_LUMINANCE_MAX);
            if (thresholdImage == null) {
                return null;
            }
            
            bigObjectsImage = thresholdImage.removeSmallObjects(true, NUM_SMALL_OBJECT_EROSIONS);
            if (bigObjectsImage == null) {
                return null;
            }
            
            // Set up the particle criteria
            ParticleFilterCriteria2[] particleSearchCriteriaCollection = new ParticleFilterCriteria2[2];
            particleSearchCriteriaCollection[0] = new ParticleFilterCriteria2(NIVision.MeasurementType.MT_BOUNDING_RECT_WIDTH, PARTICLE_SEARCH_WIDTH_MIN, PARTICLE_SEARCH_WIDTH_MAX, 0, 0);
            particleSearchCriteriaCollection[1] = new ParticleFilterCriteria2(NIVision.MeasurementType.MT_BOUNDING_RECT_HEIGHT, PARTICLE_SEARCH_HEIGHT_MIN, PARTICLE_SEARCH_HEIGHT_MAX, 0, 0);
            bigObjectsImage.particleFilter(particleSearchCriteriaCollection);

            // Get the particle reports
            reports = bigObjectsImage.getOrderedParticleAnalysisReports();
           
            if (outputFilename != null) {
            	bigObjectsImage.write(outputFilename);
                System.out.println("Processed Image Saved = " + outputFilename + ", size = " + bigObjectsImage.getHeight() + "X" + bigObjectsImage.getWidth());
            }
        } 
        catch (NIVisionException e) {
            System.err.println("NIVision error = " + e.getMessage());            
        } 
        finally {
            try {
                if (thresholdImage != null) {
                    thresholdImage.free();
                }
                if (bigObjectsImage != null) {
                    bigObjectsImage.free();
                }
            }
            catch (NIVisionException e) {
                System.err.println("Error freeing image = " + e.getMessage());            
            }
        }

        return reports;
    }
    
    private double getRectangleScore(ParticleAnalysisReport report) {
        return (report.particleArea / ((double)report.boundingRectWidth * (double)report.boundingRectHeight) - OPTIMAL_RECT) * WEIGHT_FACTOR_RECT;
    } 
    
    private double getXXRatioScore(ParticleAnalysisReport report) {
        return (report.center_mass_x_normalized - OPTIMAL_XX) * WEIGHT_FACTOR_XX;
    }
    
    private double getYYRatioScore(ParticleAnalysisReport report) {
        return (report.center_mass_x_normalized - OPTIMAL_YY) * WEIGHT_FACTOR_YY;
    }
    
    private double getAspectRatioScore(ParticleAnalysisReport report) {
        return (report.boundingRectHeight / (double)report.boundingRectWidth - OPTIMAL_AR) * WEIGHT_FACTOR_AR;
    }
    
    private double getCompositeScore(ParticleAnalysisReport report) {
        return Math.abs(getRectangleScore(report)) + Math.abs(getXXRatioScore(report)) + Math.abs(getYYRatioScore(report)) + Math.abs(getAspectRatioScore(report));
    }
    
    public class TargetInfo {
    	public double distanceToTargetFt;
    	public double angleToTargetDeg;
    	public double compositeScore;
    	
    	public TargetInfo(ParticleAnalysisReport report, double compositeScore) {
    		this.compositeScore = compositeScore;
    		updateCalculations(report);
    	}
    	
        private void updateCalculations(ParticleAnalysisReport report) {
            double tanHalfFOV = Math.tan(Math.toRadians(CAMERA_FOV_HORIZONTAL_ANGLE / 2));

            // Account for targets off center
            double focalDistancePixels = (double)report.imageWidth / 2 / tanHalfFOV;
            double targetCenterPixels = (double)report.boundingRectLeft + (double)report.boundingRectWidth / 2;
            double targetOffsetPixels =  targetCenterPixels - (double)report.imageWidth / 2;
            double offsetAngle = Math.atan(targetOffsetPixels / focalDistancePixels);

            // Calculate the distance from the camera focal point to the target
            double imageWidthFt = Math.cos(offsetAngle) * (double)report.imageWidth * TARGET_WIDTH_FT / (double)report.boundingRectWidth;
            distanceToTargetFt = imageWidthFt / 2.0 / tanHalfFOV;

            // Calculate the angle from the center of the image to the selected target
            double targetOffsetFt = imageWidthFt * targetOffsetPixels / (double)report.imageWidth;
            angleToTargetDeg = Math.atan2(targetOffsetFt, distanceToTargetFt) * 180.0 / Math.PI;
        }   
    }

}
