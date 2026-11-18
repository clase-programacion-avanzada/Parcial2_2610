package model;

// --- BOILERPLATE ---
public class LibroDigital extends Libro {
    private double descuento;

    public LibroDigital(int codigo, String titulo, double precioBase, int copiasLicenciadas, double descuento) {
        super(codigo, titulo, precioBase, copiasLicenciadas);
        this.descuento = descuento;
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    // --- SOLUCIÓN PREGUNTA 7 ---
    // Implementación del método polimórfico para calcular el precio
    @Override
    public double calcularPrecio() {
        // Para un libro digital el precio es el precio base menos el descuento
        return getPrecioBase() - descuento;
    }
}
