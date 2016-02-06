/*
 * Grayscale image representation in 1-dimension array of integer
 */
package jhalaman.image;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import jhalaman.image.tool.Cartesian;

/**
 *
 * @author Sandy Socrates <sandy_socrates@live.com>
 */
public class Citra {

    private int height;
    private int width;
    private int[] val;
    private float minimumSD = 0f;
    private float minimumSDOrientation = Cartesian.PI / 2;
    
    /**
     * Citra Constructor
     * @param height
     * @param width
     * @param val
     * @param type 
     */
    public Citra(int height, int width, int[] val) {
        this.height = height;
        this.width = width;
        this.val = new int[height * width];
        this.val = val;
    }

    public Citra(int height, int width) {
        this.height = height;
        this.width = width;
        this.val = new int[width * height];
        Arrays.fill(val, 0);
    }

    public Citra(String uri) {
        BufferedImage buffer = Utility.imageLoader(uri);
        height = buffer.getHeight();
        width = buffer.getWidth();
        int size = height * width;
        val = new int[size];
        int[] temp = new int[size];

        if (buffer.getType() != BufferedImage.TYPE_BYTE_GRAY) {
            Preprocessor.toGrayscale(buffer);
        }
        buffer.getRGB(0, 0, width, height, temp, 0, width);
        for (int i = 0; i < size; ++i) {
            int pix = Utility.G(temp[i]);
            val[i] = pix;
        }
    }

    public Citra(BufferedImage buffer) {
        height = buffer.getHeight();
        width = buffer.getWidth();
        int size = height * width;
        val = new int[size];
        int[] temp = new int[size];
        buffer.getRGB(0, 0, width, height, temp, 0, width);
        if (buffer.getType() != BufferedImage.TYPE_BYTE_GRAY) {
            for (int i = 0; i < size; ++i) {
                int pix = temp[i];
                int gray = (int) (Utility.R(pix) * 0.299 + Utility.G(pix) * 0.587 + Utility.B(pix) * 0.114);
                val[i] = gray;
            }
        } else {
            for (int i = 0; i < size; ++i) {
                int pix = Utility.G(temp[i]);
                val[i] = pix;
            }
        }
    }

    public BufferedImage toBufferedImage() {
        BufferedImage buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        int size = width * height;
        int[] out = new int[size];
        while (size-- > 0) {
            int temp = val[size];
            out[size] = Utility.toRGB(temp, temp, temp);
        }
        buffer.setRGB(0, 0, width, height, out, 0, width);
        return buffer;
    }

    public Boolean[] toBoolean() {
        int size = height * width;
        Boolean[] out = new Boolean[size];
        while (size > 0) {
            --size;
            out[size] = val[size] != 0;
        }
        return out;
    }

    public void setVal(Boolean[] in) {
        int size = height * width;
        while (size-- > 0) {
            val[size] = in[size] ? 255 : 0;
        }
    }

    /**
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * @param height the height to set
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * @param width the width to set
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * @return the val
     */
    public int[] getVal() {
        return Arrays.copyOf(val, val.length);
    }

    /**
     * @return val
     */
    public int getVal(int offset) {
        return val[offset];
    }

    public float[] getFloatVal() {
        int size = width * height;
        float[] out = new float[size];
        while (size > 0) {
            --size;
            out[size] = val[size];
        }
        return out;
    }

    /**
     * @param val the val to set
     */
    public void setVal(int[] val) {
        this.val = val;
    }

    /**
     * @param val the val to set
     */
    public void setVal(int offset, int val) {
        this.val[offset] = val;
    }

    public void setVal(float[] val) {
        int size = val.length;
        while (size > 0) {
            --size;
            this.val[size] = (int) val[size];
        }
    }

    public Citra getHistogram() {
        int[] h = new int[256];
        int size = val.length;
        while (size-- > 0) {
            ++h[val[size]];
        }

        int max = -1;
        size = 256;
        while (size-- > 0) {
            if (max < h[size]) {
                max = h[size];
            }
        }

        size = 256;
        while (size-- > 0) {
            h[size] = h[size] * 100 / max;
        }

        int[] out = new int[100 * 256];
        for (int i = 0; i < 256; ++i) {
            for (int j = 99; j >= 100 - h[i]; --j) {
                out[j * 256 + i] = 200;
            }
        }
        return new Citra(100, 256, out);
    }

