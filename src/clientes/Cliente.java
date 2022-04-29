package clientes;

import mensajes.MensajeConexion;
import mensajes.MensajeConfirmacionConexion;
import usuarios.Usuario;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Cliente {
    private static final int puerto =999;


    public static void main(String args[]) throws IOException {

        Usuario usuario=altaUsuario();
        Socket s = new Socket(usuario.getDireccionIP(), puerto);
        usuario.setS(s);

        ObjectOutputStream fout = new ObjectOutputStream(s.getOutputStream());
        fout.writeObject(new MensajeConexion("Servidor","Cliente" ,usuario));
        fout.flush();
        new OyenteServidor(s).start();
        int op;
        op=menu();
        while (op !=3) {

            //segun el menu manda mensajes al servidor
        }


    }

    public static int menu() throws IOException {

        //todo
        System.out.println(" --------------------------------------");
        System.out.println("|                MENU                  |");
        System.out.println(" --------------------------------------");
        int op=-1;
        while(op<=0 || op >3 ){

            System.out.println( "1- Consulta lista de usuarios");
            System.out.println( "2- Pedir fichero");
            System.out.println( "3-Salir");

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            if(Integer.parseInt(br.readLine())==1){
                op=1;
            }
            else if (Integer.parseInt(br.readLine())==2){
                op=2;
            }
            else if (Integer.parseInt(br.readLine())==3){
                op=3;
            }
        }

        return op;
    }

    public  static Usuario altaUsuario() throws IOException {
        BufferedReader clientID = new BufferedReader(new InputStreamReader(System.in));


        System.out.println("¿Cual es tu nombre");
        String nombre= clientID.readLine();

        System.out.println("¿Donde tienes guardados tus Ficheros");
        String ruta=clientID.readLine();

        System.out.println("¿Cual es tu direccionIP?");
        String direcIP=clientID.readLine();

        return new Usuario(nombre,direcIP,ruta);



    }
}
