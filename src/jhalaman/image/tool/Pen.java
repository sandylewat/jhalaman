/*
 * Basic drawing function
 */
package jhalaman.image.tool;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import jhalaman.image.Citra;

/**
 *
 * @author Sandy Socrates <sandy_socrates@live.com>
 */
public class Pen {

    public static void fastLine(Citra citra, int x1, int y1, int x2, int y2, int color) {
        int x, y, dx, dy, incx, incy, balance, width, height, size;
        width = citra.getWidth();
        height = citra.getHeight();
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
                if (offset >= 0 && offset < size) {
                    citra.setVal(x + width * y, color);
                }
                if (balance >= 0) {
                    y += incy;
                    balance -= dx;
                }
                balance += dy;
                x += incx;
            }
            int offset = x + width * y;
            if (offset >= 0 && offset < size) {
                citra.setVal(x + width * y, color);
            }
        } else {
            dx <<= 1;
            balance = dx - dy;
            dy <<= 1;

            while (y != y2) {
                int offset = x + width * y;
                if (offset >= 0 && offset < size) {
                    citra.setVal(x + width * y, color);
                }
                if (balance >= 0) {
                    x += incx;
                    balance -= dy;
                }
                balance += dx;
                y += incy;
            }
            int offset = x + width * y;
            if (offset >= 0 && offset < size) {
                citra.setVal(x + width * y, color);
            }
        }
    }

    public static void fastLine(BufferedImage buffer, int x1, int y1, int x2, int y2, int color) {
        int x, y, dx, dy, incx, incy, balance, width, height;
        width = buffer.getWidth();
        height = buffer.getHeight();
        int[] val = new int[width * height];
        buffer.getRGB(0, 0, width, height, val, 0, width);
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

            while (x != x2 && x >= 0 && x < width) {
                int offset = x + width * y;
                if (offset >= 0 && offset < val.length) {
                    val[x + width * y] = color;
                }
                if (balance >= 0) {
                    y += incy;
                    balance -= dx;
                }
                balance += dy;
                x += incx;
            }
            int offset = x + width * y;
            if (offset >= 0 && offset < val.length) {
                val[x + width * y] = color;
            }
        } else {
            dx <<= 1;
            balance = dx - dy;
            dy <<= 1;

            while (y != y2 && y >= 0 && y < height) {
                int offset = x + width * y;
                if (offset >= 0 && offset < val.length) {
                    val[x + width * y] = color;
                }
                if (balance >= 0) {
                    x += incx;
                    balance -= dy;
                }
                balance += dx;
                y += incy;
            }
            int offset = x + width * y;
            if (offset >= 0 && offset < val.length) {
                val[x + width * y] = color;
            }
        }
        buffer.setRGB(0, 0, width, height, val, 0, width);
    }
    
    public static void draw(Citra citra, ArrayList<Point> array, int color) {
        for(int i=0; i<array.size(); ++i) {
            Point temp = array.get(i);
            if(temp.x >= 0 && temp.x < citra.getWidth() && temp.y >= 0 && temp.y < citra.getHeight())
                citra.setVal(temp.x+temp.y*citra.getWidth(), color);
        }
    }
    
    public static void draw(BufferedImage buffer, ArrayList<Point> array, int color) {
        for(int i=0; i<array.size(); ++i) {
            Point temp = array.get(i);
            if(temp.x >= 0 && temp.x < buffer.getWidth() && temp.y >= 0 && temp.y < buffer.getHeight())
                buffer.setRGB(temp.x, temp.y, color);
        }
    }
    
}
