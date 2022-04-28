package mensajes;

public class MensajeConfirmacionListaUsuarios extends Mensaje {
	private String lista;
	
	public MensajeConfirmacionListaUsuarios(String origen, String destino, String lista) {
		super(MSG_CONF_LISTA, origen, destino);
		this.lista = lista;
	}
	
	public String getLista() { return lista; }

	@Override
	public void mostrarInfo() {
		System.out.println("El servidor " + getOrigen() + " confirma lista de usuarios a " + getDestino());
	}

}
