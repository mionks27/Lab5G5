package pe.pucp.tel306.firebox;

import java.io.Serializable;

public class UsuarioDto implements Serializable {

    private String uid;
    private String nombre;
    private String tipo;
    private String capacidad;

    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCapacidad() {
        return capacidad;
    }
    public void setCapacidad(String capacidad) {
        this.capacidad = capacidad;
    }
}
