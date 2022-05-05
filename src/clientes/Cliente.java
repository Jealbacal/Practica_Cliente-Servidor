package clientes;

import mensajes.*;
import usuarios.Usuario;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Cliente {
    private static final int puerto =999;

    public static void main(String args[]) throws IOException {

        Usuario usuario = altaUsuario();
        Socket s = new Socket(usuario.getDireccionIP(), puerto);


        ObjectOutputStream fout = new ObjectOutputStream(s.getOutputStream());
        MensajeConexion msg= new MensajeConexion("Cliente","Servidor" ,usuario);
        fout.writeObject(msg);
        fout.flush();

        (new OyenteServidor(s,usuario,fout)).start();

        int op= 0;

        while (op !=4) {
            op=menu();
            //segun el menu manda mensajes al servidor

            switch (op) {
                case 1 -> {

                    MensajeListaUsuarios mlu =new MensajeListaUsuarios("Cliente", "Servidor");
                    fout.writeObject(mlu);
                    fout.flush();
                    break;
                }
                case 2 -> {
                    System.out.println("¿Cual es el nombre del Película?");
                    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                    String fichero = br.readLine();
                    fout.writeObject(new MensajePedirFichero("Cliente", "Servidor", usuario.getId(), fichero));
                    fout.flush();
                    break;
                }
                case 3 -> {
                    for(String pelicula : usuario.getLista())
                        System.out.println(pelicula);
                    break;
                }
                case 4->{
                    fout.writeObject(new MensajeCerrarConexion(usuario.getId(), "Servidor"));
                    fout.flush();
                    break;
                }
                 default ->{
                    break;
                }
            }

        }
        //Creo que aca hay que cerrar el sockect


    }

    public static int menu() throws IOException {

        //todo
        System.out.println(" --------------------------------------");
        System.out.println("|                MENU                  |");
        System.out.println(" --------------------------------------");
        int op=-1;
        while(op<=0 || op >4 ){

            System.out.println( "1- Consulta lista de usuarios");
            System.out.println( "2- Pedir fichero");
            System.out.println( "3- Ver lista de Peliculas");
            System.out.println( "4- Salir");

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            int eleccion=Integer.parseInt(br.readLine());
            if(eleccion==1){
                op=1;
            }
            else if (eleccion==2){

                op=2;
            }
            else if (eleccion==3){
                op=3;
            }
            else if (eleccion==4){
                op=4;
            }
        }

        return op;
    }

    public  static Usuario altaUsuario() throws IOException {
        BufferedReader clientID = new BufferedReader(new InputStreamReader(System.in));


        System.out.println("¿Cual es tu nombre?");
        String nombre= clientID.readLine();

        System.out.println("¿Donde tienes guardados tus Ficheros");
        String ruta=clientID.readLine();

        System.out.println("¿Cual es tu direccionIP?");
        String direcIP=clientID.readLine();

        return new Usuario(nombre,direcIP,ruta);



    }
}
