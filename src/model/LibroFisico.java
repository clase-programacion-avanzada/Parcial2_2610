package model;

// --- BOILERPLATE ---
public class LibroFisico extends Libro {
    private double costoEnvio;

    public LibroFisico(int codigo, String titulo, double precioBase, int copiasLicenciadas, double costoEnvio) {
        super(codigo, titulo, precioBase, copiasLicenciadas);
        this.costoEnvio = costoEnvio;
    }

    public double getCostoEnvio() {
        return costoEnvio;
    }

    public void setCostoEnvio(double costoEnvio) {
        this.costoEnvio = costoEnvio;
    }

    // --- SOLUCIÓN PREGUNTA 7 ---
    // Implementación del método polimórfico para calcular el precio
    @Override
    public double calcularPrecio() {
        // Para un libro físico, el precio es el precio base más el costo del envío
        return getPrecioBase() + costoEnvio;
    }
}
