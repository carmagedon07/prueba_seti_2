package com.prueba.seti.api_test.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class SucursalRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotNull(message = "La franquiciaId es obligatoria")
    @Positive(message = "La franquiciaId debe ser positiva")
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

