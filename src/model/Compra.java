package model;

import java.util.ArrayList;
import java.util.List;

public class Compra {
    
    // --- BOILERPLATE ---
    private String nombreCliente;
    private double total;
    private List<Linea> lineas;
    private Libreria libreria; // Para consultar el catálogo como se discutió

    public Compra(String nombreCliente, Libreria libreria) {
        this.nombreCliente = nombreCliente;
        this.libreria = libreria;
        this.lineas = new ArrayList<>();
        this.total = 0.0;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public List<Linea> getLineas() {
        return lineas;
    }

    public void setLineas(List<Linea> lineas) {
        this.lineas = lineas;
    }

    public Libreria getLibreria() {
        return libreria;
    }

    public void setLibreria(Libreria libreria) {
        this.libreria = libreria;
    }

    // --- SOLUCIÓN PREGUNTA 2 ---
    public double totalizarCompra() {
        double nuevoTotal = 0.0;
        
        // Sumar subtotales de las líneas
        for (Linea linea : lineas) {
            nuevoTotal += linea.getSubtotal();
        }
        
        // Agregar el 19% correspondiente al IVA
        nuevoTotal = nuevoTotal + (nuevoTotal * 0.19);
        
        // Guardar el resultado en el total de la compra
        this.total = nuevoTotal;
        
        return this.total;
    }

    // --- SOLUCIÓN PREGUNTA 3 ---
    public Linea agregarLinea(String titulo, int cantidad) throws CompraInvalidaException {
        // Buscar el libro en el catálogo
        Libro libro = this.libreria.buscarLibro(titulo);
        
        // Lanzar excepcion si no existe o saldo en copias es insuficiente
        if (libro == null) {
            throw new CompraInvalidaException("El libro con título '" + titulo + "' no existe en el catálogo.");
        }
        
        if (libro.getCopiasLicenciadas() < cantidad) {
            throw new CompraInvalidaException("No hay suficientes copias del libro '" + titulo + "'.");
        }
        
        // Disminuir en la cantidad comprada el número de copias licenciadas del libro
        libro.setCopiasLicenciadas(libro.getCopiasLicenciadas() - cantidad);
        
        // Calcular el subtotal de la línea: cantidad * precio
        double subtotalLinea = cantidad * libro.calcularPrecio();
        
        // Crear la linea
        Linea nuevaLinea = new Linea(libro, cantidad, subtotalLinea);
        
        // Adicionar la línea a las líneas de la compra
        this.lineas.add(nuevaLinea);
        
        // Actualizar el atributo total sumando el subtotal (antes de IVA, según interpretación literal
        // del enunciado ("actualizar el atributo total sumando el subtotal..."). 
        // No obstante, si debe coincidir con `totalizarCompra` podríamos llamar `totalizarCompra()` aquí también:
        this.total += subtotalLinea;
        
        return nuevaLinea;
    }

    // --- SOLUCIÓN PREGUNTA 5 ---
    public void removerLinea(String titulo) {
        Linea lineaARemover = null;
        
        // Buscar la línea que tiene asociado el libro cuyo título coincide
        for (Linea linea : lineas) {
            if (linea.getLibro().getTitulo().equals(titulo)) {
                lineaARemover = linea;
                // Como los títulos son únicos, detenemos la búsqueda
                break; 
            }
        }
        
        if (lineaARemover != null) {
            // Remover la línea de la lista
            this.lineas.remove(lineaARemover);
            
            // Actualizar el número de copias licenciadas restaurándolas
            Libro libroAsociado = lineaARemover.getLibro();
            libroAsociado.setCopiasLicenciadas(libroAsociado.getCopiasLicenciadas() + lineaARemover.getCantidad());
            
            // Actualizar el total de la compra recomputándolo entero
            this.totalizarCompra();
        }
    }
}
