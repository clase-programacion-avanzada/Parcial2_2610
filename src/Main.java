import model.Compra;
import model.CompraInvalidaException;
import model.Libro;
import model.LibroDigital;
import model.LibroFisico;
import model.Linea;
import model.Libreria;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

// --- BOILERPLATE ---
// Clase principal creada para validar las soluciones prácticas del examen.
public class Main {

    private static int pruebasRealizadas;
    private static int pruebasAprobadas;

    public static void main(String[] args) {
        Libreria libreria = crearLibreria();

        // --- SOLUCIÓN DE VALIDACIÓN ---
        validarBusqueda(libreria);
        validarPreciosPolimorficos(libreria);
        validarValorTotalCatalogo(libreria);
        validarCompraConLineas(libreria);
        validarReporte(libreria);

        System.out.println("\nResultado: " + pruebasAprobadas + "/" + pruebasRealizadas
                + " validaciones aprobadas.");
    }

    private static Libreria crearLibreria() {
        Libreria libreria = new Libreria();
        libreria.getCatalogo().add(
                new LibroDigital(1, "Clean Code", 100.0, 5, 10.0));
        libreria.getCatalogo().add(
                new LibroFisico(2, "Design Patterns", 150.0, 3, 20.0));
        return libreria;
    }

    private static void validarBusqueda(Libreria libreria) {
        Libro encontrado = libreria.buscarLibro("Clean Code");
        validar("Pregunta 1: el libro existente debe encontrarse",
                encontrado != null && encontrado.getTitulo().equals("Clean Code"));

        validar("Pregunta 1: el libro inexistente debe retornar null",
                libreria.buscarLibro("Libro Inexistente") == null);
    }

    private static void validarPreciosPolimorficos(Libreria libreria) {
        Libro digital = libreria.buscarLibro("Clean Code");
        Libro fisico = libreria.buscarLibro("Design Patterns");

        validar("Pregunta 7: libro digital aplica descuento",
                casiIgual(digital.calcularPrecio(), 90.0));
        validar("Pregunta 7: libro físico aplica costo de envío",
                casiIgual(fisico.calcularPrecio(), 170.0));
    }

    private static void validarValorTotalCatalogo(Libreria libreria) {
        // 5 copias digitales a 90 y 3 copias físicas a 170.
        validar("Pregunta 7: calcula el valor total del catálogo",
                casiIgual(libreria.totalizarValorLibros(), 960.0));
    }

    private static void validarCompraConLineas(Libreria libreria) {
        Compra compra = new Compra("Juan Pérez", libreria);

        try {
            Linea lineaDigital = compra.agregarLinea("Clean Code", 2);
            validar("Pregunta 3: crea la línea digital",
                    lineaDigital != null
                            && casiIgual(lineaDigital.getSubtotal(), 180.0)
                            && compra.getLineas().size() == 1);

            Linea lineaFisica = compra.agregarLinea("Design Patterns", 1);
            validar("Pregunta 3: crea la línea física y calcula su subtotal",
                    lineaFisica != null
                            && casiIgual(lineaFisica.getSubtotal(), 170.0)
                            && compra.getLineas().size() == 2);

            validar("Pregunta 3: descuenta las copias licenciadas",
                    libreria.buscarLibro("Clean Code").getCopiasLicenciadas() == 3
                            && libreria.buscarLibro("Design Patterns").getCopiasLicenciadas() == 2);

            validar("Pregunta 2: suma subtotales y aplica el 19% de IVA",
                    casiIgual(compra.totalizarCompra(), 416.5));

            libreria.getCompras().add(compra);
            validar("Pregunta 4: filtra compras que superan el tope",
                    libreria.comprasSuperanTope(300.0).size() == 1
                            && libreria.comprasSuperanTope(400.0).size() == 0);

            validar("Pregunta 3: rechaza títulos inexistentes",
                    excepcionCapturada(compra, "Título inexistente", 1));
            validar("Pregunta 3: rechaza cantidades insuficientes",
                    excepcionCapturada(compra, "Clean Code", 4));

            compra.removerLinea("Clean Code");
            validar("Pregunta 5: remueve la línea y restaura las copias",
                    compra.getLineas().size() == 1
                            && libreria.buscarLibro("Clean Code").getCopiasLicenciadas() == 5);
            validar("Pregunta 5: recalcula el total después de remover",
                    casiIgual(compra.getTotal(), 202.3));

        } catch (CompraInvalidaException e) {
            validar("La validación no debe lanzar errores inesperados",
                    false);
            System.out.println("Excepción inesperada: " + e.getMessage());
        }
    }

    private static boolean excepcionCapturada(Compra compra, String titulo,
                                               int cantidad) {
        try {
            compra.agregarLinea(titulo, cantidad);
            return false;
        } catch (CompraInvalidaException e) {
            return true;
        }
    }

    private static void validarReporte(Libreria libreria) {
        libreria.GenerarReporteLibrosDigitales();

        Path reporte = Paths.get("librosDigitales.txt");
        boolean archivoExiste = Files.exists(reporte);

        validar("Pregunta 6: genera el archivo de libros digitales",
                archivoExiste);

        if (archivoExiste) {
            try {
                validar("Pregunta 6: el reporte contiene datos de libros digitales",
                        Files.readAllLines(reporte).size() > 1);
            } catch (Exception e) {
                validar("Pregunta 6: se puede leer el reporte generado", false);
            }
        }
    }

    private static void validar(String descripcion, boolean resultado) {
        pruebasRealizadas++;
        if (resultado) {
            pruebasAprobadas++;
            System.out.println("[OK] " + descripcion);
        } else {
            System.out.println("[ERROR] " + descripcion);
        }
    }

    private static boolean casiIgual(double valor1, double valor2) {
        return Math.abs(valor1 - valor2) < 0.0001;
    }
}
