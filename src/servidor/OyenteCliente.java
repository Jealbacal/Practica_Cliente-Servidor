package servidor;

import mensajes.*;
import usuarios.Usuario;
import monitores.MonitorWR;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.Iterator;
import java.util.Map;

@SuppressWarnings("serial")
public class OyenteCliente extends Thread implements Serializable {
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
			//TODO ARREGLAR ORIGEN/DESTINO DE MENSAJES
			while(repeat){
				Mensaje mensaje = (Mensaje) fin.readObject();

				switch (mensaje.getTipo()) {
				case Mensaje.MSG_CONEX: //Mensaje de conexion
					MensajeConexion msgConex = (MensajeConexion) mensaje;
					msgConex.mostrarInfo();

					Usuario usr = msgConex.getUsuario();

					usr.setFin(fin);
					usr.setFout(fout);

					monitor.requestWrite();
					tablaUsuarios.put(usr.getId(), usr);
					monitor.releaseWrite();

					fout.writeObject(new MensajeConfirmacionConexion("Servidor", usr.getId(), usr.toString()));
					fout.flush();

					break;

				case Mensaje.MSG_LISTA: //Mensaje de lista de usuarios
					MensajeListaUsuarios msgLista = (MensajeListaUsuarios) mensaje;
					msgLista.mostrarInfo();

					monitor.requestRead();
					String lista = tablaUsuarios.toString();
					monitor.releaseRead();

					fout.writeObject(new MensajeConfirmacionListaUsuarios("Servidor", msgLista.getOrigen(), lista));
					fout.flush();

					break;

				case Mensaje.MSG_FICH: //Mensaje de emitir fichero
					MensajePedirFichero msgFich = (MensajePedirFichero) mensaje;
					msgFich.mostrarInfo();

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
						usr2.getFout().writeObject(new MensajePedirFichero("Servidor", usr2.getId(), msgFich.getReceptor(), msgFich.getFichero()));
						usr2.getFout().flush();
					}
					else {
						fout.writeObject(new MensajeError("Servidor", msgFich.getOrigen(), "No se encontro el fichero " + msgFich.getFichero()));
						fout.flush();
					}

					break;

				case Mensaje.MSG_OK_CLI_SER: //Mensaje de preparado S->C
					MensajePreparadoClienteServidor msgCS = (MensajePreparadoClienteServidor) mensaje;
					msgCS.mostrarInfo();

					monitor.requestRead();
					Usuario receptor = tablaUsuarios.get(msgCS.getReceptor());
					monitor.releaseRead();

					receptor.getFout().writeObject(new MensajePreparadoServidorCliente("Servidor", receptor.getId(), msgCS.getEmisor(), msgCS.getPuerto(), msgCS.getIPEmisor()));
					receptor.getFout().flush();

					break;

				case Mensaje.MSG_CERRAR: //Mensaje de cerrar conex
					MensajeCerrarConexion msgCC = (MensajeCerrarConexion) mensaje;
					msgCC.mostrarInfo();

					monitor.requestWrite();
					tablaUsuarios.remove(msgCC.getOrigen());
					monitor.releaseWrite();

					repeat = false;

					fout.writeObject(new MensajeConfirmacionCerrarConexion("Servidor",msgCC.getOrigen()));
					fout.flush();

					break;

				case Mensaje.MSG_ERROR: //Mensaje de error
					MensajeError msgErr = (MensajeError) mensaje;
					msgErr.mostrarInfo();

					break;

				default: break;
				}
			}
		} catch (IOException | ClassNotFoundException | InterruptedException e) { e.printStackTrace(); }
	}
}