
package comunications;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
public class UDPSendPack {

    public String destinationAddress;
    public String responseMessage;

    public UDPSendPack(String ipAddress) {
        this.destinationAddress = ipAddress;
    }

    public void sent(String message) {
        try {
            DatagramSocket clientSocket = new DatagramSocket();
            clientSocket.setBroadcast(true);

            InetAddress ipAddress = InetAddress.getByName(destinationAddress);

            byte[] sendData = message.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ipAddress, 8999);
            clientSocket.send(sendPacket);

            clientSocket.close();
        } catch (Exception e) {
            System.out.println("Sender Erro: " + e.getMessage());
        }
    }
}
