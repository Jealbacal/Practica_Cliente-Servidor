package mensajes;

@SuppressWarnings("serial")
public class MensajeListaUsuarios extends Mensaje {

	public MensajeListaUsuarios(String origen, String destino) {
		super(MSG_LISTA, origen, destino);
	}

	@Override
	public void mostrarInfo() {
		System.out.println("El usuario " + getOrigen() + " pide la lista de usuarios a " + getDestino());
	}

}
