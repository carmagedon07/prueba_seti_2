package com.prueba.seti.api_test.domain.dto;

import jakarta.validation.constraints.NotBlank;

public class FranquiciaRequest {

    @NotBlank(message = "El nombre es obligatorio")
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

