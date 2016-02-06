/*
 * Image Ridge Detection algorithm implementation
 */
package jhalaman.image.filters;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import jhalaman.image.Citra;
import jhalaman.image.Preprocessor;

/**
 *
 * @author Sandy Socrates <sandy_socrates@live.com>
 */
public class RidgeFilter {
    private MatrixOperator mo;
    private int gaussianWidth;
    private float factor;
    
    public RidgeFilter() {
    }
     
    public void detect(Citra in, float sigma) {
        int width = in.getWidth();
        int height = in.getHeight();
        Citra out = new Citra(height, width);
        gaussianWidth = 2 * Math.round((float)Math.sqrt(-Math.log(0.1)*2.0*sigma*sigma))+1;
        
        float[] dXY = new float[width];
        float[] dXX = new float[width];
        float[] dYY = new float[width];
        try {
            mo = new MatrixOperator(sigma);
            mo.setKernel(MatrixOperator.generateXYGaussianKernel(sigma));
            dXY = mo.convolveToFloat(in);
            mo.setKernel(MatrixOperator.generateXXGaussianKernel(sigma));
            dXX = mo.convolveToFloat(in);
            mo.setKernel(MatrixOperator.generateYYGaussianKernel(sigma));
            dYY = mo.convolveToFloat(in);
        } catch (Exception ex) {
            Logger.getLogger(RidgeFilter.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        int size = width*height;
        float[] temp = new float[size];
        Arrays.fill(temp, 0);
        int offset;
        
        for(int i = gaussianWidth/2; i < height-gaussianWidth/2; ++i) {
            offset = i*width+1;
            for(int j = gaussianWidth/2; j < width-gaussianWidth/2; ++j) {
                float xx = dXX[offset];
                float xy = dXY[offset];
                float yy = dYY[offset];

                temp[offset] = (float) (xx + yy + Math.sqrt(xx*xx + 4*xy*xy - 2*xx*yy + yy*yy))/2;
                ++offset;
            }
        }        
        in.setVal(Preprocessor.normalizeToInt(temp, gaussianWidth/2, height, width));
    }
}
