import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class RecebedorBlocos extends JFrame implements Runnable {
    private Thread t = new Thread(this);
    private BufferedImage bi = new BufferedImage(Util.RESOLUCAO_X, Util.RESOLUCAO_Y, BufferedImage.TYPE_INT_ARGB);
    // pegar R, G, B, e alfa + 4 pois quero informar o posX e + 4 posY (posicão sorteada)
    private byte buffer[] = new byte[Util.BLOCK_X * Util.BLOCK_Y * 4 + 4 + 4];

    public RecebedorBlocos() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setVisible(true);
        setTitle("Schoweiro 1.0");
        setSize(Util.RESOLUCAO_X, Util.RESOLUCAO_Y);
        t.start();
    }

    @Override
    public void run() {
        DatagramSocket receiveSocket = null;
        while (true) {
            try {
                receiveSocket = new DatagramSocket(Util.PORTA);
                DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
                receiveSocket.receive(receivePacket);

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

                        bi.setRGB(posX + x, posY + y, cor);
                    }
                }

                Thread.sleep(10);
                repaint();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                receiveSocket.close();
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(bi, 0, 0, this);
    }

    public static void main(String[] args) {
        new RecebedorBlocos();
    }
}
