package usuarios;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("serial")
public class Usuario implements Serializable{

	private final String ID;
	private final String dirIP;
	private final String rutaInfo;
	private final Semaphore readSem;
	private final Semaphore writeSem;
	private final AtomicInteger readCount;
	private ArrayList<String> listaFich;


	public Usuario(String ID, String dirIP, String rutaInfo, Semaphore readSem, Semaphore writeSem, AtomicInteger readCount) {
		this.ID = ID;
		this.dirIP = dirIP;
		this.rutaInfo = rutaInfo;
		this.readSem = readSem;
		this.writeSem = writeSem;
		this.readCount = readCount;
		
		listaFich = new ArrayList<String>();
		for (File entry : (new File(rutaInfo)).listFiles()) listaFich.add(entry.getName());
	}

	public String getId() { return ID; }

	public String getDireccionIP() { return dirIP; }

	public String getRuta() { return rutaInfo; }
	
	public ArrayList<String> getLista() { return listaFich; }
	
	public Semaphore getReadSem() { return readSem; }

	
	public Semaphore getWriteSem() { return writeSem; }
	
	public AtomicInteger getReadCount() { return readCount; }
	
	public void actualizarLista() {
		listaFich.clear();
		for (File entry : (new File(rutaInfo)).listFiles()) listaFich.add(entry.getName());
	}
	
	public String toString() { 
		return "Usuario " + ID + " con IP " + dirIP + " y datos almacenados en " + rutaInfo;
	}
}