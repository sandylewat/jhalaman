/*
 * Basic image convolution and matrix operation
 */
package jhalaman.image.filters;

import java.util.Arrays;
import jhalaman.image.Citra;

/**
 *
 * @author Sandy Socrates <sandy_socrates@live.com>
 */
public class MatrixOperator {

    public static float[][] GAUSSIAN_KERNEL_5 = {{1, 4, 7, 4, 1},
        {4, 16, 26, 16, 4},
        {7, 26, 41, 26, 7},
        {4, 16, 26, 16, 4},
        {1, 4, 7, 4, 1}};
    public static float[][] GAUSSIAN_KERNEL_X = {{15, 69, 114, 69, 15},
        {35, 155, 255, 155, 35}, {0, 0, 0, 0, 0}, {-35, -155, -255, -155, -35},
        {-15, -69, -114, -69, -15}};
    public static float[][] GAUSSIAN_KERNEL_Y = {{15, 35, 0, -35, -15},
        {69, 155, 0, -155, -69}, {114, 255, 0, -255, -114}, {69, 155, 0, -155, -69},
        {-15, -35, 0, -35, -15}};
    public static float[][] SCHAR_OPERATOR_KERNEL_X_3 = {{-3, 0, 3},
        {-10, 0, 10},
        {-3, 0, 3}};
    public static float[][] SCHAR_OPERATOR_KERNEL_Y_3 = {{-3, -10, -3},
        {0, 0, 0},
        {3, 10, 3}};
    public static float[][] SOBEL_OPERATOR_KERNEL_X_3 = {{-1, 0, 1},
        {-2, 0, 2},
        {-1, 0, 1}};
    public static float[][] SOBEL_OPERATOR_KERNEL_Y_3 = {{-1, -2, -1},
        {0, 0, 0},
        {1, 2, 1}};
    public static float[][] PREWITT_OPERATOR_KERNEL_X_3 = {{-1, 0, 1},
        {-1, 0, 1},
        {-1, 0, 1}};
    public static float[][] PREWITT_OPERATOR_KERNEL_Y_3 = {{-1, -1, -1},
        {0, 0, 0},
        {1, 1, 1}};
    public static float GAUSSIAN_KERNEL_FACTOR_5 = (float) 1 / 273;
    public static float SOBEL_OPERATOR_KERNEL_FACTOR = 1f;
    private int kernelSize = 0;
    private float[][] kernel;
    private float convolutionFactor = 1f;
    private boolean isReady = false;
    private int width;
    private int height;
    private int margin;

    public MatrixOperator(float[][] kernel) {
        if (kernel.length > 1 && kernel[0].length == kernel.length) {
            this.kernel = kernel;
            this.kernelSize = kernel.length;
            margin = kernelSize / 2;
            this.isReady = true;
        } else {
            System.out.println("Kernel isn't square or size less than 2");
            throw new ExceptionInInitializerError();
        }
    }

