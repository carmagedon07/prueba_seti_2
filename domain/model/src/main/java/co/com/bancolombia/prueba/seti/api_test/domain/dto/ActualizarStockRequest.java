package co.com.bancolombia.prueba.seti.api_test.domain.dto;

public class ActualizarStockRequest {

    private Integer nuevoStock;

    public ActualizarStockRequest() {
    }

    public ActualizarStockRequest(Integer nuevoStock) {
        this.nuevoStock = nuevoStock;
    }

    public Integer getNuevoStock() {
        return nuevoStock;
    }

    public void setNuevoStock(Integer nuevoStock) {
        this.nuevoStock = nuevoStock;
    }
}
