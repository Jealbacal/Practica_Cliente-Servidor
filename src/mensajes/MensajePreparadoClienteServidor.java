package mensajes;

import java.net.Socket;

public class MensajePreparadoClienteServidor extends Mensaje {

	private String UsuarioID;
	private int puerto;
	private String IP;


	public MensajePreparadoClienteServidor(String origen, String destino,String usuarioID,int puerto,String IP) {
		super(MSG_OK_CLI_SER, origen, destino);
		this.IP=IP;
		this.puerto=puerto;
		this.UsuarioID=usuarioID;
	}

	public String getUsuarioID() {
		return UsuarioID;
	}

	public int getPuerto() {
		return puerto;
	}

	public String getIP() {
		return IP;
	}

	@Override
	public void mostrarInfo() {
		System.out.println("Cliente " + getOrigen() + " preparado con " + getDestino());
	}
}
