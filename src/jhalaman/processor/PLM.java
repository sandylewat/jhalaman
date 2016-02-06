/*
 * Max Width position feature
 */
package jhalaman.processor;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import jhalaman.image.Citra;
import jhalaman.image.tool.Cartesian;

/**
 * PLM stands for Panjang Lebar Maksimal or Max Width
 * This will return position of the maximum width relative to its horizontal symmetry
 * 
 * Returned value will be top, middle or bottom
 * @author Sandy Socrates <sandy_socrates@live.com>
 */
public class PLM extends FeatureExtractor {

    private float topThreshold = 1 / 3f;
    private float midThreshold = 2 / 3f;
    private float topL = 100/3f;
    private float mid = 200/3f;
    private float bot = 100f;

    public PLM(float midAreaThreshold) {
        super(midAreaThreshold);
        if (threshold > 1 || threshold < 0) {
            threshold = 1 / 3f;
        }
        this.name = "Posisi Lebar Maksimal";
        topThreshold = (1 - threshold) / 2;
        midThreshold = topThreshold + threshold;
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
            if (symmetryLinePoints.get(symTop).y > symmetryLinePoints.get(symBottom).y) {
                int temp = symTop;
                symTop = symBottom;
                symBottom = temp;
            }

            float maxWidth = 0f;
            int maxWidthPos = 0;
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
                    maxWidthPos = i;
                }
            }
            if (maxWidth > 0) {
                float length = Cartesian.distance(symmetryLinePoints.get(symTop), symmetryLinePoints.get(symBottom));
                float pos = Cartesian.distance(symmetryLinePoints.get(symTop), symmetryLinePoints.get(maxWidthPos));

                float ratio = Math.abs(length - pos) / length;
                if (ratio > midThreshold) {
                    return bot; //bottom
                } else if (ratio > topThreshold) {
                    return mid; //mid
                } else {
                    return topL; //top
                }
            } else {
                return 0f;
            }
        }
    }
}
