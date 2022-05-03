package clientes;

import mensajes.*;
import usuarios.Usuario;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

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

            boolean repeat = true;

             while (repeat) {
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

                        Socket sendSocket = ss.accept();
                        String sourceFileName = usuario.getRuta() + File.separator + msgFich.getFichero();
                        (new ObjectOutputStream(sendSocket.getOutputStream())).writeObject(new File(sourceFileName));

                        ss.close();
                        sendSocket.close();

                        break;

                    case Mensaje.MSG_OK_SER_CLI:
                        //mensaje de preparado S->C
                        MensajePreparadoServidorCliente msgPSC = (MensajePreparadoServidorCliente) mensaje;

                        Socket fileSocket = new Socket(msgPSC.getIPEmisor(), msgPSC.getPuerto());

                        File source = (File) (new ObjectInputStream(fileSocket.getInputStream())).readObject();
                        String destFileName = usuario.getRuta() + File.separator + source.getName();
                        File dest = new File(destFileName);
                        Files.copy(source.toPath(), dest.toPath());

                        fileSocket.close();

                        break;

                    case Mensaje.MSG_CONF_CERRAR:
                        //mensaje de confirmacion cerrar conex
                        MensajeConfirmacionCerrarConexion msgCCC = (MensajeConfirmacionCerrarConexion) mensaje;

                        msgCCC.mostrarInfo();

                        repeat = false;

                        break;

                    default: break;


                }
            }
        }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
