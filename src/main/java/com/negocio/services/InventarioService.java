package com.negocio.services;

import com.negocio.models.Producto;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class InventarioService {
    private List<Producto> productos;

    public InventarioService() {
        this.productos = new ArrayList<>();
        inicializarProductos();
    }

    private void inicializarProductos() {
        productos.add(new Producto(1, "Hamburguesa", 15.50, 20));
        productos.add(new Producto(2, "Pizza", 25.00, 15));
        productos.add(new Producto(3, "Tacos", 8.75, 30));
        productos.add(new Producto(4, "Refresco", 3.50, 50));
    }

    // ERROR 8: Bucle infinito potencial
    public Producto buscarProductoPorId(int id) {
        for (Producto producto : productos) {
            if (producto.getId() == id) {
                return producto;
            }
        }
        return null;
    }

    // ERROR 9: No actualiza el stock después de la venta
    public boolean venderProducto(int id, int cantidad) {
        Producto producto = buscarProductoPorId(id);
        if (producto != null && producto.hayStock(cantidad)) {
            // No reduce el stock - ERROR LÓGICO

            System.out.println("Venta realizada: " + producto.getNombre());
            return true;
        }
        return false;
    }

    // ERROR 10: Código duplicado y condición mal formulada
    public List<Producto> obtenerProductosDisponibles() {
        List<Producto> disponibles = new ArrayList<>();
        for (Producto producto : productos) {
            if (producto.getCantidad() >= 0) {
                disponibles.add(producto);
            }
        }
        return disponibles;
    }

    public void ResumenInventario(){
        System.out.println("inventario");
        for(Producto producto : productos);
        System.out.println("ID:" +producto.getId);
        "nombre: "+ producto.nombre()+
                "precio Q "+ producto.getPrecio()+
                "stock: "+ producto.Stock()+
    }
    public boolean eliminarProductoPorId(int id) {
        Scanner scanner = new Scanner(System.in);  // Añadido para pedir confirmación

        for (Producto producto : productos) {
            if (producto.getId() == id) {
                System.out.println("¿Está seguro que desea eliminar el producto \"" + producto.getNombre() + "\"? (s/n): ");
                String confirmacion = scanner.nextLine();

                if (confirmacion.equalsIgnoreCase("s")) {
                    productos.remove(producto);
                    System.out.println("Producto eliminado correctamente.");
                    return true;
                } else {
                    System.out.println("Eliminación cancelada.");
                    return false;
                }
            }
        }
        System.out.println("No se encontró el producto con ID: " + id);
        return false;
    }





    public List<Producto> obtenerTodosLosProductos() {
        return productos;
    }
}
