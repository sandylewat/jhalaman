
package jhalaman.ui.component;

import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import jhalaman.image.Utility;

/**
 *
 * @author Sandy Socrates <sandy_socrates@live.com>
 */
public class SimpleViewer extends JFrame{
    public SimpleViewer(BufferedImage buffer)
    {
        super();
        this.setTitle("Simple Viewer");
        this.setBounds(0, 0, buffer.getWidth(), buffer.getHeight());
        ViewerPanel viewer = new ViewerPanel(buffer, 0, 0);
        this.add(viewer);
        this.setVisible(true);
    }
    
    
    public SimpleViewer(String url)
    {
        super();
        BufferedImage buffer = Utility.imageLoader(url);
        this.setTitle("Simple Viewer " + System.getProperty("user.dir") + '\\' + url);
        this.setBounds(0, 0, buffer.getWidth(), buffer.getHeight());
        ViewerPanel viewer = new ViewerPanel(buffer, 0, 0);
        this.add(viewer);
        this.setVisible(true);
    }
}
