package umg.edu.gt;

import com.negocio.models.Cliente;
import com.negocio.models.Pedido;
import com.negocio.models.Producto;
import com.negocio.services.InventarioService;
import com.negocio.services.PedidoService;
import com.negocio.db.DatabaseManager;
// Agrega estos imports al inicio de tu archivo
import java.util.ArrayList;
import java.util.List;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    private static List<Pedido> pedidos = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    private static InventarioService inventarioService;
    private static PedidoService pedidoService;



    public static void main(String[] args) {
        System.out.println("=== FOODNET - Simulador de Negocio de Comida Rápida ===");

        inventarioService = new InventarioService();
        pedidoService = new PedidoService(inventarioService);
        scanner = new Scanner(System.in);

        boolean continuar = true;
        while (continuar) {
            mostrarMenu();

            int opcion = 0;
            try {
                opcion = scanner.nextInt();
                scanner.nextLine(); // limpiar buffer
            } catch (InputMismatchException e) {
                System.out.println("Debe ingresar un número válido.");
                scanner.nextLine();
                continue;
            }

            switch (opcion) {
                case 1:
                    mostrarInventario();
                    break;
                case 2:
                    crearNuevoPedido();
                    break;
                case 3:
                    agregarProductoAPedido();
                    break;
                case 4:
                    mostrarPedidos();
                    break;
                case 5:
                    mostrarIngresos();
                    break;
                case 6:
                    aplicarDescuentoAPedido();
                    break;
                case 7:
                    continuar = false;
                    break;
                case 8:
                    System.out.print("Ingrese el ID del producto a eliminar: ");
                    int idEliminar = scanner.nextInt();
                    scanner.nextLine(); // Limpiar buffer
                    inventarioService.eliminarProductoPorId(idEliminar);
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }

        DatabaseManager.cerrarConexion();
        scanner.close();
        System.out.println("¡Gracias por usar FoodNet!");
    }

    private static void mostrarMenu() {
        System.out.println("\n--- MENÚ PRINCIPAL ---");
        System.out.println("1. Ver inventario");
        System.out.println("2. Crear nuevo pedido");
        System.out.println("3. Agregar producto a pedido");
        System.out.println("4. Ver pedidos");
        System.out.println("5. Ver ingresos totales");
        System.out.println("6. Aplicar descuento a pedido");
        System.out.println("7. Salir");
        System.out.println("8 Eliminar producto ");
        System.out.print("Seleccione una opción: ");
    }

    private static void mostrarInventario() {
        System.out.println("\n--- INVENTARIO ---");
        for (Producto producto : inventarioService.obtenerProductosDisponibles()) {
            System.out.println(producto);
        }
    }

    private static void crearNuevoPedido() {
        System.out.print("Nombre del cliente: ");
        String nombre = scanner.nextLine();
        System.out.print("Teléfono del cliente: ");
        String telefono = scanner.nextLine();

        Cliente cliente = new Cliente(1, nombre, telefono);
        Pedido pedido = pedidoService.crearPedido(cliente);

        System.out.println("Pedido creado con ID: " + pedido.getId());
    }

    private static void agregarProductoAPedido() {
        try {
            System.out.print("ID del pedido: ");
            int pedidoId = scanner.nextInt();
            scanner.nextLine();

            System.out.print("ID del producto: ");
            int productoId = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Cantidad: ");
            int cantidad = scanner.nextInt();
            scanner.nextLine();

            if (pedidoService.agregarProductoAPedido(pedidoId, productoId, cantidad)) {
                System.out.println("Producto agregado exitosamente");
            } else {
                System.out.println("Error al agregar producto");
            }
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida. Intente nuevamente.");
            scanner.nextLine();
        }
    }

    private static void mostrarPedidos() {
        System.out.println("\n--- PEDIDOS ---");
        pedidoService.mostrarPedidos();
    }

    private static void mostrarIngresos() {
        double ingresos = pedidoService.calcularIngresosTotales();
        System.out.println("Ingresos totales: Q" + ingresos);
    }

    private static void aplicarDescuentoAPedido() {
        try {
            // Mostrar lista de pedidos existentes
            System.out.println("\n=== APLICAR DESCUENTO ===");
            System.out.println("Pedidos registrados:");
            if (pedidos.isEmpty()) {
                System.out.println("No hay pedidos registrados.");
                return;
            }

            for (Pedido pedido : pedidos) {
                System.out.printf("ID: %d - Cliente: %s - Total: Q%,.2f%n",
                        pedido.getId(),
                        pedido.getCliente().getNombre(),
                        pedido.getTotal());
            }

            // Solicitar ID del pedido
            System.out.print("\nIngrese el ID del pedido: ");
            int pedidoId = scanner.nextInt();
            scanner.nextLine();

            // Buscar pedido
            Pedido pedido = null;
            for (Pedido p : pedidos) {
                if (p.getId() == pedidoId) {
                    pedido = p;
                    break;
                }
            }

            if (pedido == null) {
                System.out.println("No se encontró un pedido con ese ID.");
                return;
            }

            // Mostrar detalles del pedido
            System.out.println("\nDetalles del pedido:");
            System.out.println("Cliente: " + pedido.getCliente().getNombre());
            System.out.println("Productos:");
            for (Producto producto : pedido.getProductos()) {
                System.out.printf("- %s (Q%,.2f x %d)%n",
                        producto.getNombre(),
                        producto.getPrecio(),
                        producto.getCantidad());
            }
            System.out.printf("Total actual: Q%,.2f%n", pedido.getTotal());

            // Solicitar y validar descuento
            double descuento;
            do {
                System.out.print("\nIngrese el porcentaje de descuento (0-100%): ");
                descuento = scanner.nextDouble();
                scanner.nextLine();

                if (descuento < 0 || descuento > 100) {
                    System.out.println("El descuento debe estar entre 0% y 100%");
                }
            } while (descuento < 0 || descuento > 100);

            // Aplicar descuento
            double totalAnterior = pedido.getTotal();
            pedido.aplicarDescuento(descuento);

            // Mostrar resultados
            System.out.println("\nDescuento aplicado exitosamente:");
            System.out.printf("Total anterior: Q%,.2f%n", totalAnterior);
            System.out.printf("Descuento aplicado: %.2f%%%n", descuento);
            System.out.printf("Nuevo total: Q%,.2f%n", pedido.getTotal());

        } catch (InputMismatchException e) {
            System.out.println("Error: Debe ingresar valores numéricos válidos.");
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("Ocurrió un error inesperado: " + e.getMessage());
        }

    }
}
