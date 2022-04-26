package mensajes;

public class MensajeConfirmacionConexion extends Mensaje {

	public MensajeConfirmacionConexion(String origen, String destino) {
		super(MSG_CONF_CONEX, origen, destino);
	}

	@Override
	public void mostrarInfo() {
		System.out.println("El servidor " + getOrigen() + " confirma conexion a " + getDestino());		
	}
}
