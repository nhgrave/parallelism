
package cluster;

import middleware.Middleware;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller extends Thread {

    static Map<String,Node> networkNodes;
    static Middleware middleware;
    static Node chief;
    static Node thisIsMe;

    @Override
    public void run() {
        networkNodes = new HashMap<String,Node>();
        middleware = new Middleware(this);

        try {
            String ip = this.middleware.getMyNetworkIp();
            thisIsMe = new Node(ip);

            System.out.println("My ip is " + thisIsMe.ip);
            addNode(thisIsMe);

            System.out.println("Start reciver");

            startRevicePack();

            while (true) {

                reloadTimeout(thisIsMe.ip);

                notifyIamInNetwork();

                listNodes();

                Thread.sleep(5000);

                cleanTimeOutNodes();

                chooseChief();
            }

        } catch (InterruptedException ex) {
            Logger.getLogger(Cluster.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void reloadTimeout(String ip) {
         if (networkNodes.containsKey(ip)) {
            Node node = networkNodes.get(ip);
            node.reloadTime();
        } else {
             addNode(new Node(ip));
         }
    }

    public void addNode(Node node) {
        if (!node.ip.equals("192.168.56.1") && !networkNodes.containsKey(node.ip)) {
            networkNodes.put(node.ip, node);
        }

        chooseChief();
    }

    private void startRevicePack() {
        middleware.startRevicePack();
    }

    private void notifyIamInNetwork() {
        middleware.sendBroadCast();
    }

    private void listNodes() {
        System.out.println("List nodes");


        for (String key : networkNodes.keySet()) {
            System.out.println("*- " + key);
        }

        if (amIChief()) {
            System.out.println("I'm the chief");
        } else {
            System.out.println("The chief is " + chief.ip);
        }
    }

    private void chooseChief() {
        Node node;
        if (chief != null && !networkNodes.containsKey(chief.ip)) {
            chief = null;
        }
        for (String key : networkNodes.keySet()) {
            node = networkNodes.get(key);
            if (chief == null || (!chief.ip.equals(node.ip) && (int) Integer.parseInt(chief.ip.split("\\.")[3]) < (int) Integer.parseInt(node.ip.split("\\.")[3]))) {
                chief = node;
            }
        }
    }

    private void cleanTimeOutNodes() {
        Node node;
        ArrayList<String> removeNodes = new ArrayList();

        for (String key : networkNodes.keySet()) {
            node = networkNodes.get(key);
            if (node.isTimeout()) {
                try {
                    removeNodes.add(key);
                } catch (Exception ex) {
                    // ex;
                }
            }
        }

        if (!removeNodes.isEmpty()) {
            for (String removeNode : removeNodes) {
                try {
                    networkNodes.remove(removeNode);
                    System.out.println("Removed " + removeNode);
                } catch (Exception ex) {
                    // ex;
                }
            }
        }
    }

    public boolean amIChief() {
        return thisIsMe.ip.equals(chief.ip);
    }
}
