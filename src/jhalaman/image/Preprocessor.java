/*
 * Basic image preprocessing algorithm
 */
package jhalaman.image;

import java.awt.image.BufferedImage;
import java.util.Arrays;

/**
 *
 * @author Sandy Socrates <sandy_socrates@live.com>
 */
public class Preprocessor {

    public static void toGrayscale(BufferedImage buff) {
        int pix, gray;
        for (int i = 0; i < buff.getWidth(); ++i) {
            for (int j = 0; j < buff.getHeight(); ++j) {
                pix = buff.getRGB(i, j);
                gray = (int) (Utility.R(pix) * 0.299 + Utility.G(pix) * 0.587 + Utility.B(pix) * 0.114);
                buff.setRGB(i, j, Utility.toRGB(gray, gray, gray));
            }
        }
    }

    public static void toBinary(BufferedImage buff, int threshold) {
        int pix, gray;
        if (buff.getType() == BufferedImage.TYPE_BYTE_GRAY) {
            for (int i = 0; i < buff.getWidth(); ++i) {
                for (int j = 0; j < buff.getHeight(); ++j) {
                    pix = buff.getRGB(i, j);
                    gray = Utility.B(pix);
                    buff.setRGB(i, j, gray > threshold ? 0xffffff : 0x000000);
                }
            }
        } else {
            for (int i = 0; i < buff.getWidth(); ++i) {
                for (int j = 0; j < buff.getHeight(); ++j) {
                    pix = buff.getRGB(i, j);
                    gray = (int) (Utility.R(pix) * 0.299 + Utility.G(pix) * 0.587 + Utility.B(pix) * 0.114);
                    buff.setRGB(i, j, gray > threshold ? 0xffffff : 0x000000);
                }
            }
        }
    }

    public static void normalize(float[] in) {
        float min = 9999, max = -9999;
        for (int i = 0; i < in.length; ++i) {
            float temp = in[i];
            if (min > temp) {
                min = temp;
            }
            if (max < temp) {
                max = temp;
            }
        }

        float delta = max - min;
        // anti INF
        if (delta < 0.2) {
            delta = 1;
        }

        for (int i = 0; i < in.length; ++i) {
            in[i] = (in[i] - min) / delta * 255f;
        }
    }

    public static int[] normalizeToInt(float[] in) {
        int[] out = new int[in.length];

        float min = Float.POSITIVE_INFINITY, max = Float.NEGATIVE_INFINITY;
        for (int i = 0; i < in.length; ++i) {
            float temp = in[i];
            if (min > temp) {
                min = temp;
            }
            if (max < temp) {
                max = temp;
            }
        }

        float delta = max - min;
        // anti INF
        if (delta < 0.2) {
            delta = 1f;
        }

        for (int i = 0; i < in.length; ++i) {
            out[i] = (int) ((in[i] - min) / delta * 255);
        }
        return out;
    }

    public static int[] normalizeToInt(float[] in, int kernelSize, int height, int width) {
        int[] out = new int[in.length];
        Arrays.fill(out, 0);
        float min = Float.POSITIVE_INFINITY, max = Float.NEGATIVE_INFINITY;
        int offset;
        for (int i = kernelSize; i < height - kernelSize; ++i) {
            offset = i * width + 1;
            for (int j = kernelSize; j < width - kernelSize; ++j) {
                float temp = in[offset];
                if (min > temp) {
                    min = temp;
                }
                if (max < temp) {
                    max = temp;
                }
                ++offset;
            }
        }


        float delta = max - min;
        // anti INF
        if (delta < 0.2) {
            delta = 1f;
        }
        
        for (int i = kernelSize; i < height - kernelSize; ++i) {
            offset = i * width + 1;
            for (int j = kernelSize; j < width - kernelSize; ++j) {
                 out[offset] = (int) ((in[offset] - min) / delta * 255);
                 ++offset;
            }
        }

        return out;
    }

    public static void toGrayscale(Citra citra) {
        int pix, gray;
        for (int i = 0; i < citra.getWidth() * citra.getHeight(); ++i) {
            pix = citra.getVal(i);
            gray = (int) (Utility.R(pix) * 0.299 + Utility.G(pix) * 0.587 + Utility.B(pix) * 0.114);
            citra.setVal(i, gray);

        }
    }

    public static void toBinary(Citra citra) {
        for (int i = 0; i < citra.getWidth() * citra.getHeight(); ++i) {
            int pix = citra.getVal(i);
            citra.setVal(i, pix > 127 ? 0 : 255);
        }
    }

    public static void toBinary(Citra citra, int threshold) {
        for (int i = 0; i < citra.getWidth() * citra.getHeight(); ++i) {
            int pix = citra.getVal(i);
            citra.setVal(i, pix > threshold ? 255 : 0);
        }
    }

    public static void toBinary(Citra citra, int moreThan, int valid, int invalid) {
        int[] temp = citra.getVal();
        int i = temp.length;
        while (--i > 0) {
            temp[i] = temp[i] > moreThan ? valid : invalid;
        }
        citra.setVal(temp);
    }

    public static void otsuThreshold(Citra citra, int valid, int invalid) {
        int[] h = new int[256];
        int size = citra.getWidth() * citra.getHeight();
        int i = size;
        while (--i > 0) {
            ++h[citra.getVal(i)];
        }

        float sum = 0;
        for (int t = 0; t < 256; t++) {
            sum += t * h[t];
        }

        float sumB = 0;
        int wB = 0;
        int wF = 0;

        float varMax = 0;
        int threshold = 0;

        for (int t = 0; t < 256; t++) {
            wB += h[t];               // Weight Background
            if (wB == 0) {
                continue;
            }

            wF = size - wB;                 // Weight Foreground
            if (wF == 0) {
                break;
            }

            sumB += (float) (t * h[t]);

            float mB = sumB / wB;            // Mean Background
            float mF = (sum - sumB) / wF;    // Mean Foreground

            // Calculate Between Class Variance
            float varBetween = (float) wB * (float) wF * (mB - mF) * (mB - mF);
            // Check if new maximum found
            if (varBetween > varMax) {
                varMax = varBetween;
                threshold = t;
            }
        }
        toBinary(citra, threshold, valid, invalid);
    }

