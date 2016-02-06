/**
 * Utility functions
 */

package jhalaman.image;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
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
    
    public static int R (int pix) {
        return (pix & 0xff0000) >> 16;
    }
    
    public static int G (int pix) {
        return (pix & 0xff00) >> 8;
    }
    
    public static int B (int pix) {
        return  (pix & 0xff);
    }
    
    public static int toRGB(int R, int G, int B) {
        return  (R << 16 | G << 8 | B) & 0xffffffff;
    }
    
    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        int w = img.getWidth();
        int h = img.getHeight();
        BufferedImage dimg = dimg = new BufferedImage(newW, newH, img.getType());  
        Graphics2D g = dimg.createGraphics();  
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);  
        g.drawImage(img, 0, 0, newW, newH, 0, 0, w, h, null);  
        g.dispose();  
        return dimg;  
    }
    
    public static BufferedImage redraw(BufferedImage img, int maxW, int maxH) {
        int width = img.getWidth();
        int height = img.getHeight();
        
        if (width > maxW) {
            if (height / width * maxW > maxH)
                return resize(img, (int) (width * maxH / height), maxH);
            else
                return resize(img, maxW, (int) (height * maxW / width));
            
        } else if (height > maxH) {
            if (width / height * maxH > maxW)
                return resize(img, maxW, (int) (height * maxW / width));
            else
                return resize(img, (int) (width * maxH / height), maxH);    
        } else
            return resize(img, width , height);
    }
}
