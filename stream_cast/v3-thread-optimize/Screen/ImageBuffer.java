package Screen;

import main.Client;
import Resources.Util;

public class ImageBuffer extends Thread {

    private byte[] buffer;

    public ImageBuffer(byte[] buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        // int do posX
        byte auxPosX[] = new byte[4];
        for (int i = 0; i < 4; i++) {
            // posX estava a 8 bytes atras no fim do pacote
            auxPosX[i] = buffer[buffer.length - 8 + i];
        }
        int posX = Util.bytesToInteger(auxPosX);

        // int do posY
        byte auxPosY[] = new byte[4];
        for (int i = 0; i < 4; i++) {
            // posy estava a 4 bytes atras no fim do pacote
            auxPosY[i] = buffer[buffer.length - 4 + i];
        }
        int posY = Util.bytesToInteger(auxPosY);

        int aux = 0;
        for (int y = 0; y < Util.BLOCK_Y; y++) {
            for (int x = 0; x < Util.BLOCK_X; x++) {
                // int do posY
                byte auxCor[] = new byte[4];
                for (int i = 0; i < 4; i++) {
                    // posy estava a 4 bytes atras no fim do pacote
                    auxCor[i] = buffer[aux++];
                }
                int cor = Util.bytesToInteger(auxCor);

                Client.setPixelColor(posX + x, posY + y, cor);
            }
        }
    }
}
