
public class BusyWaiting {

    static class BusyWaitingThread extends Thread {

        private int qtdeVezesImprimir;
        private String ID;

        public BusyWaitingThread(String ID, int qtde) {
            this.qtdeVezesImprimir = qtde;
            this.ID = ID;
        }

        public void run() {
            for (int i = 0; i < qtdeVezesImprimir; i++) {
                int result = (int) Math.random();
            }
        }

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        // Number o processor cores
        int cores = Runtime.getRuntime().availableProcessors();

        for (int i = 0; i < cores; i++) {
            BusyWaitingThread thread = new BusyWaitingThread("t" + i, Integer.MAX_VALUE / 100);
            thread.setPriority(Thread.MAX_PRIORITY);
            thread.start();
        }

    }

}
