package com.techstore.dao;

import com.techstore.modelo.Producto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** Acceso a datos de productos: listar, buscar, crear, actualizar y eliminar. */
public class ProductoDAO {

    private static final String SELECT =
        "SELECT p.id_producto, p.sku, p.nombre, p.descripcion, p.especificaciones, p.id_categoria, p.id_marca, " +
        "p.precio, p.stock, p.imagen_url, p.activo, c.nombre AS categoria, c.icono, m.nombre AS marca " +
        "FROM productos p JOIN categorias c ON c.id_categoria = p.id_categoria " +
        "JOIN marcas m ON m.id_marca = p.id_marca ";

    /** @param soloActivos true para la tienda (oculta los desactivados), false para el panel admin */
    public List<Producto> listar(Integer idCategoria, String q, String orden, boolean soloActivos) throws SQLException {
        StringBuilder sql = new StringBuilder(SELECT).append("WHERE 1=1 ");
        List<Object> params = new ArrayList<>();
        if (soloActivos) sql.append("AND p.activo = TRUE ");
        if (idCategoria != null) { sql.append("AND p.id_categoria = ? "); params.add(idCategoria); }
        if (q != null && !q.isBlank()) {
            sql.append("AND (p.nombre LIKE ? OR m.nombre LIKE ? OR p.sku LIKE ?) ");
            String like = "%" + q.trim() + "%";
            params.add(like); params.add(like); params.add(like);
        }
        sql.append(switch (orden == null ? "" : orden) {
            case "precio-asc"  -> "ORDER BY p.precio ASC";
            case "precio-desc" -> "ORDER BY p.precio DESC";
            case "nombre"      -> "ORDER BY p.nombre ASC";
            default            -> "ORDER BY p.id_producto DESC";
        });

        List<Producto> lista = new ArrayList<>();
        try (Connection c = Conexion.get(); PreparedStatement ps = c.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) ps.setObject(i + 1, params.get(i));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public Producto buscarPorId(int id) throws SQLException {
        try (Connection c = Conexion.get();
             PreparedStatement ps = c.prepareStatement(SELECT + "WHERE p.id_producto = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    public void crear(Producto p) throws SQLException {
        String sql = "INSERT INTO productos (sku, nombre, descripcion, especificaciones, id_categoria, id_marca, " +
                     "precio, stock, imagen_url, activo) VALUES (?,?,?,?,?,?,?,?,?,?)";
        try (Connection c = Conexion.get(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            enlazar(ps, p);
            ps.executeUpdate();
            try (ResultSet k = ps.getGeneratedKeys()) { if (k.next()) p.setId(k.getInt(1)); }
        }
    }

    public void actualizar(Producto p) throws SQLException {
        String sql = "UPDATE productos SET sku=?, nombre=?, descripcion=?, especificaciones=?, id_categoria=?, " +
                     "id_marca=?, precio=?, stock=?, imagen_url=?, activo=? WHERE id_producto=?";
        try (Connection c = Conexion.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            enlazar(ps, p);
            ps.setInt(11, p.getId());
            ps.executeUpdate();
        }
    }

    /**
     * Elimina el producto. Si ya tiene ventas registradas no se puede borrar
     * (rompería el historial), así que se desactiva.
     * @return true si se eliminó definitivamente, false si solo se desactivó
     */
    public boolean eliminar(int id) throws SQLException {
        try (Connection c = Conexion.get()) {
            try (PreparedStatement ps = c.prepareStatement("DELETE FROM productos WHERE id_producto = ?")) {
                ps.setInt(1, id);
                ps.executeUpdate();
                return true;
            } catch (SQLException e) {
                boolean tieneVentas = e instanceof SQLIntegrityConstraintViolationException || e.getErrorCode() == 1451;
                if (!tieneVentas) throw e;
            }
            try (PreparedStatement ps = c.prepareStatement("UPDATE productos SET activo = FALSE WHERE id_producto = ?")) {
                ps.setInt(1, id);
                ps.executeUpdate();
                return false;
            }
        }
    }

    private void enlazar(PreparedStatement ps, Producto p) throws SQLException {
        ps.setString(1, p.getSku());
        ps.setString(2, p.getNombre());
        ps.setString(3, p.getDescripcion());
        ps.setString(4, p.getEspecificaciones());
        ps.setInt(5, p.getIdCategoria());
        ps.setInt(6, p.getIdMarca());
        ps.setBigDecimal(7, p.getPrecio());
        ps.setInt(8, p.getStock());
        ps.setString(9, p.getImagenUrl());
        ps.setBoolean(10, p.isActivo());
    }

    private Producto mapear(ResultSet rs) throws SQLException {
        Producto p = new Producto();
        p.setId(rs.getInt("id_producto"));
        p.setSku(rs.getString("sku"));
        p.setNombre(rs.getString("nombre"));
        p.setDescripcion(rs.getString("descripcion"));
        p.setEspecificaciones(rs.getString("especificaciones"));
        p.setIdCategoria(rs.getInt("id_categoria"));
        p.setIdMarca(rs.getInt("id_marca"));
        p.setPrecio(rs.getBigDecimal("precio"));
        p.setStock(rs.getInt("stock"));
        p.setImagenUrl(rs.getString("imagen_url"));
        p.setActivo(rs.getBoolean("activo"));
        p.setCategoria(rs.getString("categoria"));
        p.setIcono(rs.getString("icono"));
        p.setMarca(rs.getString("marca"));
        return p;
    }
}
