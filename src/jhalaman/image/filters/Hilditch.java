/**
 * Hildtch skeletonization algorithm implementation
 */
package jhalaman.image.filters;

import java.util.Arrays;
import jhalaman.image.Citra;
import jhalaman.image.Preprocessor;


/**
 *
 * @author Sandy Socrates <sandy_socrates@live.com>
 */
public class Hilditch {

    int height;
    int width;
    int size;
    Boolean[] in;
    
    public Hilditch() {
    }

    public void skeletonize(Citra citra, int threshold, int valid, int invalid) {
        Preprocessor.toBinary(citra, threshold, valid, invalid);
        width = citra.getWidth();
        height = citra.getHeight();
        size = width * height;
        in = citra.toBoolean();

        for (int i = 0; i < width; ++i) {
            in[i] = false;
            in[i+width] = false;
            in[size - 1 - i - width] = false;
            in[size - 1 - i] = false;
        }

        for (int i = 1; i < height - 1; ++i) {
            int j = i * width;
            in[j] = false;
            in[j + 1] = false;
            in[j - 1] = false;
            in[j - 1] = false;
        }

        int prevRun = 0;
        int currentRun = 0;
        
        int i = 0;
        do {
            ++i;
            prevRun = currentRun;
            currentRun = thin();
        } while (prevRun != currentRun);
        
        citra.setVal(in);
    }

    private int thin() {
        int count = 0;
        int i = size - width - 2;
        Boolean[] out = Arrays.copyOf(in, size);
        while (i-- > 2*width) {
            Boolean p1 = in[i];
            if (p1) {
                if (i % width > 1 && i % width < width - 2) {
                    Boolean p2 = in[i - width];
                    Boolean p3 = in[i - width + 1];
                    Boolean p4 = in[i + 1];
                    Boolean p5 = in[i + width + 1];
                    Boolean p6 = in[i + width];
                    Boolean p7 = in[i + width - 1];
                    Boolean p8 = in[i - 1];
                    Boolean p9 = in[i - width - 1];

                    int B = B(p2, p3, p4, p5, p6, p7, p8, p9);
                    if (B > 1 && B < 7) {
                        if (A(p2, p3, p4, p5, p6, p7, p8, p9) == 1) {
                            Boolean p10 = in[i - 2 * width - 1];
                            Boolean p11 = in[i - 2 * width];
                            Boolean p12 = in[i - 2 * width + 1];

                            if ((!p2 || !p4 || !p8) || A(p11, p12, p3, p4, p1, p8, p9, p10) != 1) {
                                p10 = in[i - width + 2];
                                p11 = in[i + 2];
                                p12 = in[i + width + 2];

                                if (!(p2 && p4 && p6) || A(p3, p10, p11, p12, p5, p6, p1, p2) != 1) {
                                    out[i] = false;
                                    --count;
                                }
                            }
                        }
                    }
                }
                ++count;
            }
        }
        in = out;
        return count;
    }

    /**
     * calculate the eight 3x3 neighbour of p1 and count 0,1 pattern
     *
     * @param	p2 270
     * @param	p3 315
     * @param	p4 0
     * @param	p5 45
     * @param	p6 90
     * @param	p7 135
     * @param	p8 180
     * @param	p9 225
     * @return 0,1 pattern count
     */
    private int A(Boolean p2, Boolean p3, Boolean p4, Boolean p5, Boolean p6, Boolean p7, Boolean p8, Boolean p9) {
        int out = 0;
        if (!p2 && p3) {
            ++out;
        }
        if (!p3 && p4) {
            ++out;
        }
        if (!p4 && p5) {
            ++out;
        }
        if (!p5 && p6) {
            ++out;
        }
        if (!p6 && p7) {
            ++out;
        }
        if (!p7 && p8) {
            ++out;
        }
        if (!p8 && p9) {
            ++out;
        }
        if (!p9 && p2) {
            ++out;
        }

        return out;
    }

    /**
     * count colored pixel
     *
     * @param	p2 270
     * @param	p3 315
     * @param	p4 0
     * @param	p5 45
     * @param	p6 90
     * @param	p7 135
     * @param	p8 180
     * @param	p9 225
     * @return colored pixel
     */
    private int B(Boolean p2, Boolean p3, Boolean p4, Boolean p5, Boolean p6, Boolean p7, Boolean p8, Boolean p9) {
        int out = 0;
        if (p2) {
            out++;
        }
        if (p3) {
            out++;
        }
        if (p4) {
            out++;
        }
        if (p5) {
            out++;
        }
        if (p6) {
            out++;
        }
        if (p7) {
            out++;
        }
        if (p8) {
            out++;
        }
        if (p9) {
            out++;
        }

        return out;
    }
}
