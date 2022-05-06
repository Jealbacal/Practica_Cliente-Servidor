package servidor;

import usuarios.Usuario;
import monitores.MonitorWR;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Servidor {

    private static final int socket = 999;
    private static Map<String, Usuario> tablaUsuarios = new HashMap<String, Usuario>();
    private static Map<String, ObjectOutputStream> tablaCanales=new HashMap<String,ObjectOutputStream>();
    private static MonitorWR monitor;
    private static ServerSocket ss;

    public static void main(String args[]) throws IOException {

    	ss = new ServerSocket(socket);
    	monitor = new MonitorWR();
    	
    	while (true) {
    		System.out.println("Esperando nueva conexion al servidor");
    		Socket s = ss.accept();
    		(new OyenteCliente(s, tablaUsuarios,tablaCanales, monitor)).start();
    		System.out.println("Nueva conexion realizada con cliente " + s.toString());
    	}
    }
}
