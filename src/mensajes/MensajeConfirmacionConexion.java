package mensajes;

public class MensajeConfirmacionConexion extends Mensaje {
	private String msgConfirm;

	public MensajeConfirmacionConexion(String origen, String destino, String msgConfirm) {
		super(MSG_CONF_CONEX, origen, destino);
		this.msgConfirm = msgConfirm;
	}
	
	public String getMsg() { return msgConfirm; }

	@Override
	public void mostrarInfo() {
		System.out.println("El servidor " + getOrigen() + " confirma conexion a " + getDestino());		
	}
}
