package Network;

import Audio.Recorder;
import Resources.Util;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import main.Client;

public class AudioRecivePack extends Thread {

    DatagramSocket receiveSocket;
    private static final int AUDIO_BUFFER_SIZE = Recorder.BUFFER_SIZE;

    public AudioRecivePack() {
        try {
            receiveSocket = new DatagramSocket(Util.AUDIO_PORT);
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                byte[] buffer = new byte[AUDIO_BUFFER_SIZE];

                // receiveSocket = new DatagramSocket(Util.AUDIO_PORT);
                DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
                receiveSocket.receive(receivePacket);
                Client.audioRecord.playBuffer(buffer);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // receiveSocket.close();
            }
        }
    }
}
