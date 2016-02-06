/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jhalaman.image.filters;

import java.awt.image.BufferedImage;
import jhalaman.Global;
import jhalaman.image.Citra;
import jhalaman.image.Utility;
import jhalaman.ui.component.SimpleViewer;
import org.junit.*;

/**
 *
 * @author Sandy Socrates <sandy_socrates@live.com>
 */
public class RidgeFilterTest {
    
    public RidgeFilterTest() {
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
     * Test of detect method, of class RidgeFilter.
     */
    @Test
    public void testDetect() {
        System.out.println("detect");
        BufferedImage in = Utility.imageLoader(Global.getTestImageAddress());
        RidgeFilter instance = new RidgeFilter();
        SimpleViewer viewer;
        Citra temp = new Citra(in);
        instance.detect(temp, 1.5f);
        viewer = new SimpleViewer(temp.toBufferedImage());
        while (viewer.isShowing()){
        }
        viewer.dispose();
        Hilditch hilditch = new Hilditch();
        hilditch.skeletonize(temp, 130, 255, 0);
        viewer = new SimpleViewer(temp.toBufferedImage());
        while (viewer.isShowing()){
        }
        viewer.dispose();
    }
}
