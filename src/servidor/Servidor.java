package servidor;

import usuarios.Usuario;
import monitores.MonitorWR;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Servidor implements KeyListener {

    private static final int socket=999;
    private static Map<String, Usuario> tablaUsuario = new HashMap<String, Usuario>();
    private static MonitorWR monitor;
    private static ServerSocket ss;

    public static void main(String args[]) throws IOException {

    	ss = new ServerSocket(socket);
    	monitor = new MonitorWR();

    	while (true) {
    		System.out.println("Esperando nueva conexion al servidor");
    		Socket s = ss.accept();
    		(new OyenteCliente(s, tablaUsuario, monitor)).start();
    		System.out.println("Nueva conexion realizada con cliente " + s.toString());
    	}
    }

    @Override
	public void keyPressed(KeyEvent arg0) {
		if (arg0.getKeyCode() == KeyEvent.VK_ESCAPE) {
			try {
				ss.close();
				System.exit(0);
			} catch (IOException e) { e.printStackTrace(); }
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {}

	@Override
	public void keyTyped(KeyEvent arg0) {}
}
