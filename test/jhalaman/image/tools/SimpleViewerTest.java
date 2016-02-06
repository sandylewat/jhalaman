/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jhalaman.image.tools;

import jhalaman.ui.component.SimpleViewer;
import java.awt.image.BufferedImage;
import jhalaman.Global;
import jhalaman.image.Utility;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Sandy Socrates <sandy_socrates@live.com>
 */
public class SimpleViewerTest {
    
    public SimpleViewerTest() {
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

    @Test
    public void testSomeMethod() {
        BufferedImage buff = Utility.imageLoader(Global.getTestImageAddress());
        SimpleViewer instance = new SimpleViewer(buff);
        while(instance.isShowing()) {
        }
        instance.dispose();
    }
}
