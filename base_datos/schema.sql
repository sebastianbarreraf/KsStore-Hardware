DROP DATABASE IF EXISTS tienda_hardware;
CREATE DATABASE tienda_hardware CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE tienda_hardware;

-- ---------- Catálogo ----------
CREATE TABLE categorias (
  id_categoria INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  nombre       VARCHAR(80) NOT NULL UNIQUE,
  icono        VARCHAR(16) NOT NULL DEFAULT '📦'      -- emoji que se muestra si el producto no tiene imagen
) ENGINE=InnoDB;

CREATE TABLE marcas (
  id_marca INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  nombre   VARCHAR(60) NOT NULL UNIQUE
) ENGINE=InnoDB;

CREATE TABLE productos (
  id_producto      INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  sku              VARCHAR(40)  NOT NULL UNIQUE,
  nombre           VARCHAR(150) NOT NULL,
  descripcion      TEXT,
  especificaciones TEXT,                                -- una por línea: "Clave: valor"
  id_categoria     INT UNSIGNED NOT NULL,
  id_marca         INT UNSIGNED NOT NULL,
  precio           DECIMAL(12,2) NOT NULL CHECK (precio >= 0),
  stock            INT NOT NULL DEFAULT 0 CHECK (stock >= 0),
  imagen_url       VARCHAR(255),
  activo           BOOLEAN NOT NULL DEFAULT TRUE,       -- FALSE = oculto en la tienda
  fecha_creacion   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_prod_cat   FOREIGN KEY (id_categoria) REFERENCES categorias(id_categoria),
  CONSTRAINT fk_prod_marca FOREIGN KEY (id_marca)     REFERENCES marcas(id_marca),
  INDEX idx_prod_nombre (nombre)
) ENGINE=InnoDB;

