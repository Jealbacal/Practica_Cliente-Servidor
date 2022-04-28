package mensajes;

public class MensajePedirFichero extends Mensaje {
	private String fichero;

	public MensajePedirFichero(String origen, String destino, String fichero) {
		super(MSG_FICH, origen, destino);
		this.fichero = fichero;
	}
	
	public String getFichero() { return fichero; }

	@Override
	public void mostrarInfo() {
		System.out.println("El cliente " + getOrigen() + " pide fichero a " + getDestino());
	}
}