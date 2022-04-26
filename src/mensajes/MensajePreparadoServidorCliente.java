package mensajes;

public class MensajePreparadoServidorCliente extends Mensaje {

	public MensajePreparadoServidorCliente(String origen, String destino) {
		super(MSG_OK_SER_CLI, origen, destino);
	}

	@Override
	public void mostrarInfo() {
		System.out.println("Servidor " + getOrigen() + " preparado con " + getDestino());
	}
}
