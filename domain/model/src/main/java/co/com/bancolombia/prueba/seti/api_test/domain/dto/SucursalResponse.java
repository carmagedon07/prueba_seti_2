package co.com.bancolombia.prueba.seti.api_test.domain.dto;

public class SucursalResponse {

    private Long id;
    private String nombre;
    private Long franquiciaId;

    public SucursalResponse() {
    }

    public SucursalResponse(Long id, String nombre, Long franquiciaId) {
        this.id = id;
        this.nombre = nombre;
        this.franquiciaId = franquiciaId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

