
package Handlers.Messages;

public class InAlive {
    public static final String ID = "1";

    public void InAlive() {

    }

    public static String getMessage() {
        return InAlive.ID + "|1";
    }

    public static boolean receiveMessage(String message) {
        String[] messagePayload = message.split("\\|");
            System.out.println(messagePayload[1]);
        return messagePayload[1].toString().equals("1");
    }
}
