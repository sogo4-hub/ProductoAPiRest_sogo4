package es.daw.productoapirest.dto;

import es.daw.productoapirest.entity.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
@Data
public class RegisterRequest {
    @NotBlank(message = "El nombre de usuario es obligatorio")
    private String username;
    @NotBlank(message = "El password es obligatorio")
    private String password;
//    private String email;

//    private String role;
//    //PENDIENTE!!! y si queremos indicar varios roles????
    private List<String> roles= new ArrayList<>();
}
