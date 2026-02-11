package co.com.bancolombia.prueba.seti.api_test.domain.dto;

public class SucursalRequest {

    private String nombre;

    private Long franquiciaId;

    public SucursalRequest() {
    }

    public SucursalRequest(String nombre, Long franquiciaId) {
        this.nombre = nombre;
        this.franquiciaId = franquiciaId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getFranquiciaId() {
        return franquiciaId;
    }

    public void setFranquiciaId(Long franquiciaId) {
        this.franquiciaId = franquiciaId;
    }
}
