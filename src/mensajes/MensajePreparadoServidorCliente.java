package mensajes;

public class MensajePreparadoServidorCliente extends Mensaje {

	private final String emisor;
	private final int puerto;
	private final String IPEmisor;

	public MensajePreparadoServidorCliente(String origen, String destino, String emisor, int puerto, String IPEmisor) {
		super(MSG_OK_SER_CLI, origen, destino);
		this.emisor = emisor;
		this.puerto = puerto;
		this.IPEmisor = IPEmisor;
	}

	public String getEmisor() { return emisor; }

	public int getPuerto() { return puerto; }

	public String getIPEmisor() { return IPEmisor; }

	@Override
	public void mostrarInfo() {
		System.out.println(getOrigen() + " informa a " + getDestino() + " de que puede conectarse con:");
		System.out.println("Emisor: " + emisor + ". IP: " + IPEmisor + ". Puerto: " + puerto);
	}
}
