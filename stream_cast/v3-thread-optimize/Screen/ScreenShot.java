package Screen;

import Resources.Util;
import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

public class ScreenShot extends Thread {

    private Robot robot;
    private BufferedImage image;
    public ScreenShotBlock[] imageBlocks;

    public ScreenShot() {
        try {
            this.robot = new Robot();
            this.imageBlocks = new ScreenShotBlock[Util.RESOLUCAO_X / Util.BLOCK_X * Util.RESOLUCAO_Y / Util.BLOCK_Y];
        } catch (AWTException ex) {
            ex.printStackTrace();
        }
    }

    public boolean hasImage() {
        return image != null;
    }

    @Override
    public void run() {
        while (true) {
            // Captura a tela toda
            image = robot.createScreenCapture(new Rectangle(Util.RESOLUCAO_X, Util.RESOLUCAO_Y));

            slice();
        }
    }

    private void slice() {
        // Divide a imagem em blocos
        int columns = Util.RESOLUCAO_X / Util.BLOCK_X;
        int rows = Util.RESOLUCAO_Y / Util.BLOCK_Y;

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                // Coordinates of source image
                int srcCornerX = Util.BLOCK_X * x;
                int srcCornerY = Util.BLOCK_Y * y;

                // Coordinates of sub-image
                int dstCornerX = Util.BLOCK_X * x + Util.BLOCK_X;
                int dstCornerY = Util.BLOCK_Y * y + Util.BLOCK_Y;

                ScreenShotBlock block = new ScreenShotBlock(image, srcCornerX, srcCornerY, dstCornerX, dstCornerY);

                imageBlocks[y * columns + x] = block;
            }
        }
    }
}
