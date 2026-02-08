package com.prueba.seti.api_test.domain.model;

public class Producto {

    private Long id;
    private String nombre;
    private Integer stock;
    private Long sucursalId;

    public Producto() {
    }

    public Producto(Long id, String nombre, Integer stock, Long sucursalId) {
        this.id = id;
        this.nombre = nombre;
        this.stock = stock;
        this.sucursalId = sucursalId;
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

