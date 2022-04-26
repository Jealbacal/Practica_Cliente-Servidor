package servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {

    private static final int socket=999;

    public static void main(String args[]) throws IOException {

        while(true) {
            ServerSocket serverSockect = new ServerSocket(socket);
            Socket s = serverSockect.accept();
            (new OyenteClient(s)).start();
        }
    }
}
