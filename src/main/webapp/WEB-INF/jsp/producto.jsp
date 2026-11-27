<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="titulo" value="${p.nombre}"/>
<%@ include file="/WEB-INF/jsp/fragmentos/cabecera.jspf" %>

<div class="migas">
  <a href="${ctx}/productos">Catálogo</a><span>›</span>
  <a href="${ctx}/productos?cat=${p.idCategoria}"><c:out value="${p.categoria}"/></a><span>›</span>
  <c:out value="${p.nombre}"/>
</div>

<div class="detalle">
  <div class="galeria">
    <c:choose>
      <c:when test="${not empty p.imagenUrl}"><img src="<c:url value='${p.imagenUrl}'/>" alt="<c:out value='${p.nombre}'/>"></c:when>
      <c:otherwise>${p.icono}</c:otherwise>
    </c:choose>
  </div>
  <div class="info-detalle">
    <span class="marca"><c:out value="${p.marca}"/></span>
    <h1><c:out value="${p.nombre}"/></h1>
    <div class="precio-grande">${p.precioTxt}</div>
    <div class="stock ${p.agotado or p.stockBajo ? 'bajo' : ''}">
      <c:choose>
        <c:when test="${p.agotado}">✖ Sin stock</c:when>
        <c:when test="${p.stockBajo}">⚠ Quedan solo ${p.stock} unidades</c:when>
        <c:otherwise>✔ Disponible · ${p.stock} unidades</c:otherwise>
      </c:choose>
    </div>
    <p class="descripcion"><c:out value="${p.descripcion}"/></p>
    <ul class="lista-ventajas">
      <li>🚚 Envío a toda Colombia (gratis desde $500.000)</li>
      <li>💳 Paga con tarjeta, PSE, Nequi o transferencia</li>
    </ul>
    <c:if test="${not p.agotado}">
      <form class="compra" action="${ctx}/pagar" method="get">
        <input type="hidden" name="id" value="${p.id}">
        <div class="qty">
          <button id="menos" type="button">−</button>
          <input id="cantidad" name="cantidad" type="number" value="1" min="1" max="${p.stock}" aria-label="Cantidad">
          <button id="mas" type="button">+</button>
        </div>
        <button class="btn-agregar" type="submit">💳 Comprar ahora</button>
      </form>
    </c:if>
  </div>
</div>

<c:if test="${not empty p.specs}">
  <section class="bloque">
    <h2>Especificaciones técnicas</h2>
    <table class="tabla-specs">
      <c:forEach var="s" items="${p.specs}">
        <tr><td><c:out value="${s[0]}"/></td><td><c:out value="${s[1]}"/></td></tr>
      </c:forEach>
    </table>
  </section>
</c:if>

<%@ include file="/WEB-INF/jsp/fragmentos/pie.jspf" %>
