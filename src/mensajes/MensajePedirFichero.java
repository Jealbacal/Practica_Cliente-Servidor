package mensajes;

public class MensajePedirFichero extends Mensaje {

	public MensajePedirFichero(String origen, String destino) {
		super(MSG_FICH, origen, destino);
	}

	@Override
	public void mostrarInfo() {
		System.out.println("El cliente " + getOrigen() + " pide fichero a " + getDestino());
	}
}