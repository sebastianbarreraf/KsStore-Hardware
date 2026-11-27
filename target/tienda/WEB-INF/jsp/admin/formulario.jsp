<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="titulo" value="${p.id == 0 ? 'Nuevo producto' : 'Editar producto'}"/>
<%@ include file="/WEB-INF/jsp/fragmentos/cabecera.jspf" %>

<div class="migas"><a href="${ctx}/productos">Tienda</a><span>›</span><a href="${ctx}/admin/productos">Administración</a><span>›</span><c:out value="${titulo}"/></div>
<h1 class="titulo-pagina"><c:out value="${titulo}"/></h1>

<c:if test="${not empty error}"><div class="alerta error"><c:out value="${error}"/></div></c:if>

<form class="tarjeta form-admin" method="post" action="${ctx}/admin/productos/guardar">
  <c:if test="${p.id != 0}"><input type="hidden" name="id" value="${p.id}"></c:if>

  <div class="fila-2">
    <div class="campo"><label for="sku">SKU *</label><input id="sku" name="sku" value="<c:out value='${p.sku}'/>" maxlength="40" required></div>
    <div class="campo"><label for="nombre">Nombre *</label><input id="nombre" name="nombre" value="<c:out value='${p.nombre}'/>" maxlength="150" required></div>
  </div>

  <div class="fila-2">
    <div class="campo"><label for="idCategoria">Categoría *</label>
      <select id="idCategoria" name="idCategoria" required>
        <option value="">Selecciona...</option>
        <c:forEach var="c" items="${categorias}"><option value="${c.id}" ${c.id == p.idCategoria ? 'selected' : ''}><c:out value="${c.nombre}"/></option></c:forEach>
      </select></div>
    <div class="campo"><label for="idMarca">Marca *</label>
      <select id="idMarca" name="idMarca" required>
        <option value="">Selecciona...</option>
        <c:forEach var="m" items="${marcas}"><option value="${m.id}" ${m.id == p.idMarca ? 'selected' : ''}><c:out value="${m.nombre}"/></option></c:forEach>
      </select></div>
  </div>

  <div class="fila-2">
    <div class="campo"><label for="precio">Precio (COP) *</label><input id="precio" name="precio" type="number" min="0" step="1" value="${p.precio.toBigInteger()}" required></div>
    <div class="campo"><label for="stock">Stock *</label><input id="stock" name="stock" type="number" min="0" step="1" value="${p.stock}" required></div>
  </div>

  <div class="campo"><label for="imagenUrl">Imagen (opcional)</label>
    <input id="imagenUrl" name="imagenUrl" value="<c:out value='${p.imagenUrl}'/>" placeholder="https://... o /img/producto.jpg">
    <small>Si lo dejas vacío se mostrará el icono de la categoría.</small></div>

  <div class="campo"><label for="descripcion">Descripción</label><textarea id="descripcion" name="descripcion" rows="3"><c:out value="${p.descripcion}"/></textarea></div>

  <div class="campo"><label for="especificaciones">Especificaciones</label>
    <textarea id="especificaciones" name="especificaciones" rows="5" placeholder="Socket: AM5&#10;Núcleos: 6"><c:out value="${p.especificaciones}"/></textarea>
    <small>Una por línea, con el formato Clave: valor.</small></div>

  <label class="check"><input type="checkbox" name="activo" ${p.activo ? 'checked' : ''}> Visible en la tienda</label>

  <div class="acciones-fila">
    <button class="btn btn-primario btn-sin-borde" type="submit">💾 Guardar</button>
    <a class="btn btn-claro" href="${ctx}/admin/productos">Cancelar</a>
  </div>
</form>

<%@ include file="/WEB-INF/jsp/fragmentos/pie.jspf" %>
