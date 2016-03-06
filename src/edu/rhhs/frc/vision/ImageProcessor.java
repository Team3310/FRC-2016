package edu.rhhs.frc.vision;

import java.text.DecimalFormat;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.DrawMode;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.ImageType;
import com.ni.vision.NIVision.RGBValue;
import com.ni.vision.NIVision.ShapeMode;

/**
 * @author rhhs
 * 
 * This class takes an image as input and applies various filters and
 * techniques to identify the targets
 */
public class ImageProcessor {
    
    private static final int NUM_SMALL_OBJECT_EROSIONS = 1;
    
    private NIVision.Range TARGET_HUE_RANGE = new NIVision.Range(82, 137);	
    private NIVision.Range TARGET_SAT_RANGE = new NIVision.Range(35, 255);	
    private NIVision.Range TARGET_LUM_RANGE = new NIVision.Range(60, 255);	
    
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
    
    public static final double CAMERA_FOV_HORIZONTAL_ANGLE = 33.565;  
    public static final double CAMERA_FOV_VERTICAL_ANGLE = 59.695;  
	public static final double tanHalfFOV = Math.tan(Math.toRadians(CAMERA_FOV_HORIZONTAL_ANGLE / 2));
        
    private Image processedImage;

    public ImageProcessor() {
        processedImage = NIVision.imaqCreateImage(ImageType.IMAGE_U8, 0);
    }

    public TargetInfo findBestTarget(Image cameraImage) { 
        if (cameraImage == null) {
            return null;
        }

    	NIVision.imaqColorThreshold(processedImage, cameraImage, 1, NIVision.ColorMode.HSL, TARGET_HUE_RANGE, TARGET_SAT_RANGE, TARGET_LUM_RANGE);
        if (processedImage == null) {
            return null;
        }
        
        NIVision.imaqSizeFilter(processedImage, processedImage, 1, NUM_SMALL_OBJECT_EROSIONS, NIVision.SizeType.KEEP_LARGE, null);
        if (processedImage == null) {
            return null;
        }
        
        // The measurements are flipped because the image is rotated 90 degrees
    	int bestTargetIndex = -1;
    	double bestCompositeScore = 0;
		int numParticles = NIVision.imaqCountParticles(processedImage, 1);

    	for (int particleIndex = 0; particleIndex < numParticles; particleIndex++) {     
			double rectWidth = NIVision.imaqMeasureParticle(processedImage, particleIndex, 0, NIVision.MeasurementType.MT_BOUNDING_RECT_HEIGHT);
			double rectHeight = NIVision.imaqMeasureParticle(processedImage, particleIndex, 0, NIVision.MeasurementType.MT_BOUNDING_RECT_WIDTH);
			double rectArea = NIVision.imaqMeasureParticle(processedImage, particleIndex, 0, NIVision.MeasurementType.MT_AREA);
			double rectMomentYY = NIVision.imaqMeasureParticle(processedImage, particleIndex, 0, NIVision.MeasurementType.MT_NORM_MOMENT_OF_INERTIA_XX);
			double rectMomentXX = NIVision.imaqMeasureParticle(processedImage, particleIndex, 0, NIVision.MeasurementType.MT_NORM_MOMENT_OF_INERTIA_YY);

			double currentCompositeScore = getCompositeScore(rectArea, rectWidth, rectHeight, rectMomentXX, rectMomentYY);
            if (bestTargetIndex == -1 || currentCompositeScore < bestCompositeScore) {
            	bestCompositeScore = currentCompositeScore;
            	bestTargetIndex = particleIndex;
            }
		}
        
		double rectTop = NIVision.imaqMeasureParticle(processedImage, bestTargetIndex, 0, NIVision.MeasurementType.MT_BOUNDING_RECT_LEFT);
		double rectLeft = NIVision.imaqMeasureParticle(processedImage, bestTargetIndex, 0, NIVision.MeasurementType.MT_BOUNDING_RECT_TOP);
		double rectWidth = NIVision.imaqMeasureParticle(processedImage, bestTargetIndex, 0, NIVision.MeasurementType.MT_BOUNDING_RECT_HEIGHT);
		double rectHeight = NIVision.imaqMeasureParticle(processedImage, bestTargetIndex, 0, NIVision.MeasurementType.MT_BOUNDING_RECT_WIDTH);
		NIVision.GetImageSizeResult imageSize;
		imageSize = NIVision.imaqGetImageSize(processedImage);
		int imageHeight = imageSize.width;
		int imageWidth = imageSize.height;
		
		// Calculate distance and angle to target
        int widthOffsetPixels = (int)((rectLeft + rectWidth / 2) - imageWidth / 2);
               
    	// Account for targets off center
        double focalDistancePixels = (double)imageWidth / 2 / tanHalfFOV;
        double offsetAngle = Math.atan((double)widthOffsetPixels / focalDistancePixels);

        double imageWidthFt = Math.cos(offsetAngle) * (double)imageWidth * TARGET_WIDTH_FT / rectWidth;
        double cameraDistanceWidthFt = imageWidthFt / 2.0 / tanHalfFOV;

        // Calculate the angle from the center of the image to the selected target
        double targetOffsetFt = imageWidthFt * widthOffsetPixels / (double)imageWidth;
        double angleToTargetDeg = Math.atan2(targetOffsetFt, cameraDistanceWidthFt) * 180.0 / Math.PI;

        NIVision.Rect rect = new NIVision.Rect((int)rectLeft, (int)rectTop, (int)rectWidth, (int)rectHeight);
        NIVision.imaqDrawShapeOnImage(cameraImage, cameraImage, rect, DrawMode.DRAW_VALUE, ShapeMode.SHAPE_RECT, 255.0f);
        
        int targetYCoord = (int)(widthOffsetPixels + rectLeft + rectWidth / 2);
        NIVision.Point startPoint = new NIVision.Point(0, targetYCoord);
        NIVision.Point endPoint = new NIVision.Point(imageSize.width, targetYCoord);
        NIVision.imaqDrawLineOnImage(cameraImage, cameraImage, DrawMode.DRAW_VALUE, startPoint, endPoint, 255.0f);
        
        NIVision.OverlayTextOptions options = new NIVision.OverlayTextOptions();
        NIVision.RGBValue textColor = new RGBValue(0, 255, 0, 0);        
        NIVision.Point textPoint = new NIVision.Point((int)(rectTop + rectHeight/2), (int)targetYCoord);        
        DecimalFormat numFormat = new DecimalFormat("##.##");
        NIVision.imaqOverlayText(cameraImage, textPoint, "A = " + numFormat.format(angleToTargetDeg), textColor, options, "");
        
        return new TargetInfo(cameraDistanceWidthFt, angleToTargetDeg, bestCompositeScore);
    }
        
