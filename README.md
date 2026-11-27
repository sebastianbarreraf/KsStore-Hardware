# KsStore (Java + JSP + MariaDB/MySQL)

## Estructura
```
tienda-hardware/
├── pom.xml
├── base_datos/schema.sql          ← crea la BD, tablas, datos y usuario
└── src/main/
    ├── java/com/techstore/
    │   ├── modelo/    Producto, Categoria, Marca, Pedido      (datos)
    │   ├── dao/       Conexion, ProductoDAO, PedidoDAO ...    (SQL / base de datos)
    │   ├── servlet/   Catalogo, Producto, Compra, AdminProducto (lógica web)
    │   └── util/      Util (formato de pesos, conversiones)
    └── webapp/
        ├── css/  js/
        └── WEB-INF/jsp/   catalogo, producto, pagar, confirmacion, admin/*
```

## Cómo ejecutarlo
1. Ejecuta `base_datos/schema.sql` en tu servidor (Workbench o consola).
2. Si no usas el usuario `tienda_app`, cambia usuario/clave en `dao/Conexion.java`.
3. Compila: `mvn clean package` (genera `target/tienda.war`).
4. Copia `tienda.war` a la carpeta `webapps` de **Tomcat 10.1** y arráncalo.
5. Abre `http://localhost:8080/tienda/`

## Página principal y archivos originales
- La página principal ahora es `WEB-INF/jsp/inicio.jsp`, servida por `InicioServlet` en `/inicio`.
- `index.jsp` redirige automáticamente a `/inicio`.
- Las páginas HTML originales se conservaron en `src/main/webapp/html-originales/` como referencia del diseño inicial.
- Los fragmentos JSP reutilizables están en `WEB-INF/jsp/fragmentos/`, incluido `tarjeta.jspf` para los productos destacados.

## Rutas
- `/productos` catálogo · `/producto?id=1` detalle · `/pagar?id=1` compra · `/confirmacion?id=1`
- `/admin/productos` panel de administración (crear, editar, eliminar)

## Estilos
- `css/ksstore.css` es la **única** hoja de estilos del sitio (la usan todos los JSP vía `cabecera.jspf`).
- `estilos.css`, `paginas.css` y `ksstore-premium.css` quedan solo porque las páginas de `html-originales/` los usan; ya no se cargan en el sitio.
- La portada declara `<c:set var="ancha" value="true"/>` para que `<main>` ocupe todo el ancho; las demás páginas van dentro del contenedor.
