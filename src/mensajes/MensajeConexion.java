package mensajes;

import usuarios.Usuario;

public class MensajeConexion extends Mensaje {
	private Usuario usuario;

	public MensajeConexion(String origen, String destino, Usuario usuario) {
		super(MSG_CONEX, origen, destino);
		this.usuario = usuario;
	}

	public Usuario getUsuario() { return usuario; }

	@Override
	public void mostrarInfo() {
		System.out.println("El cliente " + getOrigen() + " pide conexion a " + getDestino());
	}

}
