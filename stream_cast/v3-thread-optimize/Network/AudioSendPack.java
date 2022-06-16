package Network;

import Resources.Util;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class AudioSendPack extends Thread {

    DatagramSocket senderSocket;
    InetAddress ipDestino;

    public AudioSendPack() {
        try {
            this.senderSocket = new DatagramSocket();
            this.ipDestino = InetAddress.getByName(Util.NETWORK_ADDRESS);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void sendBuffer(byte[] buffer) {
        DatagramPacket pack = new DatagramPacket(buffer, buffer.length, ipDestino, Util.AUDIO_PORT);

        try {
            senderSocket.send(pack);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            senderSocket.close();
        }
    }
}
