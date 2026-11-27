package com.techstore.servlet;

import com.techstore.dao.PedidoDAO;
import com.techstore.dao.ProductoDAO;
import com.techstore.modelo.Pedido;
import com.techstore.modelo.Producto;
import com.techstore.util.Util;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Set;

/**
 * Flujo de compra directa:
 *   GET  /pagar?id=3&cantidad=1  -> formulario de pago
 *   POST /pagar                  -> registra el pedido y redirige
 *   GET  /confirmacion?id=10     -> resumen del pedido
 *
 * El pago es SIMULADO: no se piden ni guardan datos de tarjeta.
 * Para cobros reales hay que integrar una pasarela (Wompi, PayU, Mercado Pago...).
 */
@WebServlet({"/pagar", "/confirmacion"})
public class CompraServlet extends HttpServlet {
    private static final Set<String> METODOS =
        Set.of("TARJETA_CREDITO", "TARJETA_DEBITO", "PSE", "NEQUI", "TRANSFERENCIA");

    private final ProductoDAO productos = new ProductoDAO();
    private final PedidoDAO pedidos = new PedidoDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            if ("/confirmacion".equals(req.getServletPath())) {
                Integer id = Util.entero(req.getParameter("id"));
                Pedido ped = id == null ? null : pedidos.buscar(id);
                if (ped == null) { resp.sendError(HttpServletResponse.SC_NOT_FOUND); return; }
                req.setAttribute("pedido", ped);
                req.getRequestDispatcher("/WEB-INF/jsp/confirmacion.jsp").forward(req, resp);
                return;
            }

            Producto p = cargarProducto(req);
            if (p == null) { resp.sendError(HttpServletResponse.SC_NOT_FOUND); return; }
            if (p.isAgotado()) { resp.sendRedirect(req.getContextPath() + "/producto?id=" + p.getId()); return; }
            mostrarFormulario(req, resp, p, cantidad(req, p), null);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Producto p = cargarProducto(req);
            if (p == null) { resp.sendError(HttpServletResponse.SC_NOT_FOUND); return; }
            int cantidad = cantidad(req, p);

            Pedido ped = new Pedido();
            ped.setClienteNombre(Util.limpiar(req.getParameter("nombre")));
            ped.setClienteEmail(Util.limpiar(req.getParameter("email")));
            ped.setClienteTelefono(Util.limpiar(req.getParameter("telefono")));
            ped.setCiudad(Util.limpiar(req.getParameter("ciudad")));
            ped.setDireccion(Util.limpiar(req.getParameter("direccion")));
            ped.setMetodoPago(Util.limpiar(req.getParameter("metodo")));

            String error = validar(ped);
            if (error == null) {
                try {
                    int idPedido = pedidos.crear(ped, p.getId(), cantidad);
                    resp.sendRedirect(req.getContextPath() + "/confirmacion?id=" + idPedido);
                    return;
                } catch (IllegalStateException e) {
                    error = e.getMessage();
                }
            }
            mostrarFormulario(req, resp, p, cantidad, error);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private Producto cargarProducto(HttpServletRequest req) throws SQLException {
        Integer id = Util.entero(req.getParameter("id"));
        Producto p = id == null ? null : productos.buscarPorId(id);
        return p != null && p.isActivo() ? p : null;
    }

    private int cantidad(HttpServletRequest req, Producto p) {
        Integer c = Util.entero(req.getParameter("cantidad"));
        int n = c == null ? 1 : Math.max(1, c);
        return p.getStock() > 0 ? Math.min(n, p.getStock()) : n;
    }

    private String validar(Pedido p) {
        if (p.getClienteNombre().isEmpty() || p.getClienteEmail().isEmpty() || p.getClienteTelefono().isEmpty()
                || p.getCiudad().isEmpty() || p.getDireccion().isEmpty()) {
            return "Completa todos los datos de contacto y envío.";
        }
        if (!p.getClienteEmail().contains("@")) return "Ingresa un correo electrónico válido.";
        if (!METODOS.contains(p.getMetodoPago())) return "Selecciona un método de pago.";
        return null;
    }

    private void mostrarFormulario(HttpServletRequest req, HttpServletResponse resp, Producto p,
                                   int cantidad, String error) throws ServletException, IOException {
        BigDecimal subtotal = p.getPrecio().multiply(BigDecimal.valueOf(cantidad));
        BigDecimal envio = PedidoDAO.envio(subtotal);
        req.setAttribute("p", p);
        req.setAttribute("cantidad", cantidad);
        req.setAttribute("subtotal", Util.pesos(subtotal));
        req.setAttribute("envio", envio.signum() == 0 ? "Gratis" : Util.pesos(envio));
        req.setAttribute("total", Util.pesos(subtotal.add(envio)));
        req.setAttribute("error", error);
        req.getRequestDispatcher("/WEB-INF/jsp/pagar.jsp").forward(req, resp);
    }
}
