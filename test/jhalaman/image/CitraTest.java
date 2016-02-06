/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jhalaman.image;

import java.awt.image.BufferedImage;
import jhalaman.Global;
import jhalaman.ui.component.SimpleViewer;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Sandy Socrates <sandy_socrates@live.com>
 */
public class CitraTest {
    
    public CitraTest() {
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
     * Test of toBufferedImage method, of class Citra.
     */
    @Test
    public void testToBufferedImage() {
        System.out.println("toBufferedImage");
        BufferedImage buff = Utility.imageLoader(Global.getTestImageAddress());
        //Preprocessor.toGrayscale(buff);
        Citra instance = new Citra(buff);
        SimpleViewer out = new SimpleViewer(instance.toBufferedImage());
        while(out.isShowing()) {
        }
        out.dispose();
    }

    /**
     * Test of getHeight method, of class Citra.
     */
    @Test
    public void testGetHeight() {
        System.out.println("getHeight");
        Citra instance = null;
        int expResult = 0;
        int result = instance.getHeight();
        assertEquals(expResult, result);
    }

    /**
     * Test of setHeight method, of class Citra.
     */
    @Test
    public void testSetHeight() {
        System.out.println("setHeight");
        int height = 0;
        Citra instance = null;
        instance.setHeight(height);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getWidth method, of class Citra.
     */
    @Test
    public void testGetWidth() {
        System.out.println("getWidth");
        Citra instance = null;
        int expResult = 0;
        int result = instance.getWidth();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setWidth method, of class Citra.
     */
    @Test
    public void testSetWidth() {
        System.out.println("setWidth");
        int width = 0;
        Citra instance = null;
        instance.setWidth(width);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getVal method, of class Citra.
     */
    @Test
    public void testGetVal_0args() {
        System.out.println("getVal");
        Citra instance = null;
        int[] expResult = null;
        int[] result = instance.getVal();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getVal method, of class Citra.
     */
    @Test
    public void testGetVal_int() {
        System.out.println("getVal");
        int offset = 0;
        Citra instance = null;
        int expResult = 0;
        int result = instance.getVal(offset);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getFloatVal method, of class Citra.
     */
    @Test
    public void testGetFloatVal() {
        System.out.println("getFloatVal");
        Citra instance = null;
        float[] expResult = null;
        float[] result = instance.getFloatVal();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setVal method, of class Citra.
     */
    @Test
    public void testSetVal_intArr() {
        System.out.println("setVal");
        int[] val = null;
        Citra instance = null;
        instance.setVal(val);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setVal method, of class Citra.
     */
    @Test
    public void testSetVal_int_int() {
        System.out.println("setVal");
        int offset = 0;
        int val = 0;
        Citra instance = null;
        instance.setVal(offset, val);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setVal method, of class Citra.
     */
    @Test
    public void testSetVal_floatArr() {
        System.out.println("setVal");
        float[] val = null;
        Citra instance = null;
        instance.setVal(val);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }


}
