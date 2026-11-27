package com.techstore.modelo;

import com.techstore.util.Util;
import java.math.BigDecimal;

public class Pedido {
    private int id, cantidad;
    private String clienteNombre, clienteEmail, clienteTelefono, ciudad, direccion, metodoPago, estado, producto;
    private BigDecimal subtotal = BigDecimal.ZERO, envio = BigDecimal.ZERO, total = BigDecimal.ZERO;

    public int getId() { return id; }                     public void setId(int v) { id = v; }
    public int getCantidad() { return cantidad; }         public void setCantidad(int v) { cantidad = v; }
    public String getClienteNombre() { return clienteNombre; }  public void setClienteNombre(String v) { clienteNombre = v; }
    public String getClienteEmail() { return clienteEmail; }    public void setClienteEmail(String v) { clienteEmail = v; }
    public String getClienteTelefono() { return clienteTelefono; }  public void setClienteTelefono(String v) { clienteTelefono = v; }
    public String getCiudad() { return ciudad; }          public void setCiudad(String v) { ciudad = v; }
    public String getDireccion() { return direccion; }    public void setDireccion(String v) { direccion = v; }
    public String getMetodoPago() { return metodoPago; }  public void setMetodoPago(String v) { metodoPago = v; }
    public String getEstado() { return estado; }          public void setEstado(String v) { estado = v; }
    public String getProducto() { return producto; }      public void setProducto(String v) { producto = v; }
    public BigDecimal getSubtotal() { return subtotal; }  public void setSubtotal(BigDecimal v) { subtotal = v; }
    public BigDecimal getEnvio() { return envio; }        public void setEnvio(BigDecimal v) { envio = v; }
    public BigDecimal getTotal() { return total; }        public void setTotal(BigDecimal v) { total = v; }

    public String getSubtotalTxt() { return Util.pesos(subtotal); }
    public String getEnvioTxt() { return envio.signum() == 0 ? "Gratis" : Util.pesos(envio); }
    public String getTotalTxt() { return Util.pesos(total); }
    public String getMetodoTxt() { return metodoPago == null ? "" : metodoPago.replace('_', ' ').toLowerCase(); }
}
