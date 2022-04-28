package clientes;

import servidor.OyenteCliente;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Cliente {
    private static final int socket=999;


    public static void main(String args[]) throws IOException {
        Socket s = new Socket("localhost",socket);
        int op;
        op=menu();
        while (op !=5) {


        }
        new OyenteServidor(s).start();

    }

    public static int menu(){

        //todo
        System.out.println(" --------------------------------------");
        System.out.println("|                MENU                  |");
        System.out.println(" --------------------------------------");

        return 0;
    }
}