    public MatrixOperator(float gaussianSigma) {
        int n = 2 * Math.round((float) Math.sqrt(-Math.log(0.1) * 2.0 * gaussianSigma * gaussianSigma)) + 1;
        float pi = (float) Math.PI;

        float[][] out = new float[n][n];
        int k = n / 2;
        float sum = 0f;
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                sum += out[i][j] = (float) Math.exp(-((i - k) * (i - k) + (j - k) * (j - k)) / (2 * pi * gaussianSigma * gaussianSigma));
            }
        }

        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                out[i][j] = out[i][j] / sum;
            }
        }
        this.kernel = out;
        this.kernelSize = n;
        this.convolutionFactor = 1f;
        margin = kernelSize / 2;
        this.isReady = true;

    }

    /**
     * @return the kernel
     */
    public float[][] getKernel() {
        return kernel;
    }

    /**
     * @param kernel the kernel to set
     */
    public void setKernel(float[][] kernel) throws Exception {
        if (kernel.length > 1 && kernel[0].length == kernel.length) {
            this.kernel = kernel;
            this.kernelSize = kernel.length;
            this.margin = kernelSize / 2;
            this.isReady = true;
        } else {
            this.isReady = false;
            throw new Exception("Kernel isn't square or size is less than 2");
        }
    }

    public void convolve(Citra buffer) {
        if (isReady) {
            width = buffer.getWidth();
            height = buffer.getHeight();

            int[] content = buffer.getVal();

            int[] out = Arrays.copyOf(content, width * height);
            int sumG;

            for (int i = margin; i < width - margin; ++i) {
                for (int j = margin; j < height - margin; ++j) {
                    sumG = 0;
                    for (int k = -margin, m = kernelSize - 1;
                            k < kernelSize - margin; ++k, --m) {
                        for (int l = -margin, n = kernelSize - 1;
                                l < kernelSize - margin; ++l, --n) {
                            sumG += content[i + k + (j + l) * width] * kernel[m][n];
                        }
                    }
                    out[i + j * width] = (int) (sumG * getConvolutionFactor());
                }
            }
            buffer.setVal(out);

        } else {
            throw new ExceptionInInitializerError(
                    "Kernel is not ready, initialize kernel first");
        }
    }

    public float[] convolveToFloat(Citra buffer) {
        if (isReady) {
            width = buffer.getWidth();
            height = buffer.getHeight();
            int size = width * height;

            float[] content = buffer.getFloatVal();
            float[] out = new float[size];

            float sumG;

            for (int i = margin; i < width - margin; ++i) {
                for (int j = margin; j < height - margin; ++j) {
                    sumG = 0;
                    for (int k = -margin, m = kernelSize - 1;
                            k < kernelSize - margin; ++k, --m) {
                        for (int l = -margin, n = kernelSize - 1;
                                l < kernelSize - margin; ++l, --n) {
                            sumG += content[i + k + (j + l) * width] * kernel[m][n];
                        }
                    }
                    out[i + j * width] = (sumG * getConvolutionFactor());
                }
            }

            int outerWidthMargin = width - margin;
            int outerHeightMargin = height - margin;
            int total = 0;

            for (int j = 0; j < height; ++j) {
                for (int i = 0; i < width; ++i) {
                    if (j < margin || j >= outerHeightMargin || i < margin || i >= outerWidthMargin) {
                        ++total;
                        sumG = 0;
                        for (int k = -margin, m = kernelSize - 1;
                                k < kernelSize - margin; ++k, --m) {
                            for (int l = -margin, n = kernelSize - 1;
                                    l < kernelSize - margin; ++l, --n) {
                                sumG += content[i + j * width] * kernel[m][n];
                            }
                        }
                        out[i + j * width] = (sumG * getConvolutionFactor());
                    }
                }
            }
            return out;
        } else {
            throw new ExceptionInInitializerError(
                    "Kernel is not ready, initialize kernel first");
        }
    }

    /**
     * @return the convolutionFactor
     */
    public float getConvolutionFactor() {
        return convolutionFactor;
    }

    /**
     * @param convolutionFactor the convolutionFactor to set
     */
    public void setConvolutionFactor(float convolutionFactor) {
        this.convolutionFactor = convolutionFactor;
    }

    public static float[][] generateGaussianKernel(float sigma) {
        sigma = sigma < 0.5f ? 0.5f : sigma;
        int width = 2 * Math.round((float) Math.sqrt(-Math.log(0.1) * 2.0 * sigma * sigma)) + 1;

        float kons = 2 * (float) Math.PI * sigma * sigma;

        float[][] out = new float[width][width];
        int k = width / 2;
        for (int i = 0; i < width; ++i) {
            int x = i - k;
            for (int j = 0; j < width; ++j) {
                int y = j - k;
                out[i][j] = kons * (float) Math.exp(-(x * x + y * y) / (2 * sigma * sigma));
            }
        }

        return out;
    }

    public static float[][] generateXGaussianKernel(float sigma) {
        sigma = sigma < 0.5f ? 0.5f : sigma;
        int width = 2 * Math.round((float) Math.sqrt(-Math.log(0.1) * 2.0 * sigma * sigma)) + 1;

        float sigma2 = sigma * sigma;
        float kons = 2 * (float) Math.PI * sigma2 * sigma2;

        float[][] out = new float[width][width];
        int k = width / 2;
        for (int i = 0; i < width; ++i) {
            int x = i - k;
            for (int j = 0; j < width; ++j) {
                int y = j - k;
                out[i][j] = -x / kons * (float) Math.exp(-(x * x + y * y) / (2 * sigma2));
            }
        }

        return out;
    }

    public static float[][] generateYGaussianKernel(float sigma) {
        sigma = sigma < 0.5f ? 0.5f : sigma;
        int width = 2 * Math.round((float) Math.sqrt(-Math.log(0.1) * 2.0 * sigma * sigma)) + 1;

        float sigma2 = sigma * sigma;
        float kons = 2 * (float) Math.PI * sigma2 * sigma2;

        float[][] out = new float[width][width];
        int k = width / 2;
        for (int i = 0; i < width; ++i) {
            int x = i - k;
            for (int j = 0; j < width; ++j) {
                int y = j - k;
                out[i][j] = -y / kons * (float) Math.exp(-(x * x + y * y) / (2 * sigma2));
            }
        }

        return out;
    }

    public static float[][] generateXYGaussianKernel(float sigma) {
        sigma = sigma < 0.5f ? 0.5f : sigma;
        int width = 2 * Math.round((float) Math.sqrt(-Math.log(0.1) * 2.0 * sigma * sigma)) + 1;

        float sigma2 = sigma * sigma;
        float kons = 2 * (float) Math.PI * sigma2 * sigma2 * sigma2;

        float[][] out = new float[width][width];
        int k = width / 2;
        for (int i = 0; i < width; ++i) {
            int x = i - k;
            for (int j = 0; j < width; ++j) {
                int y = j - k;
                out[i][j] = x * y / kons * (float) Math.exp(-(x * x + y * y) / (2 * sigma2));
            }
        }

        return out;
    }

    public static float[][] generateXXGaussianKernel(float sigma) {
        sigma = sigma < 0.5f ? 0.5f : sigma;
        int width = 2 * Math.round((float) Math.sqrt(-Math.log(0.1) * 2.0 * sigma * sigma)) + 1;

        float sigma2 = sigma * sigma;
        float kons = 2 * (float) Math.PI * sigma2 * sigma2 * sigma2;

        float[][] out = new float[width][width];
        int k = width / 2;
        for (int i = 0; i < width; ++i) {
            int x = i - k;
            for (int j = 0; j < width; ++j) {
                int y = j - k;
                out[i][j] = (x * x - sigma2) / kons * (float) Math.exp(-(x * x + y * y) / (2 * sigma2));
            }
        }

        return out;
    }

    public static float[][] generateYYGaussianKernel(float sigma) {
        sigma = sigma < 0.5f ? 0.5f : sigma;
        int width = 2 * Math.round((float) Math.sqrt(-Math.log(0.1) * 2.0 * sigma * sigma)) + 1;

        float sigma2 = sigma * sigma;
        float kons = 2 * (float) Math.PI * sigma2 * sigma2 * sigma2;

        float[][] out = new float[width][width];
        int k = width / 2;
        float sum = 0f;
        for (int i = 0; i < width; ++i) {
            int x = i - k;
            for (int j = 0; j < width; ++j) {
                int y = j - k;
                out[i][j] = (y * y - sigma2) / kons * (float) Math.exp(-(x * x + y * y) / (2 * sigma2));
            }
        }

        return out;
    }

    public void useGaussianKernel(float sigma) {
        sigma = sigma < 0.5f ? 0.5f : sigma;

        int n = 2 * Math.round((float) Math.sqrt(-Math.log(0.1) * 2.0 * sigma * sigma)) + 1;

        float[][] out = new float[n][n];
        int k = n / 2;
        float sum = 0f;
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                out[i][j] = (float) Math.exp(-((i - k) * (i - k) + (j - k) * (j - k)) / (2 * sigma * sigma));
                sum += out[i][j];
            }
        }
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                out[i][j] = out[i][j] / sum;
            }
        }
        this.kernel = out;
        this.kernelSize = n;
        this.convolutionFactor = 1f;
    }
}
