package mensajes;

public class MensajeConfirmacionListaUsuarios extends Mensaje {

	public MensajeConfirmacionListaUsuarios(String origen, String destino) {
		super(MSG_CONF_LISTA, origen, destino);
	}

	@Override
	public void mostrarInfo() {
		System.out.println("El servidor " + getOrigen() + " confirma lista de usuarios a " + getDestino());
	}

}
