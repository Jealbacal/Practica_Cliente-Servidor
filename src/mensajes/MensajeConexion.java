package mensajes;

import usuarios.Usuario;

@SuppressWarnings("serial")
public class MensajeConexion extends Mensaje {
	private final Usuario usuario;

	public MensajeConexion(String origen, String destino, Usuario usuario) {
		super(MSG_CONEX, origen, destino);
		this.usuario = usuario;
	}

	public Usuario getUsuario() { return usuario; }

	@Override
	public void mostrarInfo() {
		System.out.println("El cliente " + getOrigen() + " quiere conectarse a " + getDestino());
		System.out.println("Usuario a conectar: " + usuario.toString());
	}

}
