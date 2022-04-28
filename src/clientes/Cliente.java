package clientes;

import servidor.OyenteCliente;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Cliente {
    private static final int socket=999;
    private static int op;

    public static void main(String args[]) throws IOException {
        Socket s = new Socket("localhost",socket);
        while (true) {


            (new OyenteServidor(s,op)).start();
        }
    }
}
