package servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class OyenteCliente extends Thread{
    private Mensaje mensaje;
    private Socket s;

    public OyenteCliente(Socket s){
        this.s=s;

    }
    @Override
    public void run() {
            while(true){
                //todo
                try {
                    ObjectInputStream fin= new ObjectInputStream(s.getInputStream());
                    mensaje = (Mensaje) fin.readObject();

                    switch (mensaje.getTipo()){

                        case 1:
                            //mensaje de confirmacion
                            break;

                        case 2:
                            //mensaje de confirmacion.lista de usuario
                            break;

                        case 3:
                            //mensaje de emitir fichero
                            break;

                        case 4:
                            //mensaje de preparado S->C
                            break;

                        case 5:
                            //mensaje de confirmacion cerrar conex
                            break;

                        case 6:
                            //mensaje de confirmacion
                            break;



                    }

                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
    }


}
