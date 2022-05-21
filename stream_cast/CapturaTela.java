import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CapturaTela extends Thread {

    Robot robot;
    BufferedImage image;

    public CapturaTela() {
        try {
            this.robot = new Robot();
        } catch (AWTException ex) {
            Logger.getLogger(CapturaTela.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        if (robot != null) {
            while (true) {
                // Captura a tela toda
                image = robot.createScreenCapture(new Rectangle(Util.RESOLUCAO_X, Util.RESOLUCAO_Y));

                try {
                    sleep(30);
                } catch (InterruptedException ex) {
                    Logger.getLogger(CapturaTela.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
