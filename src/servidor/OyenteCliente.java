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
	private final Socket s;
	private final Map<String, Usuario> tablaUsuarios;
	private final MonitorWR monitor;

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

			boolean repeat = true;

			while(repeat){
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
						String u = it.next();
						
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

					case Mensaje.MSG_OK_CLI_SER:
					//mensaje de preparado S->C
						MensajePreparadoClienteServidor msgCS = (MensajePreparadoClienteServidor) mensaje;

						fout.writeObject(new MensajePreparadoServidorCliente("Servidor", "Receptor", msgCS.getEmisor(), msgCS.getPuerto(), msgCS.getIPEmisor()));
						fout.flush();

					break;

					case Mensaje.MSG_CERRAR:
						//mensaje de cerrar conex
						MensajeCerrarConexion msgCC=(MensajeCerrarConexion) mensaje;

						monitor.requestWrite();
						tablaUsuarios.remove(msgCC.getOrigen());
						monitor.releaseWrite();

						repeat = false;

						fout.writeObject(new MensajeConfirmacionCerrarConexion("Servidor",msgCC.getOrigen()));
						fout.flush();

					break;

					default: break;

				}


			}
		} catch (IOException | ClassNotFoundException | InterruptedException e) { e.printStackTrace(); }
	}


}
