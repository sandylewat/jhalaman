/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jhalaman.image.filters;

import java.awt.image.BufferedImage;
import jhalaman.image.Preprocessor;
import jhalaman.image.Utility;

/**
 *
 * @author Sandy Socrates <sandy_socrates@live.com>
 */
public class CannyEdge {
    private float lowerThreshold = 2.5F;
    private float upperThreshold = 7.5F;
    private float[][] xGradients;
    private float[][] yGradients;
    private int[][] G;
    private int[][] Mag;
    private float lower;
    private float upper;
    private MatrixOperator gaussMatrix = new MatrixOperator(MatrixOperator.GAUSSIAN_KERNEL_5);
    private MatrixOperator xSobelMatrix = new MatrixOperator(MatrixOperator.SOBEL_OPERATOR_KERNEL_X_3);
    private MatrixOperator ySobelMatrix = new MatrixOperator(MatrixOperator.SOBEL_OPERATOR_KERNEL_Y_3);
        
    private boolean isEqualize = true;
        
    public CannyEdge(float low, float high, boolean equalize) {
    //TODO : set max and min threshold
        lowerThreshold = low;
        upperThreshold = high;
        this.lower = lowerThreshold * 100;
        this.upper = upperThreshold * 100;
        
        isEqualize = equalize;
        gaussMatrix.setConvolutionFactor(MatrixOperator.GAUSSIAN_KERNEL_FACTOR_5);
        xSobelMatrix.setConvolutionFactor(MatrixOperator.SOBEL_OPERATOR_KERNEL_FACTOR);
        ySobelMatrix.setConvolutionFactor(MatrixOperator.SOBEL_OPERATOR_KERNEL_FACTOR);
    }
    
    public BufferedImage detect(BufferedImage buffer) {
        //TODO input image and do Canny Edge Detection
        BufferedImage out = Preprocessor.cloneGrayscale(buffer);
        if (isEqualize)
            Preprocessor.equalize(out);
        
        //Algorithm :
        //1. Smoothing
        gaussMatrix.convolve(out);
        //2. Finding gradients
        findGradients(out);
        //3. Non-maximum supression
        nonMaximumSupression(out);
        //4. Double thresholding and Edge tracking by hysteresis
        return doubleThreshold(out);
    }
    
    private void findGradients(BufferedImage buffer) {
                
        xGradients = xSobelMatrix.convolve(Utility.grayscaleToFloatMatrix(buffer));        
        yGradients = ySobelMatrix.convolve(Utility.grayscaleToFloatMatrix(buffer));
        G = new int[buffer.getWidth()][buffer.getHeight()];
        Mag = new int[buffer.getWidth()][buffer.getHeight()];
  
        int hyp;
        for (int i = 0; i < buffer.getWidth(); ++i) {
            for (int j = 0; j < buffer.getHeight(); ++j) {
                hyp = (int) Math.hypot((double)xGradients[i][j], (double) yGradients[i][j]);
                G[i][j] = hyp;
            }
        }
    }
    
    private void nonMaximumSupression(BufferedImage buffer) {
        int dx, dy, a1, a2, b1, b2;
        float A, B, point;
        for (int i = 1; i < buffer.getWidth()-1; ++i) {
            for (int j = 1; j < buffer.getHeight()-1; ++j) {        
                dx = xGradients[i][j] > 0 ? 1 : -1;
                dy = yGradients[i][j] > 0 ? 1 : -1;
                if (Math.abs(xGradients[i][j]) > Math.abs(yGradients[i][j])) {
                    a1 = G[i+dx][j];
                    a2 = G[i+dx][j-dy];
                    b1 = G[i-dx][j];
                    b2 = G[i-dx][j-dy];
                    A = (Math.abs(xGradients[i][j]) - Math.abs(yGradients[i][j])) * a1 + Math.abs(yGradients[i][j]) * a2;
                    B = (Math.abs(xGradients[i][j]) - Math.abs(yGradients[i][j])) * b1 + Math.abs(yGradients[i][j]) * b2;
                    point = G[i][j] * Math.abs(xGradients[i][j]);
                    if (point >= A && point> B )
                        Mag[i][j] = (int) Math.abs(xGradients[i][j]);
                    else
                        Mag[i][j] = 0;                    
                }
                else {
                    a1 = G[i][j-dy];
                    a2 = G[i+dx][j-dy];
                    b1 = G[i][j+dy];
                    b2 = G[i-dx][j+dy];
                    
                    A = (Math.abs(yGradients[i][j]) - Math.abs(xGradients[i][j])) * a1 + Math.abs(xGradients[i][j]) * a2;
                    B = (Math.abs(yGradients[i][j]) - Math.abs(xGradients[i][j])) * b1 + Math.abs(xGradients[i][j]) * b2;
                    point = G[i][j] * Math.abs(yGradients[i][j]);
                    if (point >= A && point > B)
                        Mag[i][j] = (int) Math.abs(yGradients[i][j]);
                    else
                        Mag[i][j] = 0;
                }
            }
        }
    }
    
    private BufferedImage doubleThreshold(BufferedImage buffer) {
        
        BufferedImage out = new BufferedImage(buffer.getWidth(), buffer.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        for (int i = 1; i < buffer.getWidth() - 1; ++i) {
            for (int j = 1; j < buffer.getHeight() - 1; ++j) {
                if (Mag[i][j] > upper)
                    out.setRGB(i, j, 0xffffff);
                else if(Mag[i][j] < lower)
                {}
                else if(follow(i,j))
                    out.setRGB(i, j, 0xffffff);
            }
        }
        return out;
    }
    
    private boolean follow(int x, int y) {
        for (int i = -1; i < 2; ++i) {
            for (int j = -1; j < 2; ++j) {
                if (Mag[x+i][y+j] > upper)
                    return true;
            }
        }
        return false;
    }
}
