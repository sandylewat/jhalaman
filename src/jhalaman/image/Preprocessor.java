/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jhalaman.image;

import java.awt.image.BufferedImage;

/**
 *
 * @author Sandy Socrates <sandy_socrates@live.com>
 */
public class Preprocessor {
    
    public static void toGrayscale(BufferedImage buff) {
        int pix, gray;
        for (int i = 0; i < buff.getWidth(); ++i) {
            for (int j = 0; j < buff.getHeight(); ++j) {
                pix = buff.getRGB(i,j);
                gray = (int) (Utility.R(pix) * 0.299 + Utility.G(pix) * 0.587 + Utility.B(pix) * 0.114);
                buff.setRGB(i,j, Utility.toRGB(gray, gray, gray));
            }
        }
    }
    
    public static BufferedImage cloneGrayscale(BufferedImage buff) {
        BufferedImage out = new BufferedImage(buff.getWidth(), buff.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        int pix, gray;
        for (int i = 0; i < buff.getWidth(); ++i) {
            for (int j = 0; j < buff.getHeight(); ++j) {
                pix = buff.getRGB(i,j);
                gray = (int) (Utility.R(pix) * 0.299 + Utility.G(pix) * 0.587 + Utility.B(pix) * 0.114);
                out.setRGB(i,j, Utility.toRGB(gray, gray, gray));
            }
        }
        return out;
    }
    
    public static void toBinary(BufferedImage buff) {
        int pix, gray;
        for (int i = 0; i < buff.getWidth(); ++i) {
            for (int j = 0; j < buff.getHeight(); ++j) {
                pix = buff.getRGB(i,j);
                gray = (int) (Utility.R(pix) * 0.299 + Utility.G(pix) * 0.587 + Utility.B(pix) * 0.114);
                buff.setRGB(i,j, gray > 125 ? 0xffffff : 0x000000);
            }
        }
    }
    
    public static BufferedImage cloneBinary(BufferedImage buff) {
        int pix, gray;
        BufferedImage out = new BufferedImage(buff.getWidth(),buff.getHeight(),BufferedImage.TYPE_BYTE_BINARY);
        for (int i = 0; i < buff.getWidth(); ++i) {
            for (int j = 0; j < buff.getHeight(); ++j) {
                pix = buff.getRGB(i,j);
                gray = (int) (Utility.R(pix) * 0.299 + Utility.G(pix) * 0.587 + Utility.B(pix) * 0.114);
                out.setRGB(i,j, gray > 125 ? 0xffffff : 0x000000);
            }
        }
        return out;
    }
    
    public static void equalize(BufferedImage buffer) {
        int size = buffer.getWidth() * buffer.getHeight();
        if (buffer.getType() != BufferedImage.TYPE_BYTE_GRAY)
            Preprocessor.toGrayscale(buffer);
        
        int[][] temp = Utility.grayscaleToIntMatrix(buffer);

        int[] histogram = new int[256];
        for (int i = 0; i < 256; ++i)
            histogram[i] = 0;

        for (int i = 0; i< buffer.getWidth(); ++i) {
            for (int j = 0; j < buffer.getHeight(); ++j) {
                ++histogram[temp[i][j]];
            }
        }
        
        int sum = 0, min = 0;
        int[] chistogram = new int[256];
        for (int i = 0; i < 256; ++i)
            chistogram[i] = 0;

        while (histogram[min] == 0) {
            ++min;
        }
        
        for (int i = min; i < 256; ++i) {
            sum += histogram[i];
            chistogram[i] = sum;
        }
        min = chistogram[min];
        for (int i = 0; i < 256; ++i) {
            histogram[i] = (chistogram[i] - min)* 255 /(size - min);
        }
        int gray;
        for (int i = 0; i < buffer.getWidth(); ++i) {
            for (int j = 0; j < buffer.getHeight(); ++j) {
                gray = histogram[temp[i][j]];
                buffer.setRGB(i, j, Utility.toRGB(gray, gray, gray));
            }
        }
    }
}
 