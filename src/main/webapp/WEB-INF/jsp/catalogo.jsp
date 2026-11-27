<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="titulo" value="Catálogo"/>
<%@ include file="/WEB-INF/jsp/fragmentos/cabecera.jspf" %>

<h1 class="titulo-pagina">Catálogo de productos</h1>

<nav class="nav-cat" aria-label="Categorías">
  <ul>
    <li><a class="${empty catSel ? 'activa' : ''}" href="${ctx}/productos">Todos</a></li>
    <c:forEach var="c" items="${categorias}">
      <li><a class="${catSel == c.id ? 'activa' : ''}" href="${ctx}/productos?cat=${c.id}"><c:out value="${c.icono} ${c.nombre}"/></a></li>
    </c:forEach>
  </ul>
</nav>

<form class="barra-orden" method="get" action="${ctx}/productos">
  <c:if test="${not empty catSel}"><input type="hidden" name="cat" value="${catSel}"></c:if>
  <input type="hidden" name="q" value="<c:out value='${q}'/>">
  <p>${productos.size()} productos<c:if test="${not empty q}"> para "<c:out value="${q}"/>"</c:if></p>
  <label class="orden">Ordenar por
    <select name="orden" onchange="this.form.submit()">
      <option value="" ${empty orden ? 'selected' : ''}>Novedades</option>
      <option value="precio-asc" ${orden == 'precio-asc' ? 'selected' : ''}>Precio: menor a mayor</option>
      <option value="precio-desc" ${orden == 'precio-desc' ? 'selected' : ''}>Precio: mayor a menor</option>
      <option value="nombre" ${orden == 'nombre' ? 'selected' : ''}>Nombre (A-Z)</option>
    </select>
  </label>
</form>

<c:choose>
  <c:when test="${empty productos}">
    <div class="vacio"><span class="icono">🔍</span><h3>No encontramos productos</h3><p>Prueba con otra búsqueda o categoría.</p></div>
  </c:when>
  <c:otherwise>
    <div class="grid-productos">
      <c:forEach var="p" items="${productos}">
        <%@ include file="/WEB-INF/jsp/fragmentos/tarjeta.jspf" %>
      </c:forEach>
    </div>
  </c:otherwise>
</c:choose>

<%@ include file="/WEB-INF/jsp/fragmentos/pie.jspf" %>
