package jhalaman.image.tool;

import java.awt.Point;
import java.util.ArrayList;

/**
 * Cartesian function
 * @author Sandy Socrates <sandy_socrates@live.com>
 */
public final class Cartesian {
    
    // Supress default counstructor
    private Cartesian() {
        throw new AssertionError();
    }
    /**
     * Pi in float
     */
    public final static float PI = (float) Math.PI;
    /**
     * Constant to change degree to radian
     */
    public final static float toRadian = (float) Math.PI /180;
    /**
     * Constant to change radian to degree
     */
    public final static float toDegree = 180 / (float) Math.PI;
    
    /**
     * Calculate angle of ABC
     * @param A 
     * @param B 
     * @param C 
     * @return angle made by line from Point A to Point B and Point C
     */
    public static float angleOfABC(Point A, Point B, Point C) {
        return vectorsAngle(new Point(A.x-B.x, A.y-B.y), new Point(C.x-B.x, C.y-B.y));
    }
    
    /**
     * Calculate angle made by two vector
     * @param vectorA
     * @param vectorB
     * @return angle made by the two vector
     */
    public static float vectorsAngle(Point vectorA, Point vectorB) {
        float angle  = (float) Math.acos(
                dotProduct(vectorA, vectorB)
                /(vectorLength(vectorA)*vectorLength(vectorB)));
        if (vectorA.x*vectorB.y < vectorA.y*vectorB.x)
            angle = -angle;
        return angle;
    }
    
    /**
     * Calculate dot product of two vector
     * @param vectorA
     * @param vectorB
     * @return 
     */
    public static int dotProduct(Point vectorA, Point vectorB) {
        return vectorA.x*vectorB.x + vectorA.y*vectorB.y;
    }
    
    /**
     * Calculate length of a vector
     * @param vector
     * @return 
     */
    public static float vectorLength(Point vector) {
        return (float) Math.sqrt(vector.x*vector.x + vector.y*vector.y);
    }
    
    /**
     * Rotate a vector by theta (in radian)
     * @param vector
     * @param theta radian
     * @return new vector
     */
    public static Point rotate(Point vector, float theta) {
        float cos = (float) Math.cos(theta);
        float sin = (float) Math.sin(theta);
        return new Point((int)(cos*vector.x-sin*vector.y), (int) (sin*vector.x+cos*vector.y));
    }
    
    /**
     * Rotate Point A by theta (in radian) relative to Point Axis
     * @param axis
     * @param a
     * @param theta
     * @return new location of A
     */
    public static Point rotate(Point axis, Point a, float theta) {
        Point vector = new Point(a.x-axis.x, a.y-axis.y);
        vector = rotate(vector, theta);
        vector.x += axis.x;
        vector.y += axis.y;
        return vector;
    }
    
    /**
     * Find maximal point to plot given maximal Y value and window
     * @param start starting point
     * @param yEnd maximal Y
     * @param gradient line gradient
     * @param width window width
     * @param height window height
     * @return max point
     */
    public static Point findXEnd(Point start, int yEnd, float gradient, int width, int height) {
        float c = start.y - gradient*start.x;
        yEnd = yEnd >= height ? height-1 : yEnd < 0 ? 0 : yEnd;
        int xEnd = (int) ((yEnd-c)/gradient);

        if(xEnd >= width) {
            xEnd = width-1;
            yEnd = (int) (gradient*xEnd+c);
        }
        else if(xEnd < 0) {
            xEnd = 0;
            yEnd = (int) (gradient*xEnd+c);
        }
        return new Point(xEnd, yEnd);
    }
    
    /**
     * Find maximal point to plot given maximal x value and window
     * @param start starting point
     * @param xEnd maximal x
     * @param gradient line gradient
     * @param width window width
     * @param height window height
     * @return max point
     */
    public static Point findYEnd(Point start, int xEnd, float gradient, int width, int height) {
        float c = start.y - gradient*start.x;
        xEnd = xEnd >= width ? width-1 : xEnd < 0 ? 0 : xEnd;
        int yEnd = (int) (gradient*xEnd + c);

        if(yEnd >= height) {
            yEnd = height-1;
            xEnd = (int) ((yEnd-c)/gradient);
        }
        else if(yEnd < 0) {
            yEnd = 0;
            xEnd = (int)((yEnd-c)/gradient);
        }
        return new Point(xEnd, yEnd);
    }
    
