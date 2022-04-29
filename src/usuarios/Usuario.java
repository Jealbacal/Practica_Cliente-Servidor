package usuarios;

import java.io.File;
import java.net.Socket;
import java.util.ArrayList;

public class Usuario {

	private final String ID;
	private final String dirIP;
	private final String rutaInfo;
	private ArrayList<String> listaFich;
	private  Socket s;

	public void setS(Socket s) {
		this.s = s;
	}

	public Usuario(String ID, String dirIP, String rutaInfo, Socket s) {
		this.ID = ID;
		this.dirIP = dirIP;
		this.rutaInfo = rutaInfo;
		
		for (File entry : (new File(rutaInfo)).listFiles()) {
			listaFich.add(entry.getName());
		}
		
		this.s = s;
	}

	public Usuario(String ID, String dirIP, String rutaInfo) {
		this.ID = ID;
		this.dirIP = dirIP;
		this.rutaInfo = rutaInfo;

		for (File entry : (new File(rutaInfo)).listFiles()) {
			listaFich.add(entry.getName());
		}


	}
	
	public void addFileToList(String fileName) { listaFich.add(fileName); }

	public String getId() { return ID; }

	public String getDireccionIP() { return dirIP; }

	public String getRuta() { return rutaInfo; }
	
	public ArrayList<String> getLista() { return listaFich; }
	
	public Socket getSocket() { return s; }

	public String toString() { 
		return "Usuario " + ID + " con IP " + dirIP + " y datos almacenados en " + rutaInfo + "\n";
	}
}