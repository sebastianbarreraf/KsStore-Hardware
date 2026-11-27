package com.techstore.servlet;

import com.techstore.dao.CatalogoDAO;
import com.techstore.dao.ProductoDAO;
import com.techstore.modelo.Producto;
import com.techstore.util.Util;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * Panel de administración de productos (CRUD).
 *   GET  /admin/productos            -> lista
 *   GET  /admin/productos/nuevo      -> formulario vacío
 *   GET  /admin/productos/editar?id= -> formulario con datos
 *   POST /admin/productos/guardar    -> crea o actualiza
 *   POST /admin/productos/eliminar   -> elimina (o desactiva si ya se vendió)
 *
 * OJO: sin login, cualquiera que conozca la URL puede entrar.
 * Antes de publicar el sitio hay que protegerlo.
 */
@WebServlet("/admin/productos/*")
public class AdminProductoServlet extends HttpServlet {
    private final ProductoDAO productos = new ProductoDAO();
    private final CatalogoDAO catalogo = new CatalogoDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String ruta = req.getPathInfo() == null ? "/" : req.getPathInfo();
        try {
            switch (ruta) {
                case "/nuevo" -> mostrarFormulario(req, resp, new Producto(), null);
                case "/editar" -> {
                    Integer id = Util.entero(req.getParameter("id"));
                    Producto p = id == null ? null : productos.buscarPorId(id);
                    if (p == null) {
                        flash(req, "Producto no encontrado.", "error");
                        resp.sendRedirect(base(req));
                    } else {
                        mostrarFormulario(req, resp, p, null);
                    }
                }
                default -> {
                    req.setAttribute("productos", productos.listar(null, req.getParameter("q"), null, false));
                    req.getRequestDispatcher("/WEB-INF/jsp/admin/lista.jsp").forward(req, resp);
                }
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String ruta = req.getPathInfo() == null ? "/" : req.getPathInfo();
        try {
            switch (ruta) {
                case "/guardar" -> guardar(req, resp);
                case "/eliminar" -> eliminar(req, resp);
                default -> resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private void guardar(HttpServletRequest req, HttpServletResponse resp)
            throws SQLException, ServletException, IOException {
        Producto p = new Producto();
        Integer id = Util.entero(req.getParameter("id"));
        if (id != null) p.setId(id);
        p.setSku(Util.limpiar(req.getParameter("sku")));
        p.setNombre(Util.limpiar(req.getParameter("nombre")));
        p.setDescripcion(Util.limpiar(req.getParameter("descripcion")));
        p.setEspecificaciones(Util.limpiar(req.getParameter("especificaciones")));
        String img = Util.limpiar(req.getParameter("imagenUrl"));
        p.setImagenUrl(img.isEmpty() ? null : img);
        p.setActivo(req.getParameter("activo") != null);

        Integer cat = Util.entero(req.getParameter("idCategoria"));
        Integer marca = Util.entero(req.getParameter("idMarca"));
        BigDecimal precio = Util.decimal(req.getParameter("precio"));
        Integer stock = Util.entero(req.getParameter("stock"));
        p.setIdCategoria(cat == null ? 0 : cat);
        p.setIdMarca(marca == null ? 0 : marca);
        p.setPrecio(precio == null ? BigDecimal.ZERO : precio);
        p.setStock(stock == null ? 0 : stock);

        String error = null;
        if (p.getSku().isEmpty() || p.getNombre().isEmpty()) error = "El SKU y el nombre son obligatorios.";
        else if (cat == null || marca == null) error = "Selecciona una categoría y una marca.";
        else if (precio == null || precio.signum() < 0) error = "Ingresa un precio válido (0 o mayor).";
        else if (stock == null || stock < 0) error = "Ingresa un stock válido (0 o mayor).";
        if (error != null) { mostrarFormulario(req, resp, p, error); return; }

        try {
            if (id == null) productos.crear(p); else productos.actualizar(p);
        } catch (SQLIntegrityConstraintViolationException e) {
            mostrarFormulario(req, resp, p, "Ya existe un producto con ese SKU.");
            return;
        }
        flash(req, id == null ? "Producto creado correctamente." : "Producto actualizado correctamente.", "ok");
        resp.sendRedirect(base(req));
    }

    private void eliminar(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        Integer id = Util.entero(req.getParameter("id"));
        if (id == null) {
            flash(req, "Producto no encontrado.", "error");
        } else if (productos.eliminar(id)) {
            flash(req, "Producto eliminado.", "ok");
        } else {
            flash(req, "El producto ya tiene ventas registradas, por eso no se borró: quedó oculto en la tienda.", "ok");
        }
        resp.sendRedirect(base(req));
    }

    private void mostrarFormulario(HttpServletRequest req, HttpServletResponse resp, Producto p, String error)
            throws SQLException, ServletException, IOException {
        req.setAttribute("p", p);
        req.setAttribute("error", error);
        req.setAttribute("categorias", catalogo.categorias());
        req.setAttribute("marcas", catalogo.marcas());
        req.getRequestDispatcher("/WEB-INF/jsp/admin/formulario.jsp").forward(req, resp);
    }

    /** Mensaje que se muestra una sola vez en la siguiente página. */
    private void flash(HttpServletRequest req, String mensaje, String tipo) {
        req.getSession().setAttribute("flash", mensaje);
        req.getSession().setAttribute("flashTipo", tipo);
    }

    private String base(HttpServletRequest req) {
        return req.getContextPath() + "/admin/productos";
    }
}
