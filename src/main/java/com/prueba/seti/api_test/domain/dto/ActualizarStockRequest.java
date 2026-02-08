package com.prueba.seti.api_test.domain.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ActualizarStockRequest {

    @NotNull(message = "El nuevoStock es obligatorio")
    @Positive(message = "El nuevoStock debe ser positivo")
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

