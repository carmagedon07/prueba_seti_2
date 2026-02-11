package co.com.bancolombia.prueba.seti.api_test.domain.dto;

public class ProductoRequest {

    private String nombre;

    private Integer stock;

    private Long sucursalId;

    public ProductoRequest() {
    }

    public ProductoRequest(String nombre, Integer stock, Long sucursalId) {
        this.nombre = nombre;
        this.stock = stock;
        this.sucursalId = sucursalId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Long getSucursalId() {
        return sucursalId;
    }

    public void setSucursalId(Long sucursalId) {
        this.sucursalId = sucursalId;
    }
}
