/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jhalaman.image.filters;

import java.awt.image.BufferedImage;
import jhalaman.Global;
import jhalaman.image.Citra;
import jhalaman.image.Preprocessor;
import jhalaman.image.Utility;
import jhalaman.ui.component.SimpleViewer;
import org.junit.*;

/**
 *
 * @author Sandy Socrates <sandy_socrates@live.com>
 */
public class MatrixOperatorTest {
    float[][] kernel;
    MatrixOperator instance;
    BufferedImage buffer;
    
    public MatrixOperatorTest() {
        instance = new MatrixOperator(1f);
        buffer =  Utility.imageLoader(Global.getTestImageAddress());
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        
    }
    
    @Before
    public void setUp() {
        
    }
    
    @After
    public void tearDown() {
        
    }

    /**
     * Test of getKernel method, of class MatrixOperator.
     */
    @Test
    public void testGetKernel() {
        System.out.println("getKernel");
        float[][] result = instance.getKernel();
        for (int i = 0; i < result.length; ++i) {
            for (int j = 0; j < result[i].length; ++j) {
                System.out.print(result[i][j] + " ");
            }
            System.out.println();
        }
    }

    /**
     * Test of setKernel method, of class MatrixOperator.
     * @throws java.lang.Exception
     */
    @Test
    public void testSetKernel() throws Exception {
        System.out.println("setKernel");
        float[][] kernelB = {{1,2,3,4,5},
                {4,9,12,9,4},
                {5,12,15,12,5},
                {4,9,12,9,4},
                {2,4,5,4,2}};
        instance.setKernel(kernelB);

        System.out.println("modify kernel");
        float[][] result = instance.getKernel();
        for (int i = 0; i < result.length; ++i) {
            for (int j = 0; j < result[i].length; ++j) {
                System.out.print(result[i][j] + " ");
            }
            System.out.println();
        }
        
        System.out.println("return original kernel");
        instance.setKernel(MatrixOperator.generateGaussianKernel(1f));
        result = instance.getKernel();
        for (int i = 0; i < result.length; ++i) {
            for (int j = 0; j < result[i].length; ++j) {
                System.out.print(result[i][j] + " ");
            }
            System.out.println();
        }
    }

    /**
     * Test of convolve method, of class MatrixOperator.
     */
    @Test
    public void testConvolve() {
        System.out.println("convolve");
        instance.useGaussianKernel(1f);
        Preprocessor.toGrayscale(buffer);
        Citra a = new Citra(buffer);
        instance.convolve(a);
        SimpleViewer viewer = new SimpleViewer(buffer);
        SimpleViewer viewer2 = new SimpleViewer(a.toBufferedImage());
        while(viewer.isShowing() || viewer2.isShowing()) {
        }
        viewer.dispose();
        viewer2.dispose();

    }

    /**
     * Test of getConvolutionFactor method, of class MatrixOperator.
     */
    @Test
    public void testGetConvolutionFactor() {
        System.out.println("getConvolutionFactor");
        System.out.println("convolution factor = " + instance.getConvolutionFactor());
    }

    /**
     * Test of setConvolutionFactor method, of class MatrixOperator.
     */
    @Test
    public void testSetConvolutionFactor() {
        System.out.println("setConvolutionFactor");
        instance.useGaussianKernel(1f);
        System.out.println("current convolutionFactor = " + instance.getConvolutionFactor());
        System.out.println("set to 1");
        float convolutionFactor = 1.0F;
        instance.setConvolutionFactor(convolutionFactor);
        System.out.println("current convolutionFactor = " + instance.getConvolutionFactor());
    }
    
    /**
     * Test of generateGaussianKernel method, of class MatrixOperator.
     */
    @Test
    public void testGenerateGaussianKernel() {
        System.out.println("generateGaussianKernel");
        instance.useGaussianKernel(1f);
        float[][] out = instance.getKernel();
        for (int i = 0; i < out.length; ++i) {
            for (int j = 0; j < out.length; ++j) {
                System.out.print(out[i][j] + " ");
            }
            System.out.println("");
        }
        System.out.println("current convolutionFactor = " + instance.getConvolutionFactor());
    }
}
