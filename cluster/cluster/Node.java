
package cluster;

import java.sql.Timestamp;

public class Node {
    public String ip;
    public long time;

    public Node(String ip) {
        this.ip = ip;
        this.reloadTime();
    }

    public int hostID() {
        return (int) Integer.parseInt(ip.split(".")[3]);
    }

    public void reloadTime() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        this.time = timestamp.getTime();
    }

    public boolean isTimeout() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        long curr = timestamp.getTime();
        long timeout = 10000;

        return curr - this.time > timeout;
    }
}
