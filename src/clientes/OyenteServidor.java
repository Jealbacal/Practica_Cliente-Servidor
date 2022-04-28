package clientes;

import mensajes.Mensaje;
import mensajes.MensajeConexion;
import mensajes.MensajeConfirmacionConexion;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class OyenteServidor extends Thread{
    private Mensaje mensaje;
    private Socket s;


    public OyenteServidor(Socket s){
        this.s=s;


    }

    @Override
    public void run(){

         try {
            ObjectInputStream fin = new ObjectInputStream(s.getInputStream());

            while (true) {
                //todo


                mensaje = (Mensaje) fin.readObject();

                switch (mensaje.getTipo()) {

                    case 1:
                        //mensaje de confirmacion de conexion


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
            }
        }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