    private double getRectangleScore(double area, double width, double height) {
        return (area / (width * height) - OPTIMAL_RECT) * WEIGHT_FACTOR_RECT;
    } 
    
    private double getXXRatioScore(double momentXX) {
        return (momentXX - OPTIMAL_XX) * WEIGHT_FACTOR_XX;
    }
    
    private double getYYRatioScore(double momentYY) {
        return (momentYY - OPTIMAL_YY) * WEIGHT_FACTOR_YY;
    }
    
    private double getAspectRatioScore(double width, double height) {
        return (height / width - OPTIMAL_AR) * WEIGHT_FACTOR_AR;
    }
    
    private double getCompositeScore(double area, double width, double height, double momentXX, double momentYY) {
        return Math.abs(getRectangleScore(area, width, height)) + Math.abs(getXXRatioScore(momentXX)) + Math.abs(getYYRatioScore(momentYY)) + Math.abs(getAspectRatioScore(width, height));
    }
    
    public class TargetInfo {
    	public double distanceToTargetFt;
    	public double angleToTargetDeg;
    	public double compositeScore;
    	
    	public TargetInfo(double distanceToTargetFt, double angleToTargetDeg, double compositeScore) {
    		this.distanceToTargetFt = distanceToTargetFt;
    		this.angleToTargetDeg = angleToTargetDeg;
    		this.compositeScore = compositeScore;
    	}
    }
}
