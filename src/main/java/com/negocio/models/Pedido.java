package com.negocio.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private int id;
    private Cliente cliente;
    private List<Producto> productos;
    private LocalDateTime fecha;
    private double total;
    private double descuento;

    public Pedido(int id, Cliente cliente) {
        this.id = id;
        this.cliente = cliente;
        this.productos = new ArrayList<>();
        this.fecha = LocalDateTime.now();
        this.total = 0.0;
        this.descuento = 0.0;
    }

    public void agregarProducto(Producto producto) {
        productos.add(producto);
        calcularTotal();
    }

    private void calcularTotal() {
        total = 0;
        for (Producto producto : productos) {
            total += producto.getPrecio() * producto.getCantidad(); // Usar getters
        }
        if (descuento > 0) {
            total = total - (total * descuento / 100);
        }
    }

    public Producto obtenerPrimerProducto() {
        if (productos.isEmpty()) {
            return null;
        }
        return productos.get(0);
    }

    public void aplicarDescuento(double porcentaje) {
        if (porcentaje < 0 || porcentaje > 100) {
            throw new IllegalArgumentException("Porcentaje de descuento inválido");
        }
        this.descuento = porcentaje;
        calcularTotal();
    }

    // Getters
    public int getId() { return id; }
    public Cliente getCliente() { return cliente; }
    public List<Producto> getProductos() { return productos; }
    public LocalDateTime getFecha() { return fecha; }
    public double getTotal() { return total; }
    public double getDescuento() { return descuento; }

    @Override
    public String toString() {
        return "Pedido{" +
                "id=" + id +
                ", cliente=" + cliente.getNombre() +
                ", productos=" + productos.size() +
                ", fecha=" + fecha +
                ", total=" + formatoGTQ(total) +
                ", descuento=" + descuento + "%" +
                '}';
    }

    public String formatoGTQ(double monto) {
        return String.format("Q%,.2f", monto);
    }
}

