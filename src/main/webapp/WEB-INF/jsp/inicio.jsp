<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="titulo" value="Hardware y componentes para PC"/>
<c:set var="ancha" value="true"/>
<%@ include file="/WEB-INF/jsp/fragmentos/cabecera.jspf" %>

<!-- ===== HERO ===== -->
<section class="hero">
  <div class="contenedor">
    <div>
      <span class="etiqueta">⚡ Nuevos lanzamientos disponibles</span>
      <h1>Arma el PC de tus sueños con <em>los mejores componentes</em></h1>
      <p>Procesadores, tarjetas de video, memorias y mucho más, con garantía y asesoría real para que compres con confianza.</p>
      <div class="hero-botones">
        <a href="${ctx}/productos" class="btn btn-primario">Ver catálogo</a>
        <a href="#destacados" class="btn btn-borde">Productos destacados</a>
      </div>
    </div>
    <div class="hero-visual" aria-hidden="true">🖥️</div>
  </div>
</section>

<div class="contenedor portada-fin">

  <!-- ===== CATEGORÍAS ===== -->
  <section class="seccion">
    <div class="seccion-titulo">
      <div><h2>Explora por categoría</h2><p>Encuentra rápido lo que necesitas</p></div>
    </div>
    <div class="grid-cat">
      <c:forEach var="cat" items="${categorias}">
        <a class="cat-card" href="${ctx}/productos?cat=${cat.id}">
          <span class="icono"><c:out value="${cat.icono}"/></span><span><c:out value="${cat.nombre}"/></span>
        </a>
      </c:forEach>
    </div>
  </section>

  <!-- ===== PRODUCTOS DESTACADOS (desde la base de datos) ===== -->
  <section class="seccion" id="destacados">
    <div class="seccion-titulo">
      <div><h2>Productos destacados</h2><p>Lo más nuevo de nuestro catálogo</p></div>
      <a href="${ctx}/productos" class="ver-todo">Ver todo →</a>
    </div>
    <div class="grid-productos">
      <c:forEach var="p" items="${destacados}">
        <%@ include file="/WEB-INF/jsp/fragmentos/tarjeta.jspf" %>
      </c:forEach>
    </div>
  </section>

  <!-- ===== BANNER ===== -->
  <section class="banner-cupon">
    <div>
      <h2>🚚 Envío gratis en compras desde $500.000</h2>
      <p>Pagas seguro y recibes en la puerta de tu casa, en toda Colombia.</p>
    </div>
    <div class="codigo">ENVÍO GRATIS</div>
  </section>

  <!-- ===== BENEFICIOS ===== -->
  <section class="beneficios">
    <div class="beneficio"><span class="icono">🚚</span><div><strong>Envío a todo el país</strong><span>Rápido y asegurado</span></div></div>
    <div class="beneficio"><span class="icono">🛡️</span><div><strong>Garantía oficial</strong><span>Hasta 5 años según producto</span></div></div>
    <div class="beneficio"><span class="icono">💳</span><div><strong>Pago seguro</strong><span>Tarjeta, PSE y Nequi</span></div></div>
    <div class="beneficio"><span class="icono">💬</span><div><strong>Asesoría experta</strong><span>Te ayudamos a elegir</span></div></div>
  </section>
</div>

<%@ include file="/WEB-INF/jsp/fragmentos/pie.jspf" %>
