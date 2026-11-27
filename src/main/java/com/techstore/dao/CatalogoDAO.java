package com.techstore.dao;

import com.techstore.modelo.Categoria;
import com.techstore.modelo.Marca;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** Listas de apoyo: categorías y marcas. */
public class CatalogoDAO {

    public List<Categoria> categorias() throws SQLException {
        List<Categoria> lista = new ArrayList<>();
        try (Connection c = Conexion.get(); Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT id_categoria, nombre, icono FROM categorias ORDER BY id_categoria")) {
            while (rs.next()) lista.add(new Categoria(rs.getInt(1), rs.getString(2), rs.getString(3)));
        }
        return lista;
    }

    public List<Marca> marcas() throws SQLException {
        List<Marca> lista = new ArrayList<>();
        try (Connection c = Conexion.get(); Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT id_marca, nombre FROM marcas ORDER BY nombre")) {
            while (rs.next()) lista.add(new Marca(rs.getInt(1), rs.getString(2)));
        }
        return lista;
    }
}
