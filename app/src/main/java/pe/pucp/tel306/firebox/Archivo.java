package pe.pucp.tel306.firebox;

public class Archivo {
    private String nombre;
    private Long sizeEnBytes;
    private Long creationTimeMillis;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getSizeEnBytes() {
        return sizeEnBytes;
    }

    public void setSizeEnBytes(Long sizeEnBytes) {
        this.sizeEnBytes = sizeEnBytes;
    }

    public Long getCreationTimeMillis() {
        return creationTimeMillis;
    }

    public void setCreationTimeMillis(Long creationTimeMillis) {
        this.creationTimeMillis = creationTimeMillis;
    }
}
