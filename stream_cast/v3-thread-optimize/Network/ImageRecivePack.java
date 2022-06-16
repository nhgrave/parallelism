package Network;

import Resources.Util;
import Screen.ImageBuffer;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class ImageRecivePack extends Thread {

    DatagramSocket receiveSocket;
    private final int IMAGE_BUFFER_SIZE = Util.BLOCK_X * Util.BLOCK_Y * 4 + 4 + 4;

    public ImageRecivePack() {
        try {
            receiveSocket = new DatagramSocket(Util.IMAGE_PORT);
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                // pegar R, G, B, e alfa + 4 pois quero informar o posX e + 4 posY
                byte[] buffer = new byte[IMAGE_BUFFER_SIZE];

                // receiveSocket = new DatagramSocket(Util.IMAGE_PORT);
                DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
                receiveSocket.receive(receivePacket);

                new ImageBuffer(buffer).start();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // receiveSocket.close();
            }
        }
    }
}
