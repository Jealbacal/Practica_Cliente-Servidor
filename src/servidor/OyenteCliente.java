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
	private  Map<String, Usuario> tablaUsuarios;
	private  Map<String,ObjectOutputStream> tablaCanales;
	private final MonitorWR monitor;


	public OyenteCliente(Socket s, Map<String, Usuario> tablaUsuarios,Map<String,ObjectOutputStream> tablaCanales, MonitorWR monitor){
		this.s = s;
		this.tablaUsuarios = tablaUsuarios;
		this.tablaCanales=tablaCanales;
		this.monitor = monitor;
	}

	@Override
	public void run() {
		try {
			ObjectInputStream fin = new ObjectInputStream(s.getInputStream());
			ObjectOutputStream fout = new ObjectOutputStream(s.getOutputStream());

			boolean repeat = true;

			while (repeat) {
				Mensaje mensaje = (Mensaje) fin.readObject();

				switch (mensaje.getTipo()) {
				case Mensaje.MSG_CONEX: //Mensaje de conexion
					MensajeConexion msgConex = (MensajeConexion) mensaje;
					msgConex.mostrarInfo();

					Usuario usr = msgConex.getUsuario();



					monitor.releaseWrite();
					tablaCanales.put(usr.getId(),fout);
					monitor.releaseWrite();


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
							Usuario usrAux = tablaUsuarios.get(u);
							//Acquire de lectura
							usrAux.getReadSem().acquire();
							if (usrAux.getReadCount().incrementAndGet() == 1) usrAux.getWriteSem().acquire();
							usrAux.getReadSem().release();
							//Lectura
							if (usrAux.getLista().contains(msgFich.getFichero())) {
								usr2 = usrAux;
								stop = true;
							}
							//Release de lectura
							usrAux.getReadSem().acquire();
							if (usrAux.getReadCount().decrementAndGet() == 0) usrAux.getWriteSem().release();
							usrAux.getReadSem().release();
						}
					}
					monitor.releaseRead();

					if (usr2 != null) {

						monitor.requestRead();
						ObjectOutputStream canalEmisor =tablaCanales.get(usr2.getId());
						monitor.releaseRead();
						canalEmisor.writeObject(new MensajePedirFichero("Servidor", usr2.getId(), msgFich.getReceptor(), msgFich.getFichero()));
						canalEmisor.flush();

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

					monitor.requestRead();
					ObjectOutputStream canalReceptor =tablaCanales.get(receptor.getId());
					monitor.releaseRead();

					canalReceptor.writeObject(new MensajePreparadoServidorCliente("Servidor", receptor.getId(), msgCS.getEmisor(), msgCS.getPuerto(), msgCS.getIPEmisor()));
					canalReceptor.flush();

					break;

				case Mensaje.MSG_CERRAR: //Mensaje de cerrar conex
					MensajeCerrarConexion msgCC = (MensajeCerrarConexion) mensaje;
					msgCC.mostrarInfo();

					monitor.requestWrite();
					tablaUsuarios.remove(msgCC.getOrigen());
					monitor.releaseWrite();

					repeat = false;

					fout.writeObject(new MensajeConfirmacionCerrarConexion("Servidor", msgCC.getOrigen()));
					fout.flush();

					break;

				case Mensaje.MSG_ERROR: //Mensaje de error
					MensajeError msgErr = (MensajeError) mensaje;
					msgErr.mostrarInfo();

					break;

				case Mensaje.MSG_CONF_DOWN:
					MensajeConfirmacionDescarga msgCD = (MensajeConfirmacionDescarga) mensaje;
					msgCD.mostrarInfo();

					monitor.requestWrite();
					Usuario usrNew =msgCD.getUsuario();
					tablaUsuarios.remove(msgCD.getOrigen());
					tablaUsuarios.put(msgCD.getOrigen(),usrNew);
					monitor.releaseWrite();


				default: break;
				}
			}
		} catch (IOException | ClassNotFoundException | InterruptedException e) { e.printStackTrace(); }
	}
}