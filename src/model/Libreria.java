package model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class Libreria {

    // --- BOILERPLATE ---
    private List<Libro> catalogo;
    private List<Compra> compras;

    public Libreria() {
        this.catalogo = new ArrayList<>();
        this.compras = new ArrayList<>();
    }

    public List<Libro> getCatalogo() {
        return catalogo;
    }

    public void setCatalogo(List<Libro> catalogo) {
        this.catalogo = catalogo;
    }

    public List<Compra> getCompras() {
        return compras;
    }

    public void setCompras(List<Compra> compras) {
        this.compras = compras;
    }

    // --- SOLUCIÓN PREGUNTA 1 ---
    public Libro buscarLibro(String titulo) {
        // Iterar sobre el catálogo comparando títulos
        for (Libro libro : this.catalogo) {
            // Evaluamos la igualdad con equals() para comparar contenido de String
            if (libro.getTitulo().equals(titulo)) {
                return libro; // Retorna si existe
            }
        }
        return null; // Retorna NULL si no existe
    }

    // --- SOLUCIÓN PREGUNTA 4 ---
    public ArrayList<Compra> comprasSuperanTope(double p_tope) {
        ArrayList<Compra> comprasFiltradas = new ArrayList<>();
        
        // Iterar compras
        for (Compra compra : this.compras) {
            double totalSumaLineas = 0.0;
            
            // Calculamos la suma de subtotales por separado para cada compra
            for (Linea linea : compra.getLineas()) {
                totalSumaLineas += linea.getSubtotal();
            }
            
            // Si el total calculado es mayor al parámetro, lo agregamos al listado final
            if (totalSumaLineas > p_tope) {
                comprasFiltradas.add(compra);
            }
        }
        
        return comprasFiltradas;
    }

    // --- SOLUCIÓN PREGUNTA 6 ---
    // Usamos java.nio.file para manejar los archivos de texto
    public void GenerarReporteLibrosDigitales() {
        Path rutaArchivo = Paths.get("librosDigitales.txt");
        List<String> lineasReporte = new ArrayList<>();
        
        // Encabezado
        lineasReporte.add("=== REPORTE DE LIBROS DIGITALES ===");
        
        // Filtramos y armamos el reporte
        for (Libro libro : this.catalogo) {
            // Validamos que sea instancia de LibroDigital
            if (libro instanceof LibroDigital) {
                LibroDigital digital = (LibroDigital) libro;
                String detalle = String.format("Código: %d | Título: %s | Precio Base: %.2f | Descuento: %.2f",
                        digital.getCodigo(),
                        digital.getTitulo(),
                        digital.getPrecioBase(),
                        digital.getDescuento());
                lineasReporte.add(detalle);
            }
        }
        
        try {
            // Enviarlos al disco duro escribiendo usando Nio Files
            Files.write(rutaArchivo, lineasReporte, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            System.out.println("Reporte generado exitosamente en " + rutaArchivo.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error al intentar generar el archivo del reporte: " + e.getMessage());
        }
    }

    // --- SOLUCIÓN PREGUNTA 7 ---
    public double totalizarValorLibros() {
        double valorTotalCatalogo = 0.0;
        
        // Iterar polimórficamente el catálogo
        for (Libro libro : this.catalogo) {
            // calcularPrecio() está sobre-escrito en cada subclase, se resuelve en tiempo de ejecución
            double precioPorLibro = libro.calcularPrecio();
            
            // El producto... sumado al total
            valorTotalCatalogo += (libro.getCopiasLicenciadas() * precioPorLibro);
        }
        
        return valorTotalCatalogo;
    }

    // --- BOILERPLATE MAIN ---
    public static void main(String[] args) {
        Libreria miLibreria = new Libreria();
        
        // Poblar datos base para la demostración
        miLibreria.getCatalogo().add(new LibroDigital(1, "Clean Code", 100.0, 5, 10.0)); // Precio calculado: 90
        miLibreria.getCatalogo().add(new LibroFisico(2, "Design Patterns", 150.0, 3, 20.0)); // Precio calculado: 170

        System.out.println("Valor Total Catálogo: " + miLibreria.totalizarValorLibros());
        
        // Iniciar una compra de prueba
        Compra compraCliente = new Compra("Juan Perez", miLibreria);
        try {
            compraCliente.agregarLinea("Clean Code", 2);  // Subtotal = 2 * 90 = 180
            compraCliente.agregarLinea("Design Patterns", 1); // Subtotal = 1 * 170 = 170
        } catch (CompraInvalidaException e) {
            System.err.println("Error en compra: " + e.getMessage());
        }

        // Agregar la compra a las colecciones del manejador
        miLibreria.getCompras().add(compraCliente);
        
        System.out.println("Total Compra (sin IVA): " + compraCliente.getTotal());
        System.out.println("Total Compra final (con IVA): " + compraCliente.totalizarCompra());
        
        // Probar remociones y validaciones de la librería
        compraCliente.removerLinea("Clean Code");
        System.out.println("Total tras remoción: " + compraCliente.getTotal());

        // Compras filtradas y el reporte final
        miLibreria.comprasSuperanTope(100.0);
        miLibreria.GenerarReporteLibrosDigitales();
    }
}
