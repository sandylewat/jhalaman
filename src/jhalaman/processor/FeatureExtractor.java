/*
 * Feature extractor template class
 */
package jhalaman.processor;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import jhalaman.image.Citra;

/**
 *
 * @author Sandy Socrates <sandy_socrates@live.com>
 */
public class FeatureExtractor {
    protected String name = "Feature Extractor";
    protected String attributeName = "featureExtractor";
    protected String dataType = "NUMERIC";
    
    protected ArrayList<Point> plotList;
    protected int plotColor = 0xffff0000;
    protected float threshold = 0f;
    
    public FeatureExtractor(float threshold) {
        this.attributeName = this.getClass().getSimpleName();
        this.threshold = threshold;
        this.plotList = new ArrayList<Point>();
    }
    
    
    public String NUMERIC = "NUMERIC";
    public String INTEGER = "INTEGER";
    public String REAL = "REAL";
    public String STRING = "STRING";
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    
    public String getAttributeName() {
        return attributeName;
    }
    
    public String getDataType() {
        return dataType;
    }
    
    /**
     * Extract feature from citra
     * @param original
     * @param grayscale
     * @param binary
     * @param edge
     * @param skeleton
     * @param centroid
     * @param orientation
     * @param top
     * @param bottom
     * @param symmetryLinePoints
     * @param symmetryAngle
     * @return 
     */
    public float extract(BufferedImage original, Citra grayscale, Citra binary, Citra edge, Citra skeleton, Point centroid, float orientation, Point top, Point bottom, ArrayList<Point> symmetryLinePoints, float symmetryAngle) {
        float out = 0f;
        return out;
    }
    
    public ArrayList<Point> getPlotList() {
        return plotList;
    }
    
    public int getPlotColor() {
        return plotColor;
    }
    
    public void setPlotColor(int plotColor) {
        this.plotColor = plotColor;
    }

    /**
     * @return the threshold
     */
    public float getThreshold() {
        return threshold;
    }

    /**
     * @param threshold the threshold to set
     */
    public void setThreshold(float threshold) {
        this.threshold = threshold;
    }
    
}
