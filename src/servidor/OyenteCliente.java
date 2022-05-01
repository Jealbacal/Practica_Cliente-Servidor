package servidor;

import mensajes.*;
import usuarios.Usuario;
import monitores.MonitorWR;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Iterator;
import java.util.Map;

public class OyenteCliente extends Thread{
	private Socket s;
	private Map<String, Usuario> tablaUsuarios;
	private MonitorWR monitor;

	public OyenteCliente(Socket s, Map<String, Usuario> tablaUsuarios, MonitorWR monitor){
		this.s = s;
		this.tablaUsuarios = tablaUsuarios;
		this.monitor = monitor;
	}

	@Override
	public void run() {
		try {
			ObjectInputStream fin = new ObjectInputStream(s.getInputStream());
			ObjectOutputStream fout = new ObjectOutputStream(s.getOutputStream());

			while(true){
				Mensaje mensaje = (Mensaje) fin.readObject();

				switch (mensaje.getTipo()) {

				case Mensaje.MSG_CONEX: //mensaje de conexion
					MensajeConexion msgConex = (MensajeConexion) mensaje;
					
					Usuario usr = msgConex.getUsuario();
					
					monitor.requestWrite();
					tablaUsuarios.put(usr.getId(), usr);
					monitor.releaseWrite();

					fout.writeObject(new MensajeConfirmacionConexion("Servidor", usr.getId(), usr.toString()));
					fout.flush();

					break;

				case Mensaje.MSG_LISTA: //mensaje de lista de usuarios
					MensajeListaUsuarios msgLista = (MensajeListaUsuarios) mensaje;
					
					monitor.requestRead();
					String lista = tablaUsuarios.toString();
					monitor.releaseRead();
					
					fout.writeObject(new MensajeConfirmacionListaUsuarios("Servidor", msgLista.getOrigen(), lista));
					fout.flush();

					break;

				case Mensaje.MSG_FICH: //mensaje de emitir fichero
					MensajePedirFichero msgFich = (MensajePedirFichero) mensaje;
					
					monitor.requestRead();
					Iterator<String> it = tablaUsuarios.keySet().iterator();
					
					Usuario usr2 = null;
					Boolean stop = false;
					while (it.hasNext() && !stop) {
						String u = (String) it.next();
						
						if (u != msgFich.getOrigen()) {
							if (tablaUsuarios.get(u).getLista().contains(msgFich.getFichero())) {
								usr2 = tablaUsuarios.get(u);
								stop = true;
							}
						}
					}
					monitor.releaseRead();
					
					if (usr2 != null) {
						ObjectOutputStream fout2 = new ObjectOutputStream((usr2.getSocket()).getOutputStream());
						fout2.writeObject(new MensajePedirFichero("Servidor", usr2.getId(), msgFich.getFichero()));
						fout2.flush();
					}
					
					break;

					case Mensaje.MSG_OK_SER_CLI:
					//mensaje de preparado S->C
						MensajePreparadoClienteServidor msgCS=(MensajePreparadoClienteServidor) mensaje;

						fout.writeObject(new MensajePreparadoServidorCliente("Servidor","Cliente",msgCS.getUsuarioID(),msgCS.getPuerto(),msgCS.getIP()));
						fout.flush();




					break;

				case 10:
					//mensaje de confirmacion cerrar conex
					break;

				case 11:
					//mensaje de confirmacion
					break;



				}


			}
		} catch (IOException | ClassNotFoundException | InterruptedException e) { e.printStackTrace(); }
	}


}
