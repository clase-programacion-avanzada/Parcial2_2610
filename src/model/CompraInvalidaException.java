package model;

// --- BOILERPLATE ---
// Excepción personalizada solicitada en la Pregunta 3 para manejar errores de compra
public class CompraInvalidaException extends Exception {

    public CompraInvalidaException(String mensaje) {
        super(mensaje);
    }

}
