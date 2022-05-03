package mensajes;

import java.net.Socket;

public class MensajePreparadoClienteServidor extends Mensaje {

	private final String emisor;
	private final int puerto;
	private final String IPEmisor;

	public MensajePreparadoClienteServidor(String origen, String destino, String emisor, int puerto, String IPEmisor) {
		super(MSG_OK_CLI_SER, origen, destino);
		this.emisor = emisor;
		this.puerto = puerto;
		this.IPEmisor = IPEmisor;
	}

	public String getEmisor() { return emisor; }

	public String getIPEmisor() { return IPEmisor; }

	public int getPuerto() {
		return puerto;
	}

	@Override
	public void mostrarInfo() {
		System.out.println("Cliente " + getOrigen() + " preparado con " + getDestino());
	}
}
