package mensajes;

public class MensajeCerrarConexion extends Mensaje {

	public MensajeCerrarConexion(String origen, String destino) {
		super(MSG_CERRAR, origen, destino);
	}

	@Override
	public void mostrarInfo() {
		System.out.println("Usuario " + getOrigen() + " quiere cerrar conexion con " + getDestino());
	}
}
