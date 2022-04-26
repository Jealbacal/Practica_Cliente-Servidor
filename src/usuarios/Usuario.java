package usuarios;

public class Usuario {

private final int id;
private final String direccionIP;

public Usuario(int id,String direccionIP){
    this.direccionIP=direccionIP;
    this.id=id;
}

    public int getId() {
        return id;
    }

    public String getDireccionIP() {
        return direccionIP;
    }


}
