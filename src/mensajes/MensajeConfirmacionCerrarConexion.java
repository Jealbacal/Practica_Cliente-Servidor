package mensajes;

@SuppressWarnings("serial")
public class MensajeConfirmacionCerrarConexion extends Mensaje {

	public MensajeConfirmacionCerrarConexion(String origen, String destino) {
		super(MSG_CONF_CERRAR, origen, destino);
	}

	@Override
	public void mostrarInfo() {
		System.out.println("Conexion cerrada correctamente entre " + getOrigen() + " y " + getDestino());
	}
}
