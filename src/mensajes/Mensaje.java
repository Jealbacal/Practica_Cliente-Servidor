package mensajes;

public abstract class Mensaje {
	
	private int tipo;
	private String origen, destino;
	
	public Mensaje(int tipo, String origen, String destino) {
		this.tipo = tipo;
		this.origen = origen;
		this.destino = destino;
	}
	
	// TODO
	public abstract void enviaMensaje();
	
	public int getTipo() { return tipo; }
	
	public String getOrigen() { return origen; }
	
	public String getDestino() { return destino; }
}
