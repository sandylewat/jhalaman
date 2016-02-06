/**
 * Canny Edge detection algorithm implementation
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
public class CannyEdge {

    private float[] xGrad;
    private float[] yGrad;
    private int[] angle;
    private float[] Grad;
    private int gaussianWidth = 16;
    private float gaussianSigma = 2f;
    private int[] Mag;
    private float lower;
    private float upper;
    private MatrixOperator mo = null;
    private boolean isEqualize = true;

    public CannyEdge(float low, float high, boolean equalize, float gaussSigma) {
        this.lower = low;
        this.upper = high;
        gaussianSigma = gaussSigma;
        gaussianWidth = 2 * Math.round((float) Math.sqrt(-Math.log(0.1) * 2.0 * gaussSigma * gaussSigma)) + 1;
        mo = new MatrixOperator(MatrixOperator.generateXGaussianKernel(gaussianSigma));
        isEqualize = equalize;
    }

    public Citra detect(Citra citra) {
        Citra out = new Citra(citra.getHeight(), citra.getWidth(), citra.getVal());
        if (isEqualize) {
            Preprocessor.equalize(out);
        }

        //Algorithm :
        //1. Smoothing and 2. Finding gradients
        try {
            findGradients(out);
        } catch (Exception ex) {
            Logger.getLogger(CannyEdge.class.getName()).log(Level.SEVERE, null, ex);
        }

        //3. Non-maximum supression
        nonMaximumSupression(out);
        out = new Citra(citra.getHeight(), citra.getWidth());
        //4. Double thresholding and 5.Edge tracking by hysteresis
        doubleThreshold(out);
        return out;
    }

    private void findGradients(Citra citra) throws Exception {

        int width = citra.getWidth();
        int height = citra.getHeight();
        int size = width * height;

        xGrad = mo.convolveToFloat(citra);
        mo.setKernel(MatrixOperator.generateYGaussianKernel(gaussianSigma));
        yGrad = mo.convolveToFloat(citra);
        Grad = new float[size];
        angle = new int[size];
        Mag = new int[size];
        float pi = (float) Math.PI;
        float areaI = pi / 8;
        float areaII = 3 * areaI;
        float areaIII = 5 * areaI;
        float areaIV = 7 * areaI;
        for (int i = 0; i < size; ++i) {
            float x = xGrad[i];
            float y = yGrad[i];

            Grad[i] = (float) Math.hypot(x, y); //got gradient

            float theta = (float) Math.atan2(x, y);
            //angle

            //to acomodate negative value given by atan2
            //we add pi, as pi+theta would give similar tan value
            if (theta < 0) {
                theta = pi + theta;
            }
            // using polar coordinate we can map theta to
            //          pi/2
            //            3
            //       2    |    4
            //   1        |       1
            // pi --------|-------- 0
            //   1        |       1
            //       4    |    2
            //            3    
            //          3pi/2
            // 1 angle east/west
            // 2 angle north east/south west
            // 3 angle north/south
            // 4 angle north west/south east

            angle[i] = theta <= areaI ? 1 : theta <= areaII ? 2
                    : theta <= areaIII ? 3 : theta <= areaIV ? 4 : 1;

        }
        Preprocessor.normalize(Grad);

    }

    private void nonMaximumSupression(Citra citra) {
        int width = citra.getWidth();
        int height = citra.getHeight();
        int n1, n2, area;
        float v;
        Arrays.fill(Mag, 0);
        for (int y = width; y < width * (height - gaussianWidth / 2); y += width) {
            for (int x = gaussianWidth / 2; x < width - gaussianWidth / 2; ++x) {
                int idx = y + x;
                v = Grad[idx];
                area = angle[idx];

                if (area == 3) {
                    n1 = idx - 1;
                    n2 = idx + 1;
                } else if (area == 4) {
                    n1 = idx - width + 1;
                    n2 = idx + width - 1;
                } else if (area == 1) {
                    n1 = idx - width;
                    n2 = idx + width;
                } else {
                    n1 = idx - 1 - width;
                    n2 = idx + 1 + width;
                }
                if (!(v >= Grad[n1] && v >= Grad[n2])) {
                    Mag[idx] = 0;
                } else {
                    Mag[idx] = (int) Grad[idx];
                }
            }
        }
    }

    private void doubleThreshold(Citra citra) {
        Arrays.fill(Grad, 0);
        int offset;
        int width = citra.getWidth();
        int height = citra.getHeight();
        int size = height * width;
        for (int i = gaussianWidth / 2; i < citra.getHeight() - gaussianWidth / 2; ++i) {
            offset = i * width;
            for (int j = 1; j < citra.getWidth() - gaussianWidth / 2; ++j) {
                if (Grad[offset + j] == 0 && Mag[offset + j] >= upper) {
                    follow(j, i, width);
                }
            }
        }
        int out[] = new int[size];
        for (int i = 0; i < size; ++i) {
            out[i] = Grad[i] != 0 ? 255 : 0;
        }
        citra.setVal(out);
    }

    private void follow(int x, int y, int width) {
        Grad[x + y * width] = Mag[x + y * width];
        for (int i = -1; i < 2; ++i) {
            for (int j = -1; j < 2; ++j) {
                if (Grad[x + i + (y + j) * width] == 0 && Mag[x + i + (y + j) * width] > lower) {
                    follow(x + i, y + j, width);
                }
            }
        }
    }
}
