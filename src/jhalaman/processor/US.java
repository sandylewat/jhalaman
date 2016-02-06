/*
 * Number of pixel connected to N pixel
 */
package jhalaman.processor;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import jhalaman.image.Citra;

/**
 * US stands for Ujung Skeleton or Nodes of Skeleton
 * This will return number of pixel in leaf skeleton image only connected to N (threshold) other pixel
 * 
 * @author Sandy Socrates <sandy_socrates@live.com>
 */
public class US extends FeatureExtractor{
    public US(float connectivity) {
        super(connectivity);
        this.name = "Ujung Skeleton "+ (int) connectivity;
    }
    
    @Override
    public float extract(BufferedImage original, Citra grayscale, Citra binary, Citra edge, Citra skeleton, Point centroid, float orientation, Point top, Point bottom, ArrayList<Point> symmetryLinePoints, float symmetryAngle) {
        Boolean[] in = skeleton.toBoolean();
        int size = in.length;
        int width = skeleton.getWidth();
        int i = size - width - 2;
        int sum = 0;
        int thr = (int) threshold;
        while (i-- > 2 * width) {
            if (i % width > 1 && i % width < width - 2) {
                if (in[i]) {
                    int count = 0;
                    if(in[i - width - 1]) 
                        ++count;
                    if(in[i - width]) 
                        ++count;
                    
                    if (in[i - width + 1])
                        ++count;
                    
                    if (in[i - 1])
                        ++count;
                    if (in[i + 1])
                        ++count;
                    if (in[i + width - 1])
                        ++count;
                    if (in[i + width])
                        ++count;
                    if (in[i + width + 1])
                        ++count;

                    if (count == threshold) {
                        ++sum;
                    }
                }
            }
        }
        return (float) sum;
    }
    
}
