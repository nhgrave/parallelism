package Screen;

import Resources.Util;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class ScreenShotBlock {

    private BufferedImage image;
    private  BufferedImage imageBlockBuffer;
    public int srcCornerX;
    public int srcCornerY;
    public int dstCornerX;
    public int dstCornerY;

    public ScreenShotBlock(BufferedImage image, int srcCornerX, int srcCornerY, int dstCornerX, int dstCornerY) {
        this.image = image;
        this.srcCornerX = srcCornerX;
        this.srcCornerY = srcCornerY;
        this.dstCornerX = dstCornerX;
        this.dstCornerY = dstCornerY;

        cropBlock();
    }

    private void cropBlock() {
        // Initialize the image array with image chunks
        imageBlockBuffer = new BufferedImage(Util.BLOCK_X, Util.BLOCK_Y, BufferedImage.TYPE_INT_RGB);

        // Draws image
        Graphics2D imgCreator = imageBlockBuffer.createGraphics();
        imgCreator.drawImage(image, 0, 0, Util.BLOCK_X, Util.BLOCK_Y, srcCornerX, srcCornerY, dstCornerX, dstCornerY, null);
    }

    public int[] getPixels() {
        int[] pixels = new int[Util.BLOCK_X * Util.BLOCK_Y];
        int index = 0;
        for (int pixelY = 0; pixelY < Util.BLOCK_Y; pixelY++) {
            for (int pixelX = 0; pixelX < Util.BLOCK_X; pixelX++) {
                pixels[index++] = imageBlockBuffer.getRGB(pixelX, pixelY); // ARGB
            }
        }
        return pixels;
    }
}
