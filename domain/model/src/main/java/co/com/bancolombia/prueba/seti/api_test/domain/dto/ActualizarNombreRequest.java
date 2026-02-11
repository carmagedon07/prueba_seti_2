package co.com.bancolombia.prueba.seti.api_test.domain.dto;

public class ActualizarNombreRequest {

    private String nombre;

    public ActualizarNombreRequest() {
    }

    public ActualizarNombreRequest(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
