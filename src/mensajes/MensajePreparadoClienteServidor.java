package mensajes;

public class MensajePreparadoClienteServidor extends Mensaje {

	public MensajePreparadoClienteServidor(String origen, String destino) {
		super(MSG_OK_CLI_SER, origen, destino);
	}

	@Override
	public void mostrarInfo() {
		System.out.println("Cliente " + getOrigen() + " preparado con " + getDestino());
	}
}
