package com.example.dos_login.model;  // O el nombre del paquete que uses

import java.util.ArrayList;
import java.util.List;
import com.example.dos_login.model.Producto;

public class Carrito {
    private List<Producto> productos;

    public Carrito() {
        productos = new ArrayList<>();
    }

    public void agregarProducto(Producto producto) {
        productos.add(producto);
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void vaciarCarrito() {
        productos.clear();
    }
}

