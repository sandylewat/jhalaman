/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jhalaman.image;

import java.awt.image.BufferedImage;
import jhalaman.Global;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Sandy Socrates <sandy_socrates@live.com>
 */
public class UtilityTest {
    
    public UtilityTest() {
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
     * Test of imageLoader method, of class Utility.
     */
    @Test
    public void testImageLoader() {
        System.out.println("imageLoader");
        BufferedImage result = Utility.imageLoader(Global.getTestImageAddress());
        System.out.println("Image height\t: " + result.getHeight());
        System.out.println("Image width\t: " + result.getWidth());

    }

    /**
     * Test of imageClone method, of class Utility.
     */
    @Test
    public void testImageClone() {
        System.out.println("imageClone");
        BufferedImage expResult = Utility.imageLoader(Global.getTestImageAddress());
        BufferedImage result = Utility.imageClone(expResult);
        assertEquals(expResult, result);
    }

    /**
     * Test of R method, of class Utility.
     */
    @Test
    public void testR() {
        System.out.println("R");
        int pix = 0;
        int expResult = 0;
        int result = Utility.R(pix);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of G method, of class Utility.
     */
    @Test
    public void testG() {
        System.out.println("G");
        int pix = 0;
        int expResult = 0;
        int result = Utility.G(pix);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of B method, of class Utility.
     */
    @Test
    public void testB() {
        System.out.println("B");
        int pix = 0;
        int expResult = 0;
        int result = Utility.B(pix);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toRGB method, of class Utility.
     */
    @Test
    public void testToRGB() {
        System.out.println("toRGB");
        int R = 0;
        int G = 0;
        int B = 0;
        int expResult = 0;
        int result = Utility.toRGB(R, G, B);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
