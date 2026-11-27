package com.techstore.dao;

import com.techstore.modelo.Pedido;
import java.math.BigDecimal;
import java.sql.*;

/** Registro y consulta de pedidos (compra directa de un producto). */
public class PedidoDAO {

    private static final BigDecimal ENVIO_GRATIS_DESDE = new BigDecimal("500000");
    private static final BigDecimal COSTO_ENVIO = new BigDecimal("15000");

    public static BigDecimal envio(BigDecimal subtotal) {
        return subtotal.compareTo(ENVIO_GRATIS_DESDE) >= 0 ? BigDecimal.ZERO : COSTO_ENVIO;
    }

    /**
     * Crea el pedido en una transacción: valida stock, guarda pedido y detalle.
     * El trigger de la base de datos descuenta el stock.
     * @throws IllegalStateException si el producto no está disponible o no hay stock suficiente
     */
    public int crear(Pedido ped, int idProducto, int cantidad) throws SQLException {
        try (Connection c = Conexion.get()) {
            c.setAutoCommit(false);
            try {
                BigDecimal precio;
                try (PreparedStatement ps = c.prepareStatement(
                        "SELECT precio, stock FROM productos WHERE id_producto = ? AND activo = TRUE FOR UPDATE")) {
                    ps.setInt(1, idProducto);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) throw new IllegalStateException("El producto ya no está disponible.");
                        int stock = rs.getInt("stock");
                        if (stock < cantidad) {
                            throw new IllegalStateException(stock == 0
                                ? "El producto se agotó."
                                : "Stock insuficiente: solo quedan " + stock + " unidades.");
                        }
                        precio = rs.getBigDecimal("precio");
                    }
                }

                BigDecimal subtotal = precio.multiply(BigDecimal.valueOf(cantidad));
                BigDecimal envio = envio(subtotal);
                String estado = "TRANSFERENCIA".equals(ped.getMetodoPago()) ? "PENDIENTE" : "PAGADO";

                int idPedido;
                try (PreparedStatement ps = c.prepareStatement(
                        "INSERT INTO pedidos (cliente_nombre, cliente_email, cliente_telefono, ciudad, direccion, " +
                        "metodo_pago, estado, subtotal, costo_envio, total) VALUES (?,?,?,?,?,?,?,?,?,?)",
                        Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, ped.getClienteNombre());
                    ps.setString(2, ped.getClienteEmail());
                    ps.setString(3, ped.getClienteTelefono());
                    ps.setString(4, ped.getCiudad());
                    ps.setString(5, ped.getDireccion());
                    ps.setString(6, ped.getMetodoPago());
                    ps.setString(7, estado);
                    ps.setBigDecimal(8, subtotal);
                    ps.setBigDecimal(9, envio);
                    ps.setBigDecimal(10, subtotal.add(envio));
                    ps.executeUpdate();
                    try (ResultSet k = ps.getGeneratedKeys()) { k.next(); idPedido = k.getInt(1); }
                }

                try (PreparedStatement ps = c.prepareStatement(
                        "INSERT INTO detalle_pedido (id_pedido, id_producto, cantidad, precio_unitario) VALUES (?,?,?,?)")) {
                    ps.setInt(1, idPedido);
                    ps.setInt(2, idProducto);
                    ps.setInt(3, cantidad);
                    ps.setBigDecimal(4, precio);
                    ps.executeUpdate();
                }
                c.commit();
                return idPedido;
            } catch (Exception e) {
                c.rollback();
                throw e;
            }
        }
    }

    public Pedido buscar(int id) throws SQLException {
        String sql = "SELECT p.*, d.cantidad, pr.nombre AS producto FROM pedidos p " +
                     "JOIN detalle_pedido d ON d.id_pedido = p.id_pedido " +
                     "JOIN productos pr ON pr.id_producto = d.id_producto WHERE p.id_pedido = ?";
        try (Connection c = Conexion.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                Pedido p = new Pedido();
                p.setId(rs.getInt("id_pedido"));
                p.setClienteNombre(rs.getString("cliente_nombre"));
                p.setClienteEmail(rs.getString("cliente_email"));
                p.setClienteTelefono(rs.getString("cliente_telefono"));
                p.setCiudad(rs.getString("ciudad"));
                p.setDireccion(rs.getString("direccion"));
                p.setMetodoPago(rs.getString("metodo_pago"));
                p.setEstado(rs.getString("estado"));
                p.setSubtotal(rs.getBigDecimal("subtotal"));
                p.setEnvio(rs.getBigDecimal("costo_envio"));
                p.setTotal(rs.getBigDecimal("total"));
                p.setCantidad(rs.getInt("cantidad"));
                p.setProducto(rs.getString("producto"));
                return p;
            }
        }
    }
}
