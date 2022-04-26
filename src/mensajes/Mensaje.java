package mensajes;

public abstract class Mensaje {
	protected static final int MSG_CONEX = 0;
	protected static final int MSG_CONF_CONEX = 1;
	protected static final int MSG_LISTA = 2;
	protected static final int MSG_CONF_LISTA = 3;
	protected static final int MSG_FICH = 4;
	protected static final int MSG_OK_CLI_SER = 5;
	protected static final int MSG_OK_SER_CLI = 6;
	protected static final int MSG_CERRAR = 7;
	protected static final int MSG_CONF_CERRAR = 8;
	
	private int tipo;
	private String origen, destino;
	
	public Mensaje(int tipo, String origen, String destino) {
		this.tipo = tipo;
		this.origen = origen;
		this.destino = destino;
	}
	
	// TODO
	public abstract void mostrarInfo();
	
	public int getTipo() { return tipo; }
	
	public String getOrigen() { return origen; }
	
	public String getDestino() { return destino; }
}
