package com.techstore.modelo;

public class Marca {
    private final int id;
    private final String nombre;

    public Marca(int id, String nombre) { this.id = id; this.nombre = nombre; }
    public int getId() { return id; }
    public String getNombre() { return nombre; }
}
