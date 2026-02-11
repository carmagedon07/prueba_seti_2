package co.com.bancolombia.prueba.seti.api_test.domain.dto;

public class FranquiciaRequest {

    private String nombre;

    public FranquiciaRequest() {
    }

    public FranquiciaRequest(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
