package es.daw.productoapirest.exception;

public class ProductoNotFoundException extends RuntimeException{
    public ProductoNotFoundException(String message) {
        super(message);
    }
}
