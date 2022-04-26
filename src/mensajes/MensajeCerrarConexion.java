package mensajes;

public class MensajeCerrarConexion extends Mensaje {

	public MensajeCerrarConexion(String origen, String destino) {
		super(MSG_CERRAR, origen, destino);
	}

	@Override
	public void mostrarInfo() {
		System.out.println("El cliente " + getOrigen() + " pide cerrar conexion con " + getDestino());
	}
}
