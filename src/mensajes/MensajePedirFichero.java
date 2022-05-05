package mensajes;

@SuppressWarnings("serial")
public class MensajePedirFichero extends Mensaje {
	
	private final String receptor;
	private final String fichero;

	public MensajePedirFichero(String origen, String destino, String receptor, String fichero) {
		super(MSG_FICH, origen, destino);
		this.receptor = receptor;
		this.fichero = fichero;
	}
	
	public String getReceptor() { return receptor; }
	
	public String getFichero() { return fichero; }

	@Override
	public void mostrarInfo() {
		System.out.println("El usuario " + getOrigen() + " pide el fichero " + fichero + " a " + getDestino());
	}
}