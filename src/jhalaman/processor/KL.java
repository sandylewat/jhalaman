/*
 * Width uniformity feature
 */
package jhalaman.processor;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import jhalaman.image.Citra;
import jhalaman.image.tool.Cartesian;

/**
 * KL stands for Keseragaman Lebar or Width Uniformity
 * 
 * @author Sandy Socrates <sandy_socrates@live.com>
 */
public class KL extends FeatureExtractor {

    public KL(float errorThreshold) {
        super(errorThreshold);
        this.name = "Keseragaman lebar";
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
            float maxWidth = 0f;
            int inc = symTop > symBottom ? -1 : 1;
            ArrayList<Float> widthList = new ArrayList();
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
                widthList.add(width);
                if (maxWidth < width) {
                    maxWidth = width;
                }
            }
            if (maxWidth != 0 && widthList.size() > 0) {
                int uniformWidthCounter = 0;
                float uniformThreshold = threshold*maxWidth;

                for (int i = 0; i<widthList.size(); ++i) {
                    float delta = Math.abs(widthList.get(i)-maxWidth);
                    
                    if(delta < uniformThreshold)
                    {
                        ++uniformWidthCounter;
                    }
                }
                
                return (float)uniformWidthCounter/widthList.size()*100;
            } else {
                return 0f;
            }
        }
    }
}