    public Citra getCumulativeHistogram() {

        int[] h = new int[256];
        int size = val.length;
        while (size-- > 0) {
            ++h[val[size]];
        }

        int k = 0;
        int sum = 0;
        while (k++ < 255) {
            h[k] = h[k] + sum;
            sum = h[k];
        }

        int max = h[255];
        k = 256;
        while (k-- > 0) {
            h[k] = h[k] * 100 / max;
        }
        int[] out = new int[100 * 256];
        for (int i = 0; i < 256; ++i) {
            for (int j = 99; j >= 100 - h[i]; --j) {
                out[j * 256 + i] = 255;
            }
        }
        return new Citra(100, 256, out);
    }

    public Point getCentroid() {
        int offset = 0, mx = 0, my = 0, mass = 0;

        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                if (val[offset++] > 0) {
                    mx += j;
                    my += i;
                    ++mass;
                }
            }
        }
        if (mass == 0) {
            mass = 1;
        }
        return new Point((int) mx / mass, (int) my / mass);
    }
    
    public int countIntensity(int intensityValue) {
        if(intensityValue < 0 || intensityValue > 255)
            return 0;
        int offset = val.length;
        int total = 0;
        while(offset-- > 0)
        {
            if(val[offset] == intensityValue)
                ++total;
        }
        return total;
    }

    public float getOrientation() {
        Point centroid = getCentroid();
        int xbar = centroid.x;
        int ybar = centroid.y;

        int a = 0, b = 0, c = 0;
        int offset = 0;

        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                if (val[offset++] > 0) {
                    int xij = j - xbar;
                    int yij = i - ybar;
                    a += xij * xij;
                    b += xij * yij;
                    c += yij * yij;
                }
            }
        }
        b *= 2;
        if (a == 0) {
            return (float) Math.PI / 2;
        }
        float pi = (float) Math.PI;
        float theta = (float) (0.5 * Math.atan(b / (a - c)) + pi / 2);
        if (theta < 0) {
            theta += pi;
        }
        return theta;
    }

    public Point getTopOffset() {
        int offset = 0;
        while (offset < val.length && val[offset] == 0) {
            ++offset;
        }
        int y = offset / width;
        int x1 = offset % width;
        int x2 = offset;
        while (offset++ < y * width + width - 1 && offset < val.length) {
            if (val[offset] > 0) {
                x2 = offset;
            }
        }
        x2 %= width;
        return new Point((x1 + x2) / 2, y);
    }

    public Point getBottomOffset() {
        int offset = val.length - 1;
        while (offset > 0 && val[offset] == 0) {
            --offset;
        }
        int y = offset / width;
        int x1 = offset % width;
        int x2 = x1;
        while (--offset >= y * width) {
            if (val[offset] > 0) {
                x2 = offset;
            }
        }
        x2 %= width;
        return new Point((x1 + x2) / 2, y);
    }

    public ArrayList<Point> lineIncision(Point A, Point B) {
        ArrayList<Point> array = new ArrayList<Point>();
        int x, y, dx, dy, incx, incy, balance, size, x1 = A.x, x2 = B.x, y1 = A.y, y2 = B.y;
        size = width * height;
        dx = Math.abs(x1 - x2);
        dy = Math.abs(y1 - y2);
        incx = x2 >= x1 ? 1 : -1;
        incy = y2 >= y1 ? 1 : -1;

        x = x1;
        y = y1;

        if (dx >= dy) {
            dy <<= 1;
            balance = dy - dx;
            dx <<= 1;

            while (x != x2) {
                int offset = x + width * y;
                if (offset >= 0 && offset < size
                        && x > 1 && (x < width - 1)
                        && y > 1 && (y < height - 1)
                        && (val[offset] > 0
                        || val[offset + 1] > 0
                        || val[offset - 1] > 0
                        || val[offset - width] > 0
                        || val[offset + width] > 0
                        || val[offset + width + 1] > 0
                        || val[offset + width - 1] > 0
                        || val[offset - width + 1] > 0
                        || val[offset - width + 1] > 0)) {
                    array.add(new Point(x, y));
                }
                if (balance >= 0) {
                    y += incy;
                    balance -= dx;
                }
                balance += dy;
                x += incx;
            }
            int offset = x + width * y;
            if (offset >= 0 && offset < size
                    && x > 1 && (x < width - 1)
                    && y > 1 && (y < height - 1)
                    && (val[offset] > 0
                    || val[offset + 1] > 0
                    || val[offset - 1] > 0
                    || val[offset - width] > 0
                    || val[offset + width] > 0
                    || val[offset + width + 1] > 0
                    || val[offset + width - 1] > 0
                    || val[offset - width + 1] > 0
                    || val[offset - width + 1] > 0)) {
                array.add(new Point(x, y));
            }
        } else {
            dx <<= 1;
            balance = dx - dy;
            dy <<= 1;

            while (y != y2) {
                int offset = x + width * y;
                if (offset >= 0 && offset < size
                        && x > 1 && (x < width - 1)
                        && y > 1 && (y < height - 1)
                        && (val[offset] > 0
                        || val[offset + 1] > 0
                        || val[offset - 1] > 0
                        || val[offset - width] > 0
                        || val[offset + width] > 0
                        || val[offset + width + 1] > 0
                        || val[offset + width - 1] > 0
                        || val[offset - width + 1] > 0
                        || val[offset - width + 1] > 0)) {
                    array.add(new Point(x, y));
                }
                if (balance >= 0) {
                    x += incx;
                    balance -= dy;
                }
                balance += dx;
                y += incy;
            }
            int offset = x + width * y;
            if (offset >= 0 && offset < size
                    && x > 1 && (x < width - 1)
                    && y > 1 && (y < height - 1)
                    && (val[offset] > 0
                    || val[offset + 1] > 0
                    || val[offset - 1] > 0
                    || val[offset - width] > 0
                    || val[offset + width] > 0
                    || val[offset + width + 1] > 0
                    || val[offset + width - 1] > 0
                    || val[offset - width - 1] > 0
                    || val[offset - width + 1] > 0)) {
                array.add(new Point(x, y));
            }
        }
        return array;
    }

    public ArrayList<Point> getSymmetryLinePoints(float threshold) {
        // find angle formed by top centroid and bottom named theta
        // find bottom' (line from rotating top with -theta)
        // if vector(bottom - bottom') nearly perpendicular (under threshold) to symmetry
        // approximate base = (bottom+bottom')/2
        // approximate symmetry = vector(top - base)
        // else
        // approximate symmetry = vector(top - bottom)
        Point centroid = getCentroid();
        Point top = getTopOffset();
        Point bottom = getBottomOffset();
        Point p = null;
        Point bottom_1 = Cartesian.rotate(centroid, top, -Cartesian.angleOfABC(top, centroid, bottom));
        Point vBottom_1 = Cartesian.vectorAB(centroid, bottom_1);
        if (vBottom_1.x != 0) {
            float mBottom_1 = vBottom_1.y / vBottom_1.x;
            Point maxBottom_1 = Cartesian.findXEnd(centroid, height, mBottom_1, width, height);
            ArrayList<Point> incision = lineIncision(centroid, maxBottom_1);

            //incision found
            if (incision.size() > 0) {
                bottom_1 = Cartesian.getFurthest(incision, centroid);
                Point apxH = Cartesian.vectorAB(bottom, bottom_1);
                Point apxV = Cartesian.vectorAB(centroid, top);
                // direction doesn't matter
                // check if vector apxH and apxV nearly perpendicular
                float angle = Math.abs(Cartesian.vectorsAngle(apxH, apxV));
                angle *= Cartesian.toDegree;
                //if less than threshold
                if (Math.abs(angle - 90) < threshold && apxV.x != 0) {
                    Point max = Cartesian.findXEnd(centroid, height, apxV.y / apxV.x, width, height);
                    ArrayList<Point> H = Cartesian.plotLine(bottom, bottom_1, width, height);
                    ArrayList<Point> V = Cartesian.plotLine(top, max, width, height);
                    p = Cartesian.incise(H, V);
                } else if (apxV.x == 0) {
                    Point max = new Point(centroid.x, height - 1);
                    p = Cartesian.getFurthest(lineIncision(centroid, max), centroid);
                }
            }
        }
        if (p == null) {
            p = bottom;
        }

        Point symmetryVector = Cartesian.vectorAB(top, p);
        Point symmetryTop = new Point();
        Point symmetryBottom = new Point();
        if (symmetryVector.x == 0) {
            symmetryTop.x = top.x;
            symmetryTop.y = 0;
            symmetryBottom.x = top.x;
            symmetryBottom.y = height - 1;
        } else {
            symmetryTop = Cartesian.findXEnd(top, 0, symmetryVector.y / symmetryVector.x, width, height);
            symmetryBottom = Cartesian.findXEnd(top, height, symmetryVector.y / symmetryVector.x, width, height);
        }
        return Cartesian.plotLine(symmetryTop, symmetryBottom, width, height);
    }

    public float getSymmetryLineAngle(float threshold) {
        // find angle formed by top centroid and bottom named theta
        // find bottom' (line from rotating top with -theta )
        // if vector(bottom - bottom') nearly perpendicular (under threshold) to symmetry
        // approximate base = (bottom+bottom')/2
        // approximate symmetry = vector(top - base)
        // else
        // approximate symmetry = vector(top - bottom)
        Point centroid = getCentroid();
        Point top = getTopOffset();
        Point bottom = getBottomOffset();
        Point p = null;
        Point bottom_1 = Cartesian.rotate(centroid, top, -Cartesian.angleOfABC(top, centroid, bottom));
        Point vBottom_1 = Cartesian.vectorAB(centroid, bottom_1);
        if (vBottom_1.x != 0) {
            float mBottom_1 = vBottom_1.y / vBottom_1.x;
            Point maxBottom_1 = Cartesian.findXEnd(centroid, height, mBottom_1, width, height);
            ArrayList<Point> incision = lineIncision(centroid, maxBottom_1);

            //incision found
            if (incision.size() > 0) {
                bottom_1 = Cartesian.getFurthest(incision, centroid);
                Point apxH = Cartesian.vectorAB(bottom, bottom_1);
                Point apxV = Cartesian.vectorAB(centroid, top);
                // direction doesn't matter
                // check if vector apxH and apxv nearly perpendicular
                float angle = Math.abs(Cartesian.vectorsAngle(apxH, apxV));
                angle *= Cartesian.toDegree;
                //if less than threshold
                if (Math.abs(angle - 90) < threshold && apxV.x != 0) {
                    Point max = Cartesian.findXEnd(centroid, height, apxV.y / apxV.x, width, height);
                    ArrayList<Point> H = Cartesian.plotLine(bottom, bottom_1, width, height);
                    ArrayList<Point> V = Cartesian.plotLine(top, max, width, height);
                    p = Cartesian.incise(H, V);
                } else if (apxV.x == 0) {
                    Point max = new Point(centroid.x, height - 1);
                    p = Cartesian.getFurthest(lineIncision(centroid, max), centroid);
                }
            }
        }
        if (p == null) {
            p = bottom;
        }

        Point symmetryVector = Cartesian.vectorAB(top, p);

        return Cartesian.angle(symmetryVector);
    }

    public ArrayList<Point> getEdgel() {
        ArrayList<Point> array = new ArrayList<Point>();
        int offset = val.length;
        while (offset-- > 0) {
            if (val[offset] > 0) {
                array.add(new Point(offset % width, offset / width));
            }
        }
        return array;
    }

    public float getSymmetry(float theta, float delta) {
        Point centroid = getCentroid();
        Point top;
        Point bottom;
        float max = Float.NEGATIVE_INFINITY;
        int offset = 0;
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                if (val[offset] > 0) {
                    float x = centroid.x - j;
                    float y = centroid.y - i;
                    float distance = x * x + y * y;
                    if (max < distance) {
                        max = distance;
                    }
                }
                ++offset;
            }
        }
        max = (float) Math.sqrt(max);
        float PI_per2 = Cartesian.PI / 2;
        float x = PI_per2 + theta;
        float min = Float.POSITIVE_INFINITY;
        float temp, out = Cartesian.PI;
        while (x > PI_per2) {
            float gradient = (float)Math.tan(x);
            top = Cartesian.findYEnd(centroid, 0, gradient, width, height);
            bottom = Cartesian.findYEnd(centroid, width, gradient, width, height);
            temp = symmetryDistance(top, bottom, x - PI_per2);
            if (temp < min) {
                min = temp;
                out = x;
            }
            x -= delta;
        }

        x = Cartesian.PI / 2;
        top = new Point(centroid.x, 0);
        bottom = new Point(centroid.x, height - 1);
        temp = symmetryDistance(top, bottom, 0);
        if (temp < min) {
            min = temp;
            out = x;
        }
        x -= delta;
        while (x > PI_per2 - theta) {
            float gradient = (float)Math.tan(x);
            top = Cartesian.findYEnd(centroid, 0, gradient, width, height);
            bottom = Cartesian.findYEnd(centroid, width, gradient, width, height);
            temp = symmetryDistance(top, bottom, x - PI_per2);
            if (temp < min) {
                min = temp;
                out = x;
            }
            x -= delta;
        }

        minimumSD = min / max * 100;
        minimumSDOrientation = out;

        return out;
    }

    private float symmetryDistance(Point top, Point bottom, float theta) {
        float gradient = (float) Math.tan(theta);
        float sum = 0;
        int n = 0;
        ArrayList<Point> symLine = Cartesian.plotLine(top, bottom, width, height);
        for (int i = 0; i < symLine.size(); ++i) {
            Point a = Cartesian.findYEnd(symLine.get(i), 0, gradient, width, height);
            Point b = Cartesian.findYEnd(symLine.get(i), width, gradient, width, height);
            a = slice(symLine.get(i), a);
            b = slice(symLine.get(i), b);
            float distanceA = Cartesian.distance(symLine.get(i), a);
            float distanceB = Cartesian.distance(symLine.get(i), b);
            if (distanceA > 0 || distanceB > 0) {
                ++n;
            }
            sum += Math.abs(distanceA - distanceB);
        }
        n = n == 0 ? 1 : n;
        return sum / n;
    }

    private Point slice(Point A, Point B) {
        Point out = new Point(A.x, A.y);
        int x, y, dx, dy, incx, incy, balance, size, x1 = A.x, x2 = B.x, y1 = A.y, y2 = B.y;
        size = width * height;
        dx = Math.abs(x1 - x2);
        dy = Math.abs(y1 - y2);
        incx = x2 >= x1 ? 1 : -1;
        incy = y2 >= y1 ? 1 : -1;

        x = x1;
        y = y1;

        if (dx >= dy) {
            dy <<= 1;
            balance = dy - dx;
            dx <<= 1;

            while (x != x2) {
                int offset = x + width * y;
                if (offset >= 0 && offset < size
                        && x > 1 && (x < width - 1)
                        && y > 1 && (y < height - 1)
                        && (val[offset] > 0
                        || val[offset + 1] > 0
                        || val[offset - 1] > 0
                        || val[offset - width] > 0
                        || val[offset + width] > 0
                        || val[offset + width + 1] > 0
                        || val[offset + width - 1] > 0
                        || val[offset - width + 1] > 0
                        || val[offset - width + 1] > 0)) {
                    out = new Point(x, y);
                }
                if (balance >= 0) {
                    y += incy;
                    balance -= dx;
                }
                balance += dy;
                x += incx;
            }
            int offset = x + width * y;
            if (offset >= 0 && offset < size
                    && x > 1 && (x < width - 1)
                    && y > 1 && (y < height - 1)
                    && (val[offset] > 0
                    || val[offset + 1] > 0
                    || val[offset - 1] > 0
                    || val[offset - width] > 0
                    || val[offset + width] > 0
                    || val[offset + width + 1] > 0
                    || val[offset + width - 1] > 0
                    || val[offset - width + 1] > 0
                    || val[offset - width + 1] > 0)) {
                out = new Point(x, y);
            }
        } else {
            dx <<= 1;
            balance = dx - dy;
            dy <<= 1;

            while (y != y2) {
                int offset = x + width * y;
                if (offset >= 0 && offset < size
                        && x > 1 && (x < width - 1)
                        && y > 1 && (y < height - 1)
                        && (val[offset] > 0
                        || val[offset + 1] > 0
                        || val[offset - 1] > 0
                        || val[offset - width] > 0
                        || val[offset + width] > 0
                        || val[offset + width + 1] > 0
                        || val[offset + width - 1] > 0
                        || val[offset - width + 1] > 0
                        || val[offset - width + 1] > 0)) {
                    out = new Point(x, y);
                }
                if (balance >= 0) {
                    x += incx;
                    balance -= dy;
                }
                balance += dx;
                y += incy;
            }
            int offset = x + width * y;
            if (offset >= 0 && offset < size
                    && x > 1 && (x < width - 1)
                    && y > 1 && (y < height - 1)
                    && (val[offset] > 0
                    || val[offset + 1] > 0
                    || val[offset - 1] > 0
                    || val[offset - width] > 0
                    || val[offset + width] > 0
                    || val[offset + width + 1] > 0
                    || val[offset + width - 1] > 0
                    || val[offset - width - 1] > 0
                    || val[offset - width + 1] > 0)) {
                out = new Point(x, y);
            }
        }
        return out;
    }

    /**
     * @return the minimumSD
     */
    public float getMinimumSD() {
        return minimumSD;
    }

    /**
     * @return the minimumSDOrientation
     */
    public float getMinimumSDOrientation() {
        return minimumSDOrientation;
    }
}
