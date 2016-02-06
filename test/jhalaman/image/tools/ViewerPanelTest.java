/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jhalaman.image.tools;

import jhalaman.ui.component.ViewerPanel;
import jhalaman.image.Utility;
import java.awt.image.BufferedImage;
import jhalaman.Global;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Sandy Socrates <sandy_socrates@live.com>
 */
public class ViewerPanelTest {
    
    public ViewerPanelTest() {
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
     * Test of paintComponent method, of class ViewerPanel.
     */
    @Test
    public void testPaintComponent() {
        BufferedImage buff = Utility.imageLoader(Global.getTestImageAddress());
        System.out.println("paintComponent");
        ViewerPanel instance = new ViewerPanel(buff, 0, 0);
    }
}
