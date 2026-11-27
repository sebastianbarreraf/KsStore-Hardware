package com.techstore.modelo;

public class Categoria {
    private final int id;
    private final String nombre, icono;

    public Categoria(int id, String nombre, String icono) {
        this.id = id; this.nombre = nombre; this.icono = icono;
    }
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getIcono() { return icono; }
}
