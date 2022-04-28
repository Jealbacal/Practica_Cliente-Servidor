package clientes;

import java.net.Socket;

public class OyenteServidor extends Thread{
    private Socket s;
    private int op;


    public OyenteServidor(Socket s,int op){
        this.s=s;
        this.op=op;

    }

    @Override
    public void run(){

        while(true){
            
        }

    }
}
