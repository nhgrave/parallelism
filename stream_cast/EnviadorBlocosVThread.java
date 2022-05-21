
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EnviadorBlocosVThread {

    DatagramSocket senderSocket;
    InetAddress ipDestino;
    CapturaTela capturaTela;

    public EnviadorBlocosVThread() {
        try {
            this.senderSocket = new DatagramSocket();
            this.ipDestino = InetAddress.getByName("127.0.0.1");
        } catch (SocketException | UnknownHostException ex) {
            Logger.getLogger(EnviadorBlocosVThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public class EnviadorBlocosThread extends Thread {

        int srcCornerX, srcCornerY, dstCornerX, dstCornerY;
        byte[] tBuffer;

        public EnviadorBlocosThread(int srcCornerX, int srcCornerY, int dstCornerX, int dstCornerY) {
            this.srcCornerX = srcCornerX;
            this.srcCornerY = srcCornerY;
            this.dstCornerX = dstCornerX;
            this.dstCornerY = dstCornerY;
            // buffer para armazenar o bloco da tela para enviar com a pocição X e Y
            this.tBuffer = new byte[Util.BLOCK_X * Util.BLOCK_Y * 4 + 4 + 4];
        }

        @Override
        public void run() {
            processImageBlock();

            try {
                sendBuffer();
            } catch (IOException ex) {
                Logger.getLogger(EnviadorBlocosVThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        private void processImageBlock() {
            // Initialize the image array with image chunks
            BufferedImage blockImg = new BufferedImage(Util.BLOCK_X, Util.BLOCK_Y, BufferedImage.TYPE_INT_RGB);
            // Draws image
            Graphics2D imgCreator = blockImg.createGraphics();

            imgCreator.drawImage(capturaTela.image, 0, 0, Util.BLOCK_X, Util.BLOCK_Y, srcCornerX, srcCornerY, dstCornerX, dstCornerY, null);

            int aux = 0;
            for (int pixelY = 0; pixelY < Util.BLOCK_Y; pixelY++) {
                for (int pixelX = 0; pixelX < Util.BLOCK_X; pixelX++) {
                    int cor = blockImg.getRGB(pixelX, pixelY); // ARGB

                    byte auxBuffer[] = Util.integerToBytes(cor);
                    for (int j = 0; j < auxBuffer.length; j++) {
                        tBuffer[aux++] = auxBuffer[j];
                    }
                }
            }

            // bytes do posX
            byte auxBufferPosX[] = Util.integerToBytes(srcCornerX);
            for (int j = 0; j < auxBufferPosX.length; j++) {
                tBuffer[aux++] = auxBufferPosX[j];
            }

            // bytes do posY
            byte auxBufferPosY[] = Util.integerToBytes(srcCornerY);
            for (int j = 0; j < auxBufferPosY.length; j++) {
                tBuffer[aux++] = auxBufferPosY[j];
            }
        }

        private void sendBuffer() throws IOException {
            DatagramPacket pack = new DatagramPacket(tBuffer, tBuffer.length, ipDestino, Util.PORTA);
            senderSocket.send(pack);
        }
    }

    private void sendImageBlocks() throws IOException {
        // Divide em linhas e colunas
        int blocosX = capturaTela.image.getWidth() / Util.BLOCK_X;
        int blocosY = capturaTela.image.getHeight() / Util.BLOCK_Y;

        for (int y = 0; y < blocosY; y++) {
            for (int x = 0; x < blocosX; x++) {
                // coordinates of source image
                int srcCornerX = Util.BLOCK_X * x;
                int srcCornerY = Util.BLOCK_Y * y;

                // coordinates of sub-image
                int dstCornerX = Util.BLOCK_X * x + Util.BLOCK_X;
                int dstCornerY = Util.BLOCK_Y * y + Util.BLOCK_Y;

                new EnviadorBlocosThread(srcCornerX, srcCornerY, dstCornerX, dstCornerY).start();
            }
        }
    }

    private void start() {
        capturaTela = new CapturaTela();
        capturaTela.start();

        while (true) {
            try {
                if (capturaTela.image != null) {
                    sendImageBlocks();
                } else {
                    Thread.sleep(100);
                }
            } catch (IOException | InterruptedException ex) {
                Logger.getLogger(EnviadorBlocosVThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void main(String[] args) {
        new EnviadorBlocosVThread().start();
    }
}
