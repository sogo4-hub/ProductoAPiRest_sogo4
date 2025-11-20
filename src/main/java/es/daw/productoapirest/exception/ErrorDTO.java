package es.daw.productoapirest.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorDTO {
    private String message; //Mensaje nuestro
    //PENDIENTE !!! details un Map con campo valor!!!
    private Map<String, String> details; //Detalles del error e.getMessage
    private LocalDateTime timestamp; //Fecha y hora del error

}
