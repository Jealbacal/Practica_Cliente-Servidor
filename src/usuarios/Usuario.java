package usuarios;

import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class Usuario implements Serializable{

	private final String ID;
	private final String dirIP;
	private final String rutaInfo;
	private ArrayList<String> listaFich;
	private ObjectOutputStream fout;
	private ObjectInputStream fin;

	public Usuario(String ID, String dirIP, String rutaInfo) {
		this.ID = ID;
		this.dirIP = dirIP;
		this.rutaInfo = rutaInfo;
		listaFich = new ArrayList<String>();
		for (File entry : (new File(rutaInfo)).listFiles()) {
			listaFich.add(entry.getName());
		}
	}

	public ObjectOutputStream getFout() { return fout; }

	public void setFout(ObjectOutputStream fout) { this.fout = fout; }

	public ObjectInputStream getFin() { return fin; }

	public void setFin(ObjectInputStream fin) { this.fin = fin; }

	public void actualizarLista() {
		listaFich = new ArrayList<String>();
		for (File entry : (new File(rutaInfo)).listFiles()) {
			listaFich.add(entry.getName());
		}
	}

	public String getId() { return ID; }

	public String getDireccionIP() { return dirIP; }

	public String getRuta() { return rutaInfo; }
	
	public ArrayList<String> getLista() { return listaFich; }
	
	public String toString() { 
		return "Usuario " + ID + " con IP " + dirIP + " y datos almacenados en " + rutaInfo + "\n";
	}
}