package clientes;

import locks.LockBakery;
import mensajes.*;
import usuarios.Usuario;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;

public class OyenteServidor extends Thread{
	private final Socket s;
	private final Usuario usuario;
	private final ObjectOutputStream fout;
	private final LockBakery lock;

	public OyenteServidor(Socket s, Usuario usuario, ObjectOutputStream fout, LockBakery lock){
		this.s = s;
		this.usuario = usuario;
		this.fout = fout;
		this.lock = lock;
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

					lock.releaseLock(Cliente.ID_OS);

					break;

				case Mensaje.MSG_CONF_LISTA: //Mensaje de confirmacion.lista de usuario
					MensajeConfirmacionListaUsuarios msgCLU = (MensajeConfirmacionListaUsuarios) mensaje;
					msgCLU.mostrarInfo();

					lock.releaseLock(Cliente.ID_OS);

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

					//Acquire de escritura
					usuario.getWriteSem().acquire();
					//Escritura
					usuario.actualizarLista();



					//Release de escritura
					usuario.getWriteSem().release();

					fout.reset();
					fout.writeObject(new MensajeConfirmacionDescarga(usuario.getId(), "Servidor", usuario,
							msgPSC.getEmisor()));

					fout.flush();

					System.out.println("Descarga completada, puede encontrar el fichero en su carpeta " +
							"o ver su lista de ficheros desde el menu.");



					lock.releaseLock(Cliente.ID_OS);

					break;

				case Mensaje.MSG_CONF_CERRAR: //Mensaje de confirmacion cerrar conex
					MensajeConfirmacionCerrarConexion msgCCC = (MensajeConfirmacionCerrarConexion) mensaje;
					msgCCC.mostrarInfo();

					repeat = false;

					lock.releaseLock(Cliente.ID_OS);
					
					break;

				case Mensaje.MSG_ERROR: //Mensaje de error
					MensajeError msgErr = (MensajeError) mensaje;
					msgErr.mostrarInfo();

					lock.releaseLock(Cliente.ID_OS);
					
					break;

				default: break;
				}
			}
		} catch (IOException | ClassNotFoundException | InterruptedException e) { e.printStackTrace(); }
	}
}