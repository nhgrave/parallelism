
package comunications;

import middleware.Middleware;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPRecivePack extends Thread {

    Middleware middleware;

    public UDPRecivePack(Middleware middleware) {
        this.middleware = middleware;
    }

    @Override
    public void run() {
        try {
            DatagramSocket clientSocket = new DatagramSocket(8999);

            while (true) {
                byte[] receiveData = new byte[50];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                clientSocket.receive(receivePacket);

                String senderMessage = new String(receivePacket.getData());
                String clientAddress = new String(receivePacket.getAddress().getHostAddress());

                middleware.processResponseMessage(senderMessage, clientAddress);
            }
        } catch (Exception e) {
            System.out.println("Reciver Erro: " + e.getMessage());
        }
    }
}
