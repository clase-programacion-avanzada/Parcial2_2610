package model;

// --- BOILERPLATE ---
public class Linea {
    private int cantidad;
    private double subtotal;
    private Libro libro;

    public Linea(Libro libro, int cantidad, double subtotal) {
        this.libro = libro;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }
}
