package clientes;

import mensajes.*;
import usuarios.Usuario;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.util.concurrent.Semaphore;

public class OyenteServidor extends Thread{
	private final Socket s;
	private final Usuario usuario;
	private ObjectOutputStream fout;
	private Semaphore sMenu;

	public OyenteServidor(Socket s, Usuario usuario, ObjectOutputStream fout, Semaphore sMenu){
		this.s = s;
		this.usuario = usuario;
		this.fout = fout;
		this.sMenu = sMenu;
	}

	@Override
	public void run(){

		try {
			ObjectInputStream fin = new ObjectInputStream(s.getInputStream());
			
			boolean repeat = true;
			
			while (repeat) {
				Mensaje mensaje = (Mensaje) fin.readObject();

				switch (mensaje.getTipo()) {
				case Mensaje.MSG_CONF_CONEX: //Mensaje de confirmacion de conexion
					MensajeConfirmacionConexion msgConfConex = (MensajeConfirmacionConexion) mensaje;
					msgConfConex.mostrarInfo();

					sMenu.release();

					break;

				case Mensaje.MSG_CONF_LISTA: //Mensaje de confirmacion.lista de usuario
					MensajeConfirmacionListaUsuarios msgCLU = (MensajeConfirmacionListaUsuarios) mensaje;
					msgCLU.mostrarInfo();

					sMenu.release();

					break;

				case Mensaje.MSG_FICH: //Mensaje de emitir fichero
					MensajePedirFichero msgFich = (MensajePedirFichero) mensaje;
					msgFich.mostrarInfo();
					
					ServerSocket ss = new ServerSocket(0);

					fout.writeObject(new MensajePreparadoClienteServidor(usuario.getId(), "Servidor", msgFich.getReceptor(),
							usuario.getId(), ss.getLocalPort(), usuario.getDireccionIP()));
					fout.flush();

					Socket sendSocket = ss.accept();
					String sourceFileName = usuario.getRuta() + File.separator + msgFich.getFichero();
					(new ObjectOutputStream(sendSocket.getOutputStream())).writeObject(new File(sourceFileName));

					ss.close();
					sendSocket.close();

					sMenu.release();

					break;

				case Mensaje.MSG_OK_SER_CLI: //Mensaje de preparado S->C
					MensajePreparadoServidorCliente msgPSC = (MensajePreparadoServidorCliente) mensaje;
					msgPSC.mostrarInfo();

					Socket fileSocket = new Socket(msgPSC.getIPEmisor(), msgPSC.getPuerto());

					File source = (File) (new ObjectInputStream(fileSocket.getInputStream())).readObject();
					String destFileName = usuario.getRuta() + File.separator + source.getName();
					File dest = new File(destFileName);
					
					Files.copy(source.toPath(), dest.toPath());

					fileSocket.close();

					sMenu.release();

					//TODO semaforo/lock evitar acceso a lista durante actualizacion
					usuario.actualizarLista();

					break;

				case Mensaje.MSG_CONF_CERRAR: //Mensaje de confirmacion cerrar conex
					MensajeConfirmacionCerrarConexion msgCCC = (MensajeConfirmacionCerrarConexion) mensaje;
					msgCCC.mostrarInfo();

					repeat = false;
					
					sMenu.release();
					
					break;

				case Mensaje.MSG_ERROR: //Mensaje de error
					MensajeError msgErr = (MensajeError) mensaje;
					msgErr.mostrarInfo();
					
					sMenu.release();
					
					break;

				default: break;
				}
			}
		} catch (IOException | ClassNotFoundException e) { e.printStackTrace(); }
	}
}