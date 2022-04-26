package servidor;

public class OyenteCliente implements Runnable {

	@Override
	public void run() {
		while(true) System.out.println("OyenteCliente en ejecución\n");
	}

}