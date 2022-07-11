
package cluster;

public class Cluster {

    // 1- comunica que está na rede (broadcast)
    // 2- montar uma lista de IPs conecidos
    // 3- eleger um lider (IP)
    // 4- confirmar se o lider está vivo
    // 5- adicionar campo para texto para enviar para o lider escrever na tela

    public static void main(String[] args) {
        Controller controller = new Controller();
        controller.start();
    }
}