    /**
     * Calculate angle of a vector
     * @param vector
     * @return angle in radian
     */
    public static float angle(Point vector) {
        return (float) Math.atan2(vector.y, vector.x);
    }
    
    /**
     * Calculate vector AB from point A and B
     * @param A
     * @param B
     * @return vector AB
     */
    public static Point vectorAB(Point A, Point B) {
        return new Point(B.x-A.x, B.y - A.y);
    }
    
    /**
     * Calculate distance between two point
     * @param A
     * @param B
     * @return 
     */
    public static float distance(Point A, Point B) {
        int x = B.x - A.x;
        int y = B.y - A.y;
        return (float) Math.sqrt(x*x+y*y);
    }
    
    /**
     * get nearest point from array of point
     * @param array
     * @param p
     * @return 
     */
    public static Point getNearest(ArrayList<Point> array, Point p) {
        if (array.size() < 1)
            return null;
        float min = Float.POSITIVE_INFINITY;
        int index = 0;
        for (int i = 0; i < array.size(); i++) {
            float distance = distance(array.get(i), p);
            if(min > distance) {
                min = distance;
                index = i;
            }
        }
        return array.get(index);
    }
    
    /**
     * get furthest point from array of point
     * @param array
     * @param p
     * @return 
     */
    public static Point getFurthest(ArrayList<Point> array, Point p) {
        if (array.size() < 1)
            return null;
        float max = -1;
        int index = 0;
        for (int i = 0; i < array.size(); i++) {
            float distance = distance(array.get(i), p);
            if(max < distance) {
                max = distance;
                index = i;
            }
        }
        return array.get(index);
    }
    
    /**
     * get furthest distance from array of point
     * @param array
     * @param p
     * @return 
     */
    public static float getFurthestDistance(ArrayList<Point> array, Point p) {
        if (array.size() < 1)
            return 0;
        float max = -1;
        for (int i = 0; i < array.size(); i++) {
            float distance = distance(array.get(i), p);
            if(max < distance) {
                max = distance;
            }
        }
        return max;
    }
    
    /**
     * Plot a line in Cartesian coordinate using Bresenham method
     * @param A starting point
     * @param B ending point
     * @param width window width
     * @param height window height
     * @return array of point
     */
    public static ArrayList<Point> plotLine(Point A, Point B, int width, int height) {
        ArrayList<Point> array = new ArrayList<Point>();
        int x, y, dx, dy, incx, incy, balance, size;
        int x1 = A.x, x2 = B.x, y1 = A.y, y2 = B.y;
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

            while (x != x2 && x< width && x>=0) {
                int offset = x + width * y;
                if (offset >= 0 && offset < size) {
                    array.add(new Point(x,y));
                }
                if (balance >= 0) {
                    y += incy;
                    balance -= dx;
                }
                balance += dy;
                x += incx;
            }
            int offset = x + width * y;
            if (offset >= 0 && offset < size && x< width && x>=0) {
                array.add(new Point(x,y));
            }
        } else {
            dx <<= 1;
            balance = dx - dy;
            dy <<= 1;

            while (y != y2 && y< height && y>=0) {
                int offset = x + width * y;
                if (offset >= 0 && offset < size) {
                    array.add(new Point(x,y));
                }
                if (balance >= 0) {
                    x += incx;
                    balance -= dy;
                }
                balance += dx;
                y += incy;
            }
            int offset = x + width * y;
            if (offset >= 0 && offset < size && y< height && y>=0) {
                array.add(new Point(x,y));
            }
        }
        return array;
    }
    
    /**
     * Incise two line (array of point)
     * @param arrayA
     * @param arrayB
     * @return 
     */
    public static Point incise(ArrayList<Point> arrayA, ArrayList<Point> arrayB) {
        if(arrayA.size() > 0 && arrayB.size() > 0) {
            for(int i = 0; i< arrayA.size(); ++i) 
                for(int j = 0; j < arrayB.size(); ++j) {
                    if(distance(arrayA.get(i),arrayB.get(j)) < 2)
                        return arrayA.get(i);
                }
            return null;
        }
        else
            return null;
    }
}
