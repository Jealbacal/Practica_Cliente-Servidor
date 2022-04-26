package servidor;

import usuarios.Usuario;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Servidor {

    private static final int socket=999;
    private static Map<Integer, Usuario> tablaUsuario = new HashMap<Integer,Usuario>();

    public static void main(String args[]) throws IOException {

        while (true) {
            ServerSocket ss = new ServerSocket(socket);
            Socket s = ss.accept();
            (new OyenteCliente(s,tablaUsuario)).start();
        }
    }
}
