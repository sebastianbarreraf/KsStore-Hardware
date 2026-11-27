package com.techstore.modelo;

import com.techstore.util.Util;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Producto {
    private int id, idCategoria, idMarca, stock;
    private String sku = "", nombre = "", descripcion = "", especificaciones = "", imagenUrl;
    private String categoria, icono, marca;           // vienen del JOIN
    private BigDecimal precio = BigDecimal.ZERO;
    private boolean activo = true;

    public int getId() { return id; }                 public void setId(int v) { id = v; }
    public String getSku() { return sku; }            public void setSku(String v) { sku = v; }
    public String getNombre() { return nombre; }      public void setNombre(String v) { nombre = v; }
    public String getDescripcion() { return descripcion; }  public void setDescripcion(String v) { descripcion = v; }
    public String getEspecificaciones() { return especificaciones; }  public void setEspecificaciones(String v) { especificaciones = v; }
    public int getIdCategoria() { return idCategoria; }  public void setIdCategoria(int v) { idCategoria = v; }
    public int getIdMarca() { return idMarca; }          public void setIdMarca(int v) { idMarca = v; }
    public BigDecimal getPrecio() { return precio; }     public void setPrecio(BigDecimal v) { precio = v; }
    public int getStock() { return stock; }              public void setStock(int v) { stock = v; }
    public String getImagenUrl() { return imagenUrl; }   public void setImagenUrl(String v) { imagenUrl = v; }
    public boolean isActivo() { return activo; }         public void setActivo(boolean v) { activo = v; }
    public String getCategoria() { return categoria; }   public void setCategoria(String v) { categoria = v; }
    public String getIcono() { return icono; }           public void setIcono(String v) { icono = v; }
    public String getMarca() { return marca; }           public void setMarca(String v) { marca = v; }

    // ---- Ayudas para las vistas (JSP) ----
    public String getPrecioTxt() { return Util.pesos(precio); }
    public boolean isAgotado() { return stock <= 0; }
    public boolean isStockBajo() { return stock > 0 && stock <= 4; }

    /** Convierte las líneas "Clave: valor" en pares para mostrar en una tabla. */
    public List<String[]> getSpecs() {
        List<String[]> lista = new ArrayList<>();
        if (especificaciones == null) return lista;
        for (String linea : especificaciones.split("\\R")) {
            if (linea.isBlank()) continue;
            int i = linea.indexOf(':');
            lista.add(i < 0 ? new String[]{linea.trim(), ""}
                            : new String[]{linea.substring(0, i).trim(), linea.substring(i + 1).trim()});
        }
        return lista;
    }
}
