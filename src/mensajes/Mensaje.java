package mensajes;

import java.io.Serializable;

@SuppressWarnings("serial")
public abstract class Mensaje implements Serializable {
	public static final int MSG_CONEX = 0;
	public static final int MSG_CONF_CONEX = 1;
	public static final int MSG_LISTA = 2;
	public static final int MSG_CONF_LISTA = 3;
	public static final int MSG_FICH = 4;
	public static final int MSG_OK_CLI_SER = 5;
	public static final int MSG_OK_SER_CLI = 6;
	public static final int MSG_CERRAR = 7;
	public static final int MSG_CONF_CERRAR = 8;
	
	private int tipo;
	private String origen, destino;
	
	public Mensaje(int tipo, String origen, String destino) {
		this.tipo = tipo;
		this.origen = origen;
		this.destino = destino;
	}
	
	public abstract void mostrarInfo();
	
	public int getTipo() { return tipo; }
	
	public String getOrigen() { return origen; }
	
	public String getDestino() { return destino; }
}
