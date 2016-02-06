/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jhalaman.image;

import jhalaman.ui.component.SimpleViewer;
import java.awt.image.BufferedImage;
import jhalaman.Global;
import jhalaman.image.filters.MatrixOperator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Sandy Socrates <sandy_socrates@live.com>
 */
public class PreprocessorTest {
    
    public PreprocessorTest() {
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
     * Test of toBinary method, of class Preprocessor.
     */
    @Test
    public void testToBinary() {
        System.out.println("toBinary");
        Citra citra = new Citra("data\\Train Clean\\2153.jpg");
        Preprocessor.toBinary(citra);
        SimpleViewer instance = new SimpleViewer(citra.toBufferedImage());
        while(instance.isShowing()) {
        }
        instance.dispose();
    }
    
}
