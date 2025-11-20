package es.daw.productoapirest.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
public class GlobalExceptionHandler {
//    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
@ExceptionHandler( ConstraintViolationException.class)
public ResponseEntity<ErrorDTO> handleConstraintViolationException(ConstraintViolationException ex) {
    Map<String, String> errores = new HashMap<>();
    for (ConstraintViolation e : ex.getConstraintViolations()) {
        errores.put(e.getPropertyPath().toString(), e.getMessage());
    }
    ErrorDTO errorDTO = new ErrorDTO(
            "Error de constraint violation!!",
            errores,
            LocalDateTime.now()
    );
    return ResponseEntity.status(BAD_REQUEST).body(errorDTO);
}
@ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> handleException(MethodArgumentNotValidException ex) {

        Map<String, String> errores = new HashMap<>();

            for (FieldError e : ex.getBindingResult().getFieldErrors()) {
                errores.put(e.getField(), e.getDefaultMessage());
            }

        ErrorDTO errorDTO = new ErrorDTO(
                "Error de validacion en ProductoDTO",
                errores,
               LocalDateTime.now()
        );
//        return ResponseEntity.badRequest().body(errorDTO);
        return ResponseEntity.status(BAD_REQUEST).body(errorDTO); //Devolvemos un error 400
//        return new ResponseEntity<>(errorDTO, BAD_REQUEST);
    }
@ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handleGenericException(Exception ex) {
        ErrorDTO errorDTO = new ErrorDTO(
                "Pedazo de error generico",
                Map.of("detalle", ex.getMessage()),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
    }
    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<ErrorDTO> handleNumberFormatException(NumberFormatException ex) {
        ErrorDTO errorDTO = new ErrorDTO(
                "Invalid Number format.....",
                Map.of("detalle", ex.getMessage()),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
    }
    @ExceptionHandler(ProductoNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleProductoNotFoundException(ProductoNotFoundException ex) {
        ErrorDTO errorDTO = new ErrorDTO().builder()
                .message("Producto no encontrado")
                .details(Map.of("exception", ex.getClass().getSimpleName(),
                                "detalle", ex.getMessage()))
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);//404
    }
    @ExceptionHandler(FabricanteNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleFabricanteNotFoundException(FabricanteNotFoundException ex) {
        ErrorDTO errorDTO = new ErrorDTO().builder()
                .message("Fabricante no encontrado")
                .details(Map.of("exception", ex.getClass().getSimpleName(),
                                "detalle", ex.getMessage()))
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);//404
    }

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<ErrorDTO> handleDatabaseException(DatabaseException ex) {
        ErrorDTO errorDTO = new ErrorDTO().builder()
                .message("Problema con la base de datos")
                .details(Map.of("exception", ex.getClass().getSimpleName(),
                        "detalle", ex.getMessage()))
                .timestamp(LocalDateTime.now())
                .build();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
    }

    //PENDIENTE controlar SQLException
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ErrorDTO> handleSQLException(SQLException ex) {
        ErrorDTO errorDTO = new ErrorDTO().builder()
                .message("Problema con la base de datos")
                .details(Map.of("exception", ex.getClass().getSimpleName(),
                        "detalle", ex.getMessage()))
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorDTO> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        ErrorDTO errorDTO = new ErrorDTO().builder()
                .message("Usuario ya existe")
                .details(Map.of("exception", ex.getClass().getSimpleName(),
                        "detalle", ex.getMessage()))
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorDTO);
    }
    @ExceptionHandler(RoleNotFoundExcepction.class)
    public ResponseEntity<ErrorDTO> handleRoleNotFoundExcepction(RoleNotFoundExcepction ex) {
        ErrorDTO errorDTO = new ErrorDTO().builder()
                .message("Rol no encontrado")
                .details(Map.of("exception", ex.getClass().getSimpleName(),
                        "detalle", ex.getMessage()))
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
    }

}
