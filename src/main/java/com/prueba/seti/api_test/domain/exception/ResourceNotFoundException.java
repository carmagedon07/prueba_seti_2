package com.prueba.seti.api_test.domain.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resource, Object id) {
        super(resource + " con ID " + id + " no encontrado");
    }
}

