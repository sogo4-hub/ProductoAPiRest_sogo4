package es.daw.productoapirest.exception;

public class RoleNotFoundExcepction extends RuntimeException{
    public RoleNotFoundExcepction(String message){
        super(message);
    }
}
