package clientes;

import mensajes.*;
import usuarios.Usuario;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class OyenteServidor extends Thread{
    private final Socket s;
    private final Usuario usuario;

    public OyenteServidor(Socket s, Usuario usuario){
        this.s = s;
        this.usuario = usuario;
    }

    @Override
    public void run(){

         try {
            ObjectInputStream fin = new ObjectInputStream(s.getInputStream());
            ObjectOutputStream fout = new ObjectOutputStream(s.getOutputStream());

             while (true) {
                Mensaje mensaje = (Mensaje) fin.readObject();

                switch (mensaje.getTipo()) {

                    case Mensaje.MSG_CONF_CONEX:
                        //mensaje de confirmacion de conexion
                        MensajeConfirmacionConexion msgConfConex = (MensajeConfirmacionConexion) mensaje;

                        System.out.println(msgConfConex.getMsg());

                        break;

                    case Mensaje.MSG_CONF_LISTA:
                        //mensaje de confirmacion.lista de usuario
                        MensajeConfirmacionListaUsuarios msgCLU = (MensajeConfirmacionListaUsuarios) mensaje;

                        System.out.println(msgCLU.getLista());

                        break;

                    case Mensaje.MSG_FICH:
                        //mensaje de emitir fichero
                        MensajePedirFichero msgFich = (MensajePedirFichero) mensaje;

                        int port = 400;

                        ServerSocket ss = new ServerSocket(port);

                        fout.writeObject(new MensajePreparadoClienteServidor(usuario.getId(), "Servidor", usuario.getId(), port, usuario.getDireccionIP()));
                        fout.flush();

                        break;

                    case 12:
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
