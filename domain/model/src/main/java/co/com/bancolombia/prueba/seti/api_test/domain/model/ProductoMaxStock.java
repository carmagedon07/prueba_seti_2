package co.com.bancolombia.prueba.seti.api_test.domain.model;

public class ProductoMaxStock {

    private Long productoId;
    private String nombreProducto;
    private Integer stock;
    private Long sucursalId;
    private String nombreSucursal;

    public ProductoMaxStock() {
    }

    public ProductoMaxStock(Long productoId, String nombreProducto, Integer stock, Long sucursalId, String nombreSucursal) {
        this.productoId = productoId;
        this.nombreProducto = nombreProducto;
        this.stock = stock;
        this.sucursalId = sucursalId;
        this.nombreSucursal = nombreSucursal;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
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

    public String getNombreSucursal() {
        return nombreSucursal;
    }

    public void setNombreSucursal(String nombreSucursal) {
        this.nombreSucursal = nombreSucursal;
    }
}

