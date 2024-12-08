package com.example.dos_login.model;
public class Producto {
    private String nombre;
    private double precio;
    private int cantidad; // Para almacenar la cantidad de un producto
    private int id; // Para almacenar un identificador único si es necesario

    // Constructor con los parámetros adicionales
    public Producto(int id, String nombre, double precio, int cantidad) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
    }

    // Getters para obtener los valores de los atributos
    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public int getId() {
        return id;
    }
}
