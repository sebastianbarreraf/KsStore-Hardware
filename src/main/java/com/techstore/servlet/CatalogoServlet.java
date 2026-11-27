package com.techstore.servlet;

import com.techstore.dao.CatalogoDAO;
import com.techstore.dao.ProductoDAO;
import com.techstore.util.Util;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

/** Página principal: catálogo con filtro por categoría, búsqueda y orden. */
@WebServlet("/productos")
public class CatalogoServlet extends HttpServlet {
    private final ProductoDAO productos = new ProductoDAO();
    private final CatalogoDAO catalogo = new CatalogoDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Integer idCat = Util.entero(req.getParameter("cat"));
            String q = req.getParameter("q");
            String orden = req.getParameter("orden");

            req.setAttribute("productos", productos.listar(idCat, q, orden, true));
            req.setAttribute("categorias", catalogo.categorias());
            req.setAttribute("catSel", idCat);
            req.setAttribute("q", q);
            req.setAttribute("orden", orden);
            req.getRequestDispatcher("/WEB-INF/jsp/catalogo.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
