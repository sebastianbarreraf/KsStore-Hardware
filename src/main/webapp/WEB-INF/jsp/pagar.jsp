<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="titulo" value="Pagar"/>
<%@ include file="/WEB-INF/jsp/fragmentos/cabecera.jspf" %>

<div class="migas">
  <a href="${ctx}/productos">Catálogo</a><span>›</span>
  <a href="${ctx}/producto?id=${p.id}"><c:out value="${p.nombre}"/></a><span>›</span>Pagar
</div>
<h1 class="titulo-pagina">Finalizar compra</h1>

<c:if test="${not empty error}"><div class="alerta error"><c:out value="${error}"/></div></c:if>

<form class="layout-2" action="${ctx}/pagar" method="post">
  <input type="hidden" name="id" value="${p.id}">
  <input type="hidden" name="cantidad" value="${cantidad}">

  <div>
    <section class="tarjeta">
      <h3>1. Datos de contacto y envío</h3>
      <div class="fila-2">
        <div class="campo"><label for="nombre">Nombre completo</label><input id="nombre" name="nombre" value="<c:out value='${param.nombre}'/>" required></div>
        <div class="campo"><label for="telefono">Teléfono</label><input id="telefono" name="telefono" type="tel" value="<c:out value='${param.telefono}'/>" required></div>
      </div>
      <div class="campo"><label for="email">Correo electrónico</label><input id="email" name="email" type="email" value="<c:out value='${param.email}'/>" required></div>
      <div class="fila-2">
        <div class="campo"><label for="ciudad">Ciudad</label><input id="ciudad" name="ciudad" value="<c:out value='${empty param.ciudad ? "Medellín" : param.ciudad}'/>" required></div>
        <div class="campo"><label for="direccion">Dirección</label><input id="direccion" name="direccion" value="<c:out value='${param.direccion}'/>" required></div>
      </div>
    </section>

    <section class="tarjeta">
      <h3>2. Método de pago</h3>
      <label class="opcion-pago"><input type="radio" name="metodo" value="TARJETA_CREDITO" ${empty param.metodo or param.metodo == 'TARJETA_CREDITO' ? 'checked' : ''}><span class="icono">💳</span>Tarjeta de crédito</label>
      <label class="opcion-pago"><input type="radio" name="metodo" value="TARJETA_DEBITO" ${param.metodo == 'TARJETA_DEBITO' ? 'checked' : ''}><span class="icono">💳</span>Tarjeta débito</label>
      <label class="opcion-pago"><input type="radio" name="metodo" value="PSE" ${param.metodo == 'PSE' ? 'checked' : ''}><span class="icono">🏦</span>PSE</label>
      <label class="opcion-pago"><input type="radio" name="metodo" value="NEQUI" ${param.metodo == 'NEQUI' ? 'checked' : ''}><span class="icono">📱</span>Nequi</label>
      <label class="opcion-pago"><input type="radio" name="metodo" value="TRANSFERENCIA" ${param.metodo == 'TRANSFERENCIA' ? 'checked' : ''}><span class="icono">🔁</span>Transferencia bancaria</label>
      <p class="nota">Entorno de pruebas: el pago es simulado y no se cobra ni se guardan datos de tarjeta.</p>
    </section>
  </div>

  <aside class="tarjeta resumen">
    <h3>Tu pedido</h3>
    <div class="linea"><span><c:out value="${p.nombre}"/> × ${cantidad}</span><span>${subtotal}</span></div>
    <div class="linea"><span>Envío</span><span class="ok">${envio}</span></div>
    <div class="linea total"><span>Total</span><span>${total}</span></div>
    <button class="btn btn-primario btn-block btn-sin-borde" type="submit">Confirmar y pagar</button>
    <a href="${ctx}/producto?id=${p.id}" class="btn btn-claro btn-block">Volver al producto</a>
  </aside>
</form>

<%@ include file="/WEB-INF/jsp/fragmentos/pie.jspf" %>
