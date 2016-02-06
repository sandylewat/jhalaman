/*
 * Total Area per Ridge Area feature
 */
package jhalaman.processor;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import jhalaman.image.Citra;
import jhalaman.image.Preprocessor;
import jhalaman.image.filters.Hilditch;
import jhalaman.image.filters.RidgeFilter;

/**
 * RBL stands for Rasio Bukit - Luas or Ridge per Total Area Ratio
 * This will try to find the ridge of the leaf and return the ratio of it to total area
 * 
 * @author Sandy Socrates <sandy_socrates@live.com>
 */
public class RBL extends FeatureExtractor{
    
    private RidgeFilter rf = new RidgeFilter();
    private Hilditch h = new Hilditch();
    
    public RBL(float threshold) {
        super(threshold);
        this.name = "Rasio Bukit-Luas";
    }
    
    @Override
    public float extract(BufferedImage original, Citra grayscale, Citra binary, Citra edge, Citra skeleton, Point centroid, float orientation, Point top, Point bottom, ArrayList<Point> symmetryLinePoints, float symmetryAngle) {
        Citra temp = new Citra(original);
        rf.detect(temp,threshold);
        h.skeletonize(temp, Preprocessor.getOtsuThreshold(temp), 255, 0);
        int size = binary.countIntensity(255);
        int ridge = temp.countIntensity(255);
        return ridge == 0 ? 0f : (float)(ridge/(float)size)*100;
    }
}
