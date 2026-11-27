package com.techstore.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/** Utilidades pequeñas: formato de pesos y conversión segura de parámetros. */
public final class Util {
    private Util() {}

    /** 899000 -> "$899.000" */
    public static String pesos(BigDecimal valor) {
        DecimalFormatSymbols s = new DecimalFormatSymbols(Locale.ROOT);
        s.setGroupingSeparator('.');
        return "$" + new DecimalFormat("#,##0", s).format(valor);
    }

    public static Integer entero(String texto) {
        try {
            return texto == null || texto.isBlank() ? null : Integer.valueOf(texto.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static BigDecimal decimal(String texto) {
        try {
            return texto == null || texto.isBlank() ? null : new BigDecimal(texto.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static String limpiar(String texto) {
        return texto == null ? "" : texto.trim();
    }
}
