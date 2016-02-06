/*
 * Length per Width Ratio feature
 */
package jhalaman.processor;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import jhalaman.image.Citra;
import jhalaman.image.tool.Cartesian;

/**
 * RLP stands for Rasio Lebar-Panjang or Length per Width Ratio
 * This will return the ratio of leaf's height and width
 * 
 * @author Sandy Socrates <sandy_socrates@live.com>
 */
public class RLP extends FeatureExtractor{
    public RLP() {
        super(0);
        this.name = "Rasio Lebar-Panjang";
    }
    
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
            float length = Cartesian.distance(symmetryLinePoints.get(symTop), symmetryLinePoints.get(symBottom));
            float maxWidth = 0f;
            int inc = symTop > symBottom ? -1 : 1;
            for (int i = symTop; i != symBottom; i += inc) {
                Point a = Cartesian.findYEnd(symmetryLinePoints.get(i), 0, gradient, edge.getWidth(), edge.getHeight());
                Point b = Cartesian.findYEnd(symmetryLinePoints.get(i), edge.getWidth(), gradient, edge.getWidth(), edge.getHeight());
                ArrayList<Point> incisionA = edge.lineIncision(symmetryLinePoints.get(i), a);
                ArrayList<Point> incisionB = edge.lineIncision(symmetryLinePoints.get(i), b);

                Point A = symmetryLinePoints.get(i);
                Point B = symmetryLinePoints.get(i);
                if (incisionA.size() > 0) {
                    A = Cartesian.getFurthest(incisionA, symmetryLinePoints.get(i));
                }
                if (incisionB.size() > 0) {
                    B = Cartesian.getFurthest(incisionB, symmetryLinePoints.get(i));
                }
                
                float width = Cartesian.distance(A, B);
                if(maxWidth < width)
                    maxWidth = width;
            }
            if(length != 0)
                return maxWidth/length*100;
            else
                return 0f;
        }        
    }
}
