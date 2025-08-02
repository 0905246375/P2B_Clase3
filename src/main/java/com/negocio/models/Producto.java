package com.negocio.models;

public class Producto {
    // Atributos privados (encapsulamiento correcto)
    private int id;
    private String nombre;
    private double precio;
    private int cantidad;

    // Constructor
    public Producto(int id, String nombre, double precio, int cantidad) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
    }

    // Métodos getters (accesores)
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    // Métodos setters (modificadores)
    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    // Método de negocio
    public boolean hayStock(int cantidadSolicitada) {
        return this.cantidad >= cantidadSolicitada;
    }

    // Método toString mejorado
    @Override
    public String toString() {
        return String.format("Producto [ID: %d, Nombre: %s, Precio: Q%,.2f, Cantidad: %d]",
                id, nombre, precio, cantidad);
    }
}

