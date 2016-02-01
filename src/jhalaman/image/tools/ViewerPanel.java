
package jhalaman.image.tools;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 *
 * @author Sandy Socrates <sandy_socrates@live.com>
 */
public class ViewerPanel extends JPanel{
    private BufferedImage image;
    private int x,y;
    public ViewerPanel(BufferedImage image, int x, int y) {
        super();
        this.image = image;
        this.x = x;
        this.y = y;
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, x, y, null);
    }
}
