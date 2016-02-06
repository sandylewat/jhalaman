/*
 * Base Balance represented with Symmetry Distance
 */
package jhalaman.processor;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import jhalaman.image.Citra;
import jhalaman.image.tool.Cartesian;

/**
 * Simetri Pangkal or Base Balance represent whether the leaf base edge is symmetric or not
 * This will return scale 0-1, 0 means the edge is symmetric
 * Please note that the leaf image is under assumption that the base will be below
 * 
 * @author Sandy Socrates <sandy_socrates@live.com>
 */
public class SimetriPangkal extends FeatureExtractor {

    public SimetriPangkal() {
        super(0f);
        this.name = "Simetri Pangkal";
    }
    public static final float SYMMETRYCAL = 0f;
    
    /**
     * Extract feature from citra
     *
     * @param citra
     */
    @Override
    public float extract(BufferedImage original, Citra grayscale, Citra binary, Citra edge, Citra skeleton, Point centroid, float orientation, Point top, Point bottom, ArrayList<Point> symmetryLinePoints, float symmetryAngle) {
        float gradient = (float) Math.tan(symmetryAngle + Cartesian.PI / 2);

        int symTop = -1;
        int symBottom = -1;

        int it = 0;
        Boolean found = false;
        while (it < symmetryLinePoints.size() && !found) {
            Point a = Cartesian.findYEnd(symmetryLinePoints.get(it), 0, gradient, edge.getWidth(), edge.getHeight());
            Point b = Cartesian.findYEnd(symmetryLinePoints.get(it), edge.getWidth(), gradient, edge.getWidth(), edge.getHeight());
            ArrayList<Point> incisionA = edge.lineIncision(symmetryLinePoints.get(it), a);
            ArrayList<Point> incisionB = edge.lineIncision(symmetryLinePoints.get(it), b);

            if (incisionA.size() > 0 || incisionB.size() > 0) {
                symTop = it;
                found = true;
            }
            ++it;
        }
        found = false;
        it = symmetryLinePoints.size() - 1;

        while (it >= 0 && !found) {
            Point a = Cartesian.findYEnd(symmetryLinePoints.get(it), 0, gradient, edge.getWidth(), edge.getHeight());
            Point b = Cartesian.findYEnd(symmetryLinePoints.get(it), edge.getWidth(), gradient, edge.getWidth(), edge.getHeight());
            ArrayList<Point> incisionA = edge.lineIncision(symmetryLinePoints.get(it), a);
            ArrayList<Point> incisionB = edge.lineIncision(symmetryLinePoints.get(it), b);

            if (incisionA.size() > 0 || incisionB.size() > 0) {
                symBottom = it;
                found = true;
            }
            --it;
        }

        if (symTop < 0 || symBottom < 0) {
            return 0;
        } else {

            if (symmetryLinePoints.get(symTop).y > symmetryLinePoints.get(symBottom).y) {
                int temp = symBottom;
                symBottom = symTop;
                symTop = temp;
            }
            int inc = symTop > symBottom ? -1 : 1;
            float out = 0f;
            float max = Float.NEGATIVE_INFINITY;
            int sum = 0;
            float length = Cartesian.vectorLength(Cartesian.vectorAB(symmetryLinePoints.get(symBottom), symmetryLinePoints.get(symTop)));
            it = symTop;
            while (it < symmetryLinePoints.size() && it >= 0 && Cartesian.distance(symmetryLinePoints.get(symTop), symmetryLinePoints.get(it)) < 0.75 * length) {
                it += inc;
            }
            int baseTop = it;


            for (int i = baseTop; i != symBottom; i += inc) {
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
}
