import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class EnviadorBlocos {

    Robot robot;
    DatagramSocket senderSocket;
    InetAddress ipDestino;
    byte buffer[];

    public EnviadorBlocos() {
        try {
            this.robot = new Robot();
            this.senderSocket = new DatagramSocket();
            this.ipDestino = InetAddress.getByName("127.0.0.1");
            // buffer para armazenar o bloco da tela para enviar com a pocição X e Y
            this.buffer = new byte[Util.BLOCK_X * Util.BLOCK_Y * 4 + 4 + 4];
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void start() {
        while (true) {
            try {
                // Captura a tela toda
                BufferedImage image = robot.createScreenCapture(new Rectangle(Util.RESOLUCAO_X, Util.RESOLUCAO_Y));

                // Divide em linhas e colunas
                int blocosX = image.getWidth() / Util.BLOCK_X;
                int blocosY = image.getHeight() / Util.BLOCK_Y;

                for (int y = 0; y < blocosY; y++) {
                    for (int x = 0; x < blocosX; x++) {
                        // Initialize the image array with image chunks
                        BufferedImage blockImg = new BufferedImage(Util.BLOCK_X, Util.BLOCK_Y, BufferedImage.TYPE_INT_RGB);
                        // Draws image
                        Graphics2D imgCreator = blockImg.createGraphics();

                        // coordinates of source image
                        int srcCornerX = Util.BLOCK_X * x;
                        int srcCornerY = Util.BLOCK_Y * y;

                        // coordinates of sub-image
                        int dstCornerX = Util.BLOCK_X * x + Util.BLOCK_X;
                        int dstCornerY = Util.BLOCK_Y * y + Util.BLOCK_Y;

                        imgCreator.drawImage(image, 0, 0, Util.BLOCK_X, Util.BLOCK_Y, srcCornerX, srcCornerY, dstCornerX, dstCornerY, null);

                        int aux = 0;
                        for (int pixelY = 0; pixelY < Util.BLOCK_Y; pixelY++) {
                            for (int pixelX = 0; pixelX < Util.BLOCK_X; pixelX++) {
                                int cor = blockImg.getRGB(pixelX, pixelY); // ARGB

                                byte auxBuffer[] = Util.integerToBytes(cor);
                                for (int j = 0; j < auxBuffer.length; j++) {
                                    buffer[aux++] = auxBuffer[j];
                                }
                            }
                        }

                        // bytes do posX
                        byte auxBufferPosX[] = Util.integerToBytes(srcCornerX);
                        for (int j = 0; j < auxBufferPosX.length; j++) {
                            buffer[aux++] = auxBufferPosX[j];
                        }

                        // bytes do posY
                        byte auxBufferPosY[] = Util.integerToBytes(srcCornerY);
                        for (int j = 0; j < auxBufferPosY.length; j++) {
                            buffer[aux++] = auxBufferPosY[j];
                        }

                        sendBuffer(buffer);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void sendBuffer(byte[] buffer) throws IOException {
        DatagramPacket pack = new DatagramPacket(buffer, buffer.length, ipDestino, Util.PORTA);
        senderSocket.send(pack);
    }

    public static void main(String[] args) {
        EnviadorBlocos enviador = new EnviadorBlocos();
        enviador.start();
    }
}
