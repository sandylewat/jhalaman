/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jhalaman.image.filters;

import java.awt.image.BufferedImage;
import jhalaman.image.Utility;

/**
 *
 * @author Sandy Socrates <sandy_socrates@live.com>
 */
public class MatrixOperator {
    public static float[][] GAUSSIAN_KERNEL_5 = {{1,4,7,4,1},
                    {4,16,26,16,4},
                    {7,26,41,26,7},
                    {4,16,26,16,4},
                    {1,4,7,4,1}};

    public static float[][] SOBEL_OPERATOR_KERNEL_X_3 = {{-1,0,1},
                    {-2,0,2},
                    {-1,0,1}};
    public static float[][] SOBEL_OPERATOR_KERNEL_Y_3 = {{1,2,1},
                    {0,0,0},
                    {-1,-2,-1}};
    public static float GAUSSIAN_KERNEL_FACTOR_5 = (float) 1/273;
    public static float SOBEL_OPERATOR_KERNEL_FACTOR = 4.0F;
    private int kernelSize = 0;
    private float[][] kernel;
    private float convolutionFactor = 0f;
    private boolean isReady = false;
    
    public MatrixOperator(float [][] kernel) {
        if (kernel.length > 1 && kernel[0].length == kernel.length) {
            this.kernel = kernel;
            this.kernelSize = kernel.length;
            this.isReady = true;
        }
        else {
            System.out.println("Kernel isn't square or size less than 2");
            throw new ExceptionInInitializerError();
        }
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
            this.isReady = true;
        }
        else {
            this.isReady = false;
            throw new Exception("Kernel isn't square or size is less than 2");
        }
    }
    
    public void convolve(BufferedImage buffer) {
        if(isReady) {
            int sumG, pix;
            if (buffer.getType() == BufferedImage.TYPE_BYTE_BINARY || buffer.getType() == BufferedImage.TYPE_BYTE_GRAY) {
                for (int i = kernelSize/2; i < buffer.getWidth() - kernelSize/2; ++i) {
                    for (int j = kernelSize/2; j < buffer.getHeight() - kernelSize/2; ++j) {
                        sumG = 0;
                        for (int k = -kernelSize/2, m = 0; k < kernelSize - kernelSize/2; ++k, ++m) {
                            for (int l = -kernelSize/2, n = 0; l < kernelSize - kernelSize/2; ++l, ++n) {
                                pix = buffer.getRGB(i+k, j+l);
                                sumG += Utility.B(pix) * kernel[m][n];
                            }
                        }
                        sumG *= getConvolutionFactor();
                        sumG = sumG > 255 ? 255 : sumG < 0 ? 0 : sumG;
                        buffer.setRGB(i, j, Utility.toRGB(sumG, sumG, sumG));
                    }
                }
            }
            else {
                int sumR, sumB;
                for (int i = kernelSize/2; i < buffer.getWidth() - kernelSize/2; ++i) {
                    for (int j = kernelSize/2; j < buffer.getHeight() - kernelSize/2; ++j) {
                        sumR = 0;
                        sumG = 0;
                        sumB = 0;
                        for (int k = -kernelSize/2, m = 0; k < kernelSize - kernelSize/2; ++k, ++m) {
                            for (int l = -kernelSize/2, n = 0; l < kernelSize - kernelSize/2; ++l, ++n) {
                                pix = buffer.getRGB(i+k, j+l);
                                sumR += Utility.R(pix) * kernel[m][n];
                                sumG += Utility.G(pix) * kernel[m][n];
                                sumB += Utility.B(pix) * kernel[m][n];
                            }
                        }
                        sumR *= getConvolutionFactor();
                        sumR = sumR > 255 ? 255 : sumR < 0 ? 0 : sumR;
                        sumG *= getConvolutionFactor();
                        sumG = sumG > 255 ? 255 : sumG < 0 ? 0 : sumG;
                        sumB *= getConvolutionFactor();
                        sumB = sumB > 255 ? 255 : sumB < 0 ? 0 : sumB;                   

                        buffer.setRGB(i, j, Utility.toRGB(sumR, sumG, sumB));
                    }
                }
            }
        }
        else {
            throw new ExceptionInInitializerError("kernel is not ready, initialize kernel first");
        }
    }
    
    public float[][] convolve(float[][] buffer) {
        if(isReady) {
            float[][] out = new float[buffer.length][buffer[0].length];
            float sumG;
            for (int i = kernelSize/2; i < buffer.length - kernelSize/2; ++i) {
                for (int j = kernelSize/2; j < buffer[0].length - kernelSize/2; ++j) {
                    sumG = 0;
                    for (int k = -kernelSize/2, m = 0; k < kernelSize - kernelSize/2; ++k, ++m) {
                        for (int l = -kernelSize/2, n = 0; l < kernelSize - kernelSize/2; ++l, ++n) {
                            sumG += buffer[i+k][j+l] * kernel[m][n];
                        }
                    }
                    sumG *= getConvolutionFactor();
                    out[i][j] = sumG;
                }
            }
            return out;
        }
        else {
            throw new ExceptionInInitializerError("kernel is not ready, initialize kernel first");
        }
    }
    
    public BufferedImage cloneConvolve(BufferedImage buffer) {
        BufferedImage out = Utility.imageClone(buffer);
        if(isReady) {
            int sumG, pix;
            if (out.getType() == BufferedImage.TYPE_BYTE_BINARY || out.getType() == BufferedImage.TYPE_BYTE_GRAY) {
                for (int i = kernelSize/2; i < out.getWidth() - kernelSize/2; ++i) {
                    for (int j = kernelSize/2; j < out.getHeight() - kernelSize/2; ++j) {
                        sumG = 0;
                        for (int k = -kernelSize/2, m = 0; k < kernelSize - kernelSize/2; ++k, ++m) {
                            for (int l = -kernelSize/2, n = 0; l < kernelSize - kernelSize/2; ++l, ++n) {
                                pix = out.getRGB(i+k, j+l);
                                sumG += Utility.B(pix) * kernel[m][n];
                            }
                        }
                        sumG *= getConvolutionFactor();
                        sumG = sumG > 255 ? 255 : sumG < 0 ? 0 : sumG;
                        out.setRGB(i, j, Utility.toRGB(sumG, sumG, sumG));
                    }
                }
            }
            else {
                int sumR, sumB;
                for (int i = kernelSize/2; i < out.getWidth() - kernelSize/2; ++i) {
                    for (int j = kernelSize/2; j < out.getHeight() - kernelSize/2; ++j) {
                        sumR = 0;
                        sumG = 0;
                        sumB = 0;
                        for (int k = -kernelSize/2, m = 0; k < kernelSize - kernelSize/2; ++k, ++m) {
                            for (int l = -kernelSize/2, n = 0; l < kernelSize - kernelSize/2; ++l, ++n) {
                                pix = out.getRGB(i+k, j+l);
                                sumR += Utility.R(pix) * kernel[m][n];
                                sumG += Utility.G(pix) * kernel[m][n];
                                sumB += Utility.B(pix) * kernel[m][n];
                            }
                        }
                        sumR *= getConvolutionFactor();
                        sumR = sumR > 255 ? 255 : sumR < 0 ? 0 : sumR;
                        sumG *= getConvolutionFactor();
                        sumG = sumG > 255 ? 255 : sumG < 0 ? 0 : sumG;
                        sumB *= getConvolutionFactor();
                        sumB = sumB > 255 ? 255 : sumB < 0 ? 0 : sumB;                   

                        out.setRGB(i, j, Utility.toRGB(sumR, sumG, sumB));
                    }
                }
            }
            return out;
        }
        else {
            throw new ExceptionInInitializerError("kernel is not ready, initialize kernel first");
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
}
