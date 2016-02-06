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
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Sandy Socrates <sandy_socrates@live.com>
 */
public class CannyEdgeTest {
    
    public CannyEdgeTest() {
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
     * Test of detect method, of class CannyEdge.
     */
    @Test
    public void testDetect() {
        System.out.println("detect");
        BufferedImage buffer = Utility.imageLoader(Global.getTestImageAddress());
        CannyEdge instance = new CannyEdge(75,100, false, 1.5f); //0.1, 3.1 scan ; 0.1, 4 scan
        Citra citra = new Citra(buffer);
        BufferedImage out = instance.detect(citra).toBufferedImage();
        SimpleViewer viewer = new SimpleViewer(out);
        while (viewer.isShowing()) {    
        }
        viewer.dispose();
    }
}
