
package jhalaman.image;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;


/**
 *
 * @author Sandy Socrates <sandy_socrates@live.com>
 */
public class Utility {
    public static BufferedImage imageLoader(String url) {
        BufferedImage out = null;
        try {
            out = ImageIO.read(new File(url));
        }
        catch (IOException ex) {
            System.out.println("File " + System.getProperty("user.dir") + url + " was not found.");
            Logger.getLogger(Utility.class.getName()).log(Level.SEVERE, null, ex);
        }
        return out;
    }
    
    public static BufferedImage imageClone(BufferedImage buffer) {
        ColorModel cm = buffer.getColorModel();
        String[] pnames = buffer.getPropertyNames();
        Hashtable<String, Object> cproperties = new Hashtable<String, Object>();
        if (pnames != null) {
            for (int i = 0; i < pnames.length; i++) {
                cproperties.put(pnames[i], buffer.getProperty(pnames[i]));
            }
        }
        return new BufferedImage(cm, buffer.copyData(null), cm.isAlphaPremultiplied(), cproperties); 
    }
    
    public static float[][] grayscaleToFloatMatrix(BufferedImage buffer) {
        float[][] out = new float[buffer.getWidth()][buffer.getHeight()];
        for (int i = 0; i < buffer.getWidth(); ++i) {
            for (int j = 0; j < buffer.getHeight(); ++j) {
                out[i][j] = Utility.B(buffer.getRGB(i, j));
            }
        }
        return out;
    }
    
    public static int[][] grayscaleToIntMatrix(BufferedImage buffer) {
        int[][] out = new int[buffer.getWidth()][buffer.getHeight()];
        for (int i = 0; i < buffer.getWidth(); ++i) {
            for (int j = 0; j < buffer.getHeight(); ++j) {
                out[i][j] = Utility.B(buffer.getRGB(i, j));
            }
        }
        return out;
    }
    
    public static BufferedImage matrixToGrayscale(float[][] buffer) {
        BufferedImage out = new BufferedImage(buffer.length, buffer[0].length, BufferedImage.TYPE_BYTE_GRAY);
        int gray;
        for (int i = 0; i < buffer.length; ++i) {
            for (int j = 0; j < buffer[0].length; ++j) {
                gray = (int) buffer[i][j];
                gray = gray > 255 ? 255 : gray;
                gray = gray < 0 ? 0 : gray;
                out.setRGB(i, j, toRGB(gray, gray, gray));
            }
        }
        return out;
    }
    
    public static BufferedImage matrixToGrayscale(int[][] buffer) {
        BufferedImage out = new BufferedImage(buffer.length, buffer[0].length, BufferedImage.TYPE_BYTE_GRAY);
        int gray;
        for (int i = 0; i < buffer.length; ++i) {
            for (int j = 0; j < buffer[0].length; ++j) {
                gray = buffer[i][j];
                gray = gray > 255 ? 255 : gray;
                gray = gray < 0 ? 0 : gray;
                out.setRGB(i, j, toRGB(gray, gray, gray));
            }
        }
        return out;
    }
    
    public static int R (int pix) {
        return (pix >> 16) & 0xff;
    }
    
    public static int G (int pix) {
        return (pix >> 8) & 0xff;
    }
    
    public static int B (int pix) {
        return  (pix) & 0xff;
    }
    
    public static int toRGB(int R, int G, int B) {
        return  (R << 16 | G << 8 | B) & 0xffffff;
    }
}
