package mensajes;

public class MensajeConexion extends Mensaje {

	public MensajeConexion(String origen, String destino) {
		super(MSG_CONEX, origen, destino);
	}

	@Override
	public void mostrarInfo() {
		System.out.println("El cliente " + getOrigen() + " pide conexion a " + getDestino());
	}

}
