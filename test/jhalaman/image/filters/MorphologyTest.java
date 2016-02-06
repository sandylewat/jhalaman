/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jhalaman.image.filters;

import jhalaman.Global;
import jhalaman.image.Citra;
import jhalaman.image.Preprocessor;
import jhalaman.ui.component.SimpleViewer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Sandy Socrates <sandy_socrates@live.com>
 */
public class MorphologyTest {
    
    public MorphologyTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testErode() {
        Citra tes = new Citra(Global.getTestImageAddress());
        Morphology m = new Morphology();
        Preprocessor.toBinary(tes);
        SimpleViewer sv = new SimpleViewer(tes.toBufferedImage());
        while(sv.isShowing()) {}
        m.dilate(tes, 1);
        System.out.println(tes.countIntensity(255)/m.getTotalErosion(tes));
    }
}
