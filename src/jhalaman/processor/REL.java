/*
 * Total Area per Total Erosion feature
 */
package jhalaman.processor;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import jhalaman.image.Citra;
import jhalaman.image.filters.Morphology;

/**
 * REL stands for Rasio Erosi - Luas or Total Area per Total Erosion Ratio
 * This will return number of erosion needed to reduce the binary image of the leaf
 * until nothing is left
 * 
 * @author Sandy Socrates <sandy_socrates@live.com>
 */
public class REL extends FeatureExtractor{
    
    private Morphology m = new Morphology();
    
    public REL() {
        super(0f);
        this.name = "Rasio Erosi-Luas";
    }
    
    @Override
    public float extract(BufferedImage original, Citra grayscale, Citra binary, Citra edge, Citra skeleton, Point centroid, float orientation, Point top, Point bottom, ArrayList<Point> symmetryLinePoints, float symmetryAngle) {
        int totalErosion =  m.getTotalErosion(binary);
        int size = binary.countIntensity(255);
        return (float)totalErosion/size*100;
    }
}
