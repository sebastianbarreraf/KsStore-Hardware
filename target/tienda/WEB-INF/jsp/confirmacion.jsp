<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="titulo" value="Pedido confirmado"/>
<%@ include file="/WEB-INF/jsp/fragmentos/cabecera.jspf" %>

<div class="auth">
  <div class="tarjeta auth-card">
    <h1>✅ ¡Pedido #${pedido.id} registrado!</h1>
    <p>Gracias por tu compra, <c:out value="${pedido.clienteNombre}"/>.</p>
    <div class="linea"><span><c:out value="${pedido.producto}"/> × ${pedido.cantidad}</span><span>${pedido.subtotalTxt}</span></div>
    <div class="linea"><span>Envío</span><span>${pedido.envioTxt}</span></div>
    <div class="linea total"><span>Total</span><span>${pedido.totalTxt}</span></div>
    <div class="linea"><span>Método de pago</span><span><c:out value="${pedido.metodoTxt}"/></span></div>
    <div class="linea"><span>Estado</span><span><strong><c:out value="${pedido.estado}"/></strong></span></div>
    <div class="linea"><span>Envío a</span><span><c:out value="${pedido.direccion}"/>, <c:out value="${pedido.ciudad}"/></span></div>
    <c:if test="${pedido.estado == 'PENDIENTE'}">
      <p class="nota">Tu pedido queda pendiente hasta confirmar la transferencia.</p>
    </c:if>
    <a href="${ctx}/productos" class="btn btn-primario btn-block" style="margin-top:20px">Seguir comprando</a>
  </div>
</div>

<%@ include file="/WEB-INF/jsp/fragmentos/pie.jspf" %>
