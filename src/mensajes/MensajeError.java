package mensajes;

@SuppressWarnings("serial")
public class MensajeError extends Mensaje {
	
	private final String msg;

	public MensajeError(String origen, String destino, String msg) {
		super(MSG_ERROR, origen, destino);
		this.msg = msg;
	}

	@Override
	public void mostrarInfo() {
		System.out.println(msg);
	}

}
