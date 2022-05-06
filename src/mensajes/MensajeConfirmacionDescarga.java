package mensajes;

import usuarios.Usuario;

public class MensajeConfirmacionDescarga extends Mensaje{

    private Usuario usuario;
    private String emisor;
    public MensajeConfirmacionDescarga(String origen, String destino, Usuario usuario,String emisor){
        super(MSG_CONF_DOWN,origen,destino);
        this.usuario=usuario;
        this.emisor=emisor;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    @Override
    public void mostrarInfo() {
        System.out.println("El usuario " + getOrigen()+" ha descargado correctamente el fichero de : "+emisor);
    }
}
