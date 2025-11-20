package es.daw.productoapirest.exception;

public class FabricanteNotFoundException extends RuntimeException {
    public FabricanteNotFoundException(String message) {
        super(message);
    }
}