-- ---------- Pedidos (compra directa, sin cuenta de usuario) ----------
CREATE TABLE pedidos (
  id_pedido        INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  cliente_nombre   VARCHAR(100) NOT NULL,
  cliente_email    VARCHAR(120) NOT NULL,
  cliente_telefono VARCHAR(20)  NOT NULL,
  ciudad           VARCHAR(60)  NOT NULL,
  direccion        VARCHAR(200) NOT NULL,
  metodo_pago      ENUM('TARJETA_CREDITO','TARJETA_DEBITO','PSE','NEQUI','TRANSFERENCIA') NOT NULL,
  estado           ENUM('PENDIENTE','PAGADO','ENVIADO','ENTREGADO','CANCELADO') NOT NULL DEFAULT 'PENDIENTE',
  subtotal         DECIMAL(12,2) NOT NULL,
  costo_envio      DECIMAL(12,2) NOT NULL DEFAULT 0,
  total            DECIMAL(12,2) NOT NULL,
  fecha            DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE detalle_pedido (
  id_detalle      INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  id_pedido       INT UNSIGNED NOT NULL,
  id_producto     INT UNSIGNED NOT NULL,
  cantidad        INT UNSIGNED NOT NULL CHECK (cantidad > 0),
  precio_unitario DECIMAL(12,2) NOT NULL,               -- precio al momento de la compra
  CONSTRAINT fk_det_ped  FOREIGN KEY (id_pedido)   REFERENCES pedidos(id_pedido) ON DELETE CASCADE,
  CONSTRAINT fk_det_prod FOREIGN KEY (id_producto) REFERENCES productos(id_producto)
) ENGINE=InnoDB;

-- Descuenta el stock automáticamente al registrar una venta
DELIMITER $$
CREATE TRIGGER trg_descontar_stock
AFTER INSERT ON detalle_pedido
FOR EACH ROW
BEGIN
  UPDATE productos SET stock = stock - NEW.cantidad WHERE id_producto = NEW.id_producto;
END$$
DELIMITER ;

-- ---------- Datos iniciales ----------
INSERT INTO categorias (nombre, icono) VALUES
 ('Procesadores','🧠'),('Tarjetas de video','🎮'),('Memoria RAM','💾'),('Almacenamiento','💽'),
 ('Tarjetas madre','🧩'),('Fuentes de poder','🔌'),('Gabinetes','🖥️'),('Refrigeración','❄️'),('Periféricos','🖱️');

INSERT INTO marcas (nombre) VALUES
 ('AMD'),('Intel'),('NVIDIA'),('ASUS'),('MSI'),('Corsair'),('Kingston'),('Samsung'),('Gigabyte'),('Cooler Master'),('Logitech');

INSERT INTO productos (sku, nombre, descripcion, especificaciones, id_categoria, id_marca, precio, stock) VALUES
 ('CPU-AMD-7600X','AMD Ryzen 5 7600X','Procesador de 6 núcleos y 12 hilos para socket AM5, ideal para gaming y productividad.','Socket: AM5\nNúcleos: 6\nHilos: 12\nFrecuencia base: 4.7 GHz\nGarantía: 36 meses',1,1,899000,15),
 ('CPU-INT-13600K','Intel Core i5-13600K','Procesador de 14 núcleos para socket LGA1700.','Socket: LGA1700\nNúcleos: 14\nHilos: 20\nGarantía: 36 meses',1,2,1099000,10),
 ('GPU-NV-4060','NVIDIA GeForce RTX 4060 8GB','Tarjeta de video con 8GB GDDR6 y Ray Tracing.','Memoria: 8GB GDDR6\nConsumo: 115W\nGarantía: 24 meses',2,3,1699000,8),
 ('GPU-MSI-4070S','MSI RTX 4070 Super 12GB','Potencia para juegos en 1440p con DLSS 3.','Memoria: 12GB GDDR6X\nConsumo: 220W\nGarantía: 24 meses',2,5,2999000,5),
 ('RAM-COR-16D5','Corsair Vengeance 16GB DDR5 5600','Kit de memoria 2x8GB DDR5.','Capacidad: 16GB (2x8GB)\nTipo: DDR5\nVelocidad: 5600 MHz\nGarantía: 60 meses',3,6,299000,30),
 ('RAM-KIN-16D4','Kingston Fury 16GB DDR4 3200','Kit de memoria 2x8GB DDR4.','Capacidad: 16GB (2x8GB)\nTipo: DDR4\nVelocidad: 3200 MHz',3,7,189000,40),
 ('SSD-SAM-980-1T','Samsung 980 NVMe 1TB','SSD M.2 NVMe de 1TB, rápido y confiable.','Capacidad: 1TB\nInterfaz: NVMe PCIe 3.0\nLectura: 3500 MB/s',4,8,329000,25),
 ('SSD-KIN-480','Kingston A400 480GB SATA','SSD SATA de 2.5 pulgadas.','Capacidad: 480GB\nInterfaz: SATA III',4,7,159000,50),
 ('MB-ASU-B650','ASUS TUF Gaming B650-Plus','Tarjeta madre AM5 con soporte DDR5.','Socket: AM5\nChipset: B650\nMemoria: DDR5\nFormato: ATX',5,4,999000,7),
 ('PSU-COR-650','Corsair CV650 650W 80+ Bronze','Fuente de poder de 650W.','Potencia: 650W\nCertificación: 80+ Bronze\nFormato: ATX',6,6,289000,20),
 ('CASE-CM-NR200','Cooler Master NR200P','Gabinete compacto Mini-ITX.','Formato: Mini-ITX\nColor: Negro',7,10,549000,6),
 ('COOL-CM-212','Cooler Master Hyper 212','Disipador por aire para CPU.','Tipo: Aire\nCompatibilidad: AM4, AM5, LGA1700',8,10,129000,2),
 ('PER-LOG-G203','Logitech G203 Mouse Gamer','Mouse con iluminación RGB.','Sensor: 8000 DPI\nConexión: USB',9,11,89000,45);

-- ---------- Usuario de la aplicación (ajusta la clave y cámbiala también en Conexion.java) ----------
CREATE USER IF NOT EXISTS 'tienda_app'@'localhost' IDENTIFIED BY 'TuClaveSegura123';
GRANT SELECT, INSERT, UPDATE, DELETE ON tienda_hardware.* TO 'tienda_app'@'localhost';
FLUSH PRIVILEGES;
