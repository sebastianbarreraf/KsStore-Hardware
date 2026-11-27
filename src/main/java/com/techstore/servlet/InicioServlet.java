package com.techstore.servlet;

import com.techstore.dao.CatalogoDAO;
import com.techstore.dao.ProductoDAO;
import com.techstore.modelo.Producto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/** Página de inicio de KsStore: categorías y productos destacados desde MySQL. */
@WebServlet("/inicio")
public class InicioServlet extends HttpServlet {
    private final ProductoDAO productos = new ProductoDAO();
    private final CatalogoDAO catalogo = new CatalogoDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            req.setAttribute("categorias", catalogo.categorias());
            List<Producto> lista = productos.listar(null, null, null, true);
            req.setAttribute("destacados", lista.subList(0, Math.min(4, lista.size())));
            req.getRequestDispatcher("/WEB-INF/jsp/inicio.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("No se pudo cargar la página principal desde la base de datos.", e);
        }
    }
}
