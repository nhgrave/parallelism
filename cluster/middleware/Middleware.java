
package middleware;

import Handlers.Messages.InAlive;
import cluster.Controller;
import cluster.Node;
import comunications.UDPRecivePack;
import comunications.UDPSendPack;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Middleware {

    static final String BRODCAST = "255.255.255.255";
    static final String NETWORK = "10.3.20";
    private Controller controller;

    public Middleware(Controller controller) {
        this.controller = controller;
    }

    public void startRevicePack() {
        UDPRecivePack reciver = new UDPRecivePack(this);
        reciver.start();
    }

    public String getMyNetworkIp() {
        String myIp = "";
        try {
            Enumeration<NetworkInterface> nInterfaces = NetworkInterface.getNetworkInterfaces();
            while (nInterfaces.hasMoreElements()) {
                Enumeration<InetAddress> inetAddresses = nInterfaces.nextElement().getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    String address = inetAddresses.nextElement().getHostAddress();
                    if (address.contains(NETWORK)) {
                        myIp = address;
                    }
                }
            }
        } catch (Exception ex) {
               System.out.println(ex.getMessage());
        }
        return myIp;
    }

    public boolean checkNodeIsAlive(Node node) {
        UDPSendPack sendPack = new UDPSendPack(node.ip);
        sendPack.sent(InAlive.getMessage());
        // deve esperar o retorno
        return InAlive.receiveMessage(sendPack.responseMessage);
    }

     public void sendBroadCast() {
        try {
            List<InetAddress> lista = this.listAllBroadcastAddresses();
            UDPSendPack send;

            for(InetAddress add : lista) {
                send = new UDPSendPack(add.getHostAddress());
                send.sent(InAlive.getMessage());
            }

        } catch (SocketException ex) {
            Logger.getLogger(UDPSendPack.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    List<InetAddress> listAllBroadcastAddresses() throws SocketException {
        List<InetAddress> broadcastList = new ArrayList<>();
        Enumeration<NetworkInterface> interfaces
          = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface networkInterface = interfaces.nextElement();

            if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                continue;
            }

            networkInterface.getInterfaceAddresses().stream()
              .map(a -> a.getBroadcast())
              .filter(Objects::nonNull)
              .forEach(broadcastList::add);
        }
        return broadcastList;
    }

    public void processResponseMessage(String message, String clientAddress) {
        String[] messagePayload = message.split("\\|");
        String messageID = messagePayload[0];

        switch (messageID) {
            case InAlive.ID:
                this.controller.reloadTimeout(clientAddress);
                break;
            default:
                System.out.println("Message not implemented");
        }
    }
}
