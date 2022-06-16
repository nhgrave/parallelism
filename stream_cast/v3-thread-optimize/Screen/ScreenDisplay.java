package Screen;

import Resources.Util;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import static java.lang.Thread.sleep;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class ScreenDisplay extends JFrame implements Runnable {

    private Thread t = new Thread(this);
    private BufferedImage bi = new BufferedImage(Util.RESOLUCAO_X, Util.RESOLUCAO_Y, BufferedImage.TYPE_INT_ARGB);

    public ScreenDisplay() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setVisible(true);
        setTitle("ScreenCast");
        setSize(Util.RESOLUCAO_X, Util.RESOLUCAO_Y);
    }

    public void start() {
        t.start();
    }

    public void setPixelColor(int posX, int posY, int cor) {
        bi.setRGB(posX, posY, cor);
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(bi, 0, 0, this);
    }

    @Override
    public void run() {
        while (true) {
            // Monta a tela toda
            repaint();

            try {
                sleep(40);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

}
