package com.prueba.seti.api_test.domain.dto;

import jakarta.validation.constraints.NotBlank;

public class ActualizarNombreRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    public ActualizarNombreRequest() {
    }

    public ActualizarNombreRequest(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}

