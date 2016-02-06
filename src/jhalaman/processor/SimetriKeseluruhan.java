/*
 * Whole Balance represented with Symmetry Distance
 */
package jhalaman.processor;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import jhalaman.image.Citra;
import jhalaman.image.tool.Cartesian;

/**
 * Simetri Keseluruhan or Whole Balance represent whether the leaf edge is symmetric or not
 * This will return scale 0-1, 0 means the edge is symmetric
 * 
 * @author Sandy Socrates <sandy_socrates@live.com>
 */
public class SimetriKeseluruhan extends FeatureExtractor {

    public SimetriKeseluruhan() {
        super(0f);
        this.name = "Simetri Keseluruhan";
    }
    public static final float SYMMETRYCAL = 0f;

    
    @Override
    public float extract(BufferedImage original, Citra grayscale, Citra binary, Citra edge, Citra skeleton, Point centroid, float orientation, Point top, Point bottom, ArrayList<Point> symmetryLinePoints, float symmetryAngle) {
        float out = 0f;
        float max = Float.NEGATIVE_INFINITY;
        float gradient = (float) Math.tan(symmetryAngle + Cartesian.PI / 2);
        int sum = 0;
        for (int i = 0; i < symmetryLinePoints.size(); ++i) {
            Point a = Cartesian.findYEnd(symmetryLinePoints.get(i), 0, gradient, edge.getWidth(), edge.getHeight());
            Point b = Cartesian.findYEnd(symmetryLinePoints.get(i), edge.getWidth(), gradient, edge.getWidth(), edge.getHeight());
            ArrayList<Point> incisionA = edge.lineIncision(symmetryLinePoints.get(i), a);
            ArrayList<Point> incisionB = edge.lineIncision(symmetryLinePoints.get(i), b);

            float distanceA = 0;
            float distanceB = 0;
            if (incisionA.size() > 0) {
                Point A = Cartesian.getFurthest(incisionA, symmetryLinePoints.get(i));
                if (A != null) {
                    distanceA = Cartesian.distance(A, symmetryLinePoints.get(i));
                    plotList.add(A);
                }
            }
            if (incisionB.size() > 0) {
                Point B = Cartesian.getFurthest(incisionB, symmetryLinePoints.get(i));
                if (B != null) {
                    distanceB = Cartesian.distance(B, symmetryLinePoints.get(i));
                    plotList.add(B);
                }
            }

            if (distanceA > 0 || distanceB > 0) {
                ++sum;
            }
            if (max < distanceA) {
                max = distanceA;
            }
            if (max < distanceB) {
                max = distanceB;
            }
            out += Math.abs(distanceA - distanceB);
        }
        if (sum == 0) {
            return SYMMETRYCAL;
        }
        return (float) (out / sum / max * 100);
    }
}
