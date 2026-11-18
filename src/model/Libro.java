package model;

// --- BOILERPLATE ---
public abstract class Libro {
    private int codigo;
    private String titulo;
    private double precioBase;
    private int copiasLicenciadas;

    public Libro(int codigo, String titulo, double precioBase, int copiasLicenciadas) {
        this.codigo = codigo;
        this.titulo = titulo;
        this.precioBase = precioBase;
        this.copiasLicenciadas = copiasLicenciadas;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public double getPrecioBase() {
        return precioBase;
    }

    public void setPrecioBase(double precioBase) {
        this.precioBase = precioBase;
    }

    public int getCopiasLicenciadas() {
        return copiasLicenciadas;
    }

    public void setCopiasLicenciadas(int copiasLicenciadas) {
        this.copiasLicenciadas = copiasLicenciadas;
    }

    // --- SOLUCIÓN PREGUNTA 7 (Firma polimórfica) ---
    // Método abstracto para calcular el precio final del libro
    public abstract double calcularPrecio();
}