    public static void otsuThreshold(Citra citra) {

        int[] h = new int[256];
        int size = citra.getWidth() * citra.getHeight();
        int i = size;
        while (--i > 0) {
            ++h[citra.getVal(i)];
        }

        float sum = 0;
        for (int t = 0; t < 256; t++) {
            sum += t * h[t];
        }

        float sumB = 0;
        int wB = 0;
        int wF = 0;

        float varMax = 0;
        int threshold = 0;

        for (int t = 0; t < 256; t++) {
            wB += h[t];               // Weight Background
            if (wB == 0) {
                continue;
            }

            wF = size - wB;                 // Weight Foreground
            if (wF == 0) {
                break;
            }

            sumB += (float) (t * h[t]);

            float mB = sumB / wB;            // Mean Background
            float mF = (sum - sumB) / wF;    // Mean Foreground

            // Calculate Between Class Variance
            float varBetween = (float) wB * (float) wF * (mB - mF) * (mB - mF);
            // Check if new maximum found
            if (varBetween > varMax) {
                varMax = varBetween;
                threshold = t;
            }
        }
        toBinary(citra, threshold, 0, 255);
    }

    public static int getOtsuThreshold(Citra citra) {

        int[] h = new int[256];
        int size = citra.getWidth() * citra.getHeight();
        int i = size;
        while (--i > 0) {
            ++h[citra.getVal(i)];
        }

        float sum = 0;
        for (int t = 0; t < 256; t++) {
            sum += t * h[t];
        }

        float sumB = 0;
        int wB = 0;
        int wF = 0;

        float varMax = 0;
        int threshold = 0;

        for (int t = 0; t < 256; t++) {
            wB += h[t];               // Weight Background
            if (wB == 0) {
                continue;
            }

            wF = size - wB;                 // Weight Foreground
            if (wF == 0) {
                break;
            }

            sumB += (float) (t * h[t]);

            float mB = sumB / wB;            // Mean Background
            float mF = (sum - sumB) / wF;    // Mean Foreground

            // Calculate Between Class Variance
            float varBetween = (float) wB * (float) wF * (mB - mF) * (mB - mF);
            // Check if new maximum found
            if (varBetween > varMax) {
                varMax = varBetween;
                threshold = t;
            }
        }
        return threshold;
    }

    public static void equalize(Citra citra) {
        int size = citra.getWidth() * citra.getHeight();

        int[] temp = citra.getVal();

        int[] histogram = new int[256];
        for (int i = 0; i < 256; ++i) {
            histogram[i] = 0;
        }

        for (int i = 0; i < size; ++i) {
            ++histogram[temp[i]];
        }

        int min = 0, sum = 0;
        int[] chistogram = new int[256];
        Arrays.fill(chistogram, 0);

        while (histogram[min] == 0) {
            ++min;
        }

        for (int i = min; i < 256; ++i) {
            sum += histogram[i];
            chistogram[i] = sum;
        }

        min = chistogram[min];
        for (int i = 0; i < 256; ++i) {
            histogram[i] = (chistogram[i] - min) * 255 / (size - min);
        }

        sum = 0;
        for (int i = 0; i < 256; ++i) {
            sum += histogram[i];
        }

        for (int i = 0; i < size; ++i) {
            temp[i] = histogram[temp[i]];
        }

        citra.setVal(temp);
    }

    public static Citra binaryEdgeDetect(Citra citra) {
        int width = citra.getWidth();
        int height = citra.getHeight();
        int size = width * height;
        Citra out = new Citra(citra.getWidth(),
                citra.getHeight());
        boolean[] A = new boolean[size];
        int[] B = new int[size];
        Arrays.fill(A, false);
        Arrays.fill(B, 0);
        for (int i = 0; i < size; ++i) {
            B[i] = 0xffffff;
            A[i] = citra.getVal(i) != -1;
        }

        for (int i = 1; i < height - 1; ++i) {
            int offset = i * width;
            for (int j = 1; j < width; ++j) {
                int x = offset + j;
                B[x] = (!A[x] && (A[x + 1] || A[x + width] || A[x - width] || A[x - 1])) ? 0xffffffff : 0xff000000;
            }
        }
        out.setVal(B);
        return out;
    }

    public static void removeNoise(Citra citra, int lengthThreshold) {
        Boolean[] edgel = citra.toBoolean();
        int width = citra.getWidth();
        int height = citra.getHeight();
        int max = edgel.length - width;

        for (int i = 1; i < height - 1; ++i) {
            int offset = i * width;
            for (int j = 1; j < width; ++j) {
                int sum = 0;
                if (edgel[offset]) {
                    if (edgel[offset - 1]) {
                        sum++;
                    }
                    if (edgel[offset - width]) {
                        sum++;
                    }
                    if (edgel[offset - 1 - width]) {
                        sum++;
                    }
                    if (edgel[offset - 1 + width]) {
                        sum++;
                    }
                    if (edgel[offset + width]) {
                        sum++;
                    }
                    if (edgel[offset + width + 1]) {
                        sum++;
                    }
                    if (edgel[offset + 1]) {
                        sum++;
                    }
                    if (edgel[offset + width - 1]) {
                        sum++;
                    }
                    if (sum == 0) {
                        edgel[offset] = false;
                    }
                }
            }
        }
    }
}
