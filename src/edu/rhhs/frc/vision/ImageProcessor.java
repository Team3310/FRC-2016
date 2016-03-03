package edu.rhhs.frc.vision;

import com.ni.vision.NIVision;
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
    
    public static ParticleAnalysisReport[] identifyRectangularTargets(ColorImage originalImage, String outputFilename) {
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
            System.out.println("NIVision error = " + e.getMessage());            
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
                System.out.println("Error freeing image = " + e.getMessage());            
            }
        }

        return reports;
    }

}
