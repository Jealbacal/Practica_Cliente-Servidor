package clientes;

import mensajes.*;
import usuarios.Usuario;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.Semaphore;

public class Cliente {
	private static final int PUERTO = 999;
	private static final int OP_LISTA_USUARIOS = 1;
	private static final int OP_DESCARGAR_FICHERO = 2;
	private static final int OP_LISTA_FICHEROS = 3;
	private static final int OP_SALIR = 4;

	public static void main(String args[]) throws IOException, InterruptedException {

		Usuario usuario = altaUsuario();
		
		Socket s = new Socket(usuario.getDireccionIP(), PUERTO);

		Semaphore sMenu = new Semaphore(0, true);
		
		ObjectOutputStream fout = new ObjectOutputStream(s.getOutputStream());
		
		MensajeConexion msgConex = new MensajeConexion(usuario.getId(), "Servidor", usuario);
		
		fout.writeObject(msgConex);
		fout.flush();

		(new OyenteServidor(s, usuario, fout, sMenu)).start();

		int op = 0;

		while (op != 4) {
			sMenu.acquire();
			
			op = menu();
			//segun el menu manda mensajes al servidor

			switch (op) {
			case OP_LISTA_USUARIOS:
				MensajeListaUsuarios msgLU = new MensajeListaUsuarios(usuario.getId(), "Servidor");
				
				fout.writeObject(msgLU);
				fout.flush();
				
				break;

			case OP_DESCARGAR_FICHERO:
				System.out.println("¿Cual es el nombre del fichero?");
				
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				String fichero = br.readLine();
				
				fout.writeObject(new MensajePedirFichero(usuario.getId(), "Servidor", usuario.getId(), fichero));
				fout.flush();
				
				break;

			case OP_LISTA_FICHEROS:
				for (String fich : usuario.getLista())
					System.out.println(fich);

				sMenu.release();
				
				break;

			case OP_SALIR:
				fout.writeObject(new MensajeCerrarConexion(usuario.getId(), "Servidor"));
				fout.flush();
				
				sMenu.acquire();
				
				break;

			default: break;
			}
		}
		
		s.close();
	}

	public static int menu() throws IOException {
		System.out.println("\n --------------------------------------");
		System.out.println("|                MENU                  |");
		System.out.println(" --------------------------------------");
		
		int op = -1;
		while (op <= 0 || op > 4) {
			System.out.println("  1- Consulta lista de usuarios");
			System.out.println("  2- Pedir fichero");
			System.out.println("  3- Ver lista de ficheros");
			System.out.println("  4- Salir");
			System.out.println("Escoge una opcion:");

			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			
			int eleccion = Integer.parseInt(br.readLine());
			if (eleccion == 1) op = 1;
			else if (eleccion == 2) op = 2;
			else if (eleccion == 3) op = 3;
			else if (eleccion == 4) op = 4;
			else System.out.println("\nOpcion no valida, inserta un entero del 1 al 4:");
		}

		return op;
	}

	public  static Usuario altaUsuario() throws IOException {
		BufferedReader clientID = new BufferedReader(new InputStreamReader(System.in));

		System.out.println("¿Cual es tu nombre?");
		String nombre = clientID.readLine();

		System.out.println("\n¿Donde tienes guardados tus ficheros?");
		String ruta = clientID.readLine();

		System.out.println("\n¿Cual es tu direccionIP?");
		String direcIP = clientID.readLine();

		return new Usuario(nombre, direcIP, ruta);
	}
}
