package com.prueba.seti.api_test.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ProductoRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotNull(message = "El stock es obligatorio")
    @Positive(message = "El stock debe ser positivo")
    private Integer stock;

    @NotNull(message = "La sucursalId es obligatoria")
    @Positive(message = "La sucursalId debe ser positiva")
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

