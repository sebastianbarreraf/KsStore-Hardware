<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="titulo" value="Administración"/>
<%@ include file="/WEB-INF/jsp/fragmentos/cabecera.jspf" %>

<div class="migas"><a href="${ctx}/productos">Tienda</a><span>›</span>Administración</div>
<div class="admin-cab">
  <h1 class="titulo-pagina">Administrar productos</h1>
  <a class="btn btn-primario" href="${ctx}/admin/productos/nuevo">+ Nuevo producto</a>
</div>

<c:if test="${not empty sessionScope.flash}">
  <div class="alerta ${sessionScope.flashTipo}"><c:out value="${sessionScope.flash}"/></div>
  <c:remove var="flash" scope="session"/><c:remove var="flashTipo" scope="session"/>
</c:if>

<form class="busca-admin" method="get" action="${ctx}/admin/productos">
  <input name="q" value="<c:out value='${param.q}'/>" placeholder="Buscar por nombre, marca o SKU">
  <button class="btn btn-claro" type="submit">Buscar</button>
</form>

<div class="tabla-wrap">
  <table class="tabla-admin">
    <thead><tr><th>Producto</th><th>SKU</th><th>Categoría</th><th>Precio</th><th>Stock</th><th>Estado</th><th>Acciones</th></tr></thead>
    <tbody>
      <c:forEach var="p" items="${productos}">
        <tr>
          <td>
            <div class="celda-prod">
              <div class="mini">
                <c:choose>
                  <c:when test="${not empty p.imagenUrl}"><img src="<c:url value='${p.imagenUrl}'/>" alt=""></c:when>
                  <c:otherwise>${p.icono}</c:otherwise>
                </c:choose>
              </div>
              <div><strong><c:out value="${p.nombre}"/></strong><br><small><c:out value="${p.marca}"/></small></div>
            </div>
          </td>
          <td><c:out value="${p.sku}"/></td>
          <td><c:out value="${p.categoria}"/></td>
          <td>${p.precioTxt}</td>
          <td><span class="pill ${p.agotado or p.stockBajo ? 'bajo' : ''}">${p.stock}</span></td>
          <td><span class="pill ${p.activo ? 'on' : 'off'}">${p.activo ? 'Visible' : 'Oculto'}</span></td>
          <td>
            <div class="acciones-fila">
              <a class="btn-sm" href="${ctx}/admin/productos/editar?id=${p.id}">✏️ Editar</a>
              <form method="post" action="${ctx}/admin/productos/eliminar" data-confirm="¿Eliminar &quot;<c:out value='${p.nombre}'/>&quot;?">
                <input type="hidden" name="id" value="${p.id}">
                <button class="btn-sm btn-peligro" type="submit">🗑️ Eliminar</button>
              </form>
            </div>
          </td>
        </tr>
      </c:forEach>
      <c:if test="${empty productos}"><tr><td colspan="7" class="vacio">No hay productos para mostrar.</td></tr></c:if>
    </tbody>
  </table>
</div>

<%@ include file="/WEB-INF/jsp/fragmentos/pie.jspf" %>
