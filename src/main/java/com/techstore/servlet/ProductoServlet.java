package com.techstore.servlet;

import com.techstore.dao.ProductoDAO;
import com.techstore.modelo.Producto;
import com.techstore.util.Util;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

/** Detalle de un producto: /producto?id=3 */
@WebServlet("/producto")
public class ProductoServlet extends HttpServlet {
    private final ProductoDAO productos = new ProductoDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Integer id = Util.entero(req.getParameter("id"));
            Producto p = id == null ? null : productos.buscarPorId(id);
            if (p == null || !p.isActivo()) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Producto no encontrado");
                return;
            }
            req.setAttribute("p", p);
            req.getRequestDispatcher("/WEB-INF/jsp/producto.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
