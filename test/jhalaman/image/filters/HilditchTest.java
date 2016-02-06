/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jhalaman.image.filters;

import jhalaman.Global;
import jhalaman.image.Citra;
import jhalaman.ui.component.SimpleViewer;
import org.junit.*;

/**
 *
 * @author Sandy Socrates <sandy_socrates@live.com>
 */
public class HilditchTest {
    
    public HilditchTest() {
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
     * Test of skeletonize method, of class Hilditch.
     */
    @Test
    public void testSkeletonize() {
        System.out.println("skeletonize");
        Citra citra = new Citra(Global.getTestImageAddress());
        MatrixOperator mo = new MatrixOperator(1.3f);
        mo.convolve(citra);
        Hilditch instance = new Hilditch();
        instance.skeletonize(citra, 127, 0, 255);
        SimpleViewer viewer = new SimpleViewer(citra.toBufferedImage());
        while(viewer.isShowing()) {
        }
        viewer.dispose();
    }
}
