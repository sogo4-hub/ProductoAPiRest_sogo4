package es.daw.productoapirest.service;

import es.daw.productoapirest.dto.ApiResponse;
import es.daw.productoapirest.dto.AuthRequest;
import es.daw.productoapirest.dto.AuthResponse;
import es.daw.productoapirest.dto.RegisterRequest;
import es.daw.productoapirest.entity.Role;
import es.daw.productoapirest.entity.User;
import es.daw.productoapirest.exception.RoleNotFoundExcepction;
import es.daw.productoapirest.exception.handleSQLException;
import es.daw.productoapirest.repository.RoleRepository;
import es.daw.productoapirest.repository.UserRepository;
import es.daw.productoapirest.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerMapping;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import java.util.Optional;

@Service
@RequiredArgsConstructor

public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final HandlerMapping resourceHandlerMapping;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public ResponseEntity<ApiResponse> registro(RegisterRequest request){
        if(userRepository.findByUsername(request.getUsername()).isPresent()){
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new ApiResponse(false, "El usuario "+ request.getUsername() +" ya existe"));
        }

        // 2. Determinar los roles solicitados o asignar por defecto ROLE_USER
        List<String> requestedRoles = Optional.ofNullable(request.getRoles())
                .filter(list -> !list.isEmpty())
                .orElse(List.of("ROLE_USER"));

        // 3. Buscar y validar cada rol en la base de datos
        Set<Role> userRoles = requestedRoles.stream()
                .map(String::trim)
                .filter(r -> !r.isEmpty())
                .map(String::toUpperCase)
                .map(r -> r.startsWith("ROLE_") ? r : "ROLE_" + r)
                .distinct()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new RoleNotFoundExcepction("El rol " + roleName + " no existe")))
                .collect(Collectors.toSet());

        // 4. Crear el usuario
        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setRoles(userRoles);

        // 5. Guardar el usuario
        if(userRepository.save(newUser) == null){
            throw new handleSQLException("No se guardó el usuario " + request.getUsername());
        }

        ApiResponse response = new ApiResponse(true, "Usuario creado correctamente");
        response.addDetail("username", newUser.getUsername());
        response.addDetail("roles", newUser.getRoles()
                .stream()
                .map(Role::getName)
                .toList()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    public ResponseEntity<AuthResponse> autentificacion(AuthRequest request){
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    ));
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtService.generateToken(userDetails);
            return ResponseEntity.ok(new AuthResponse(token));
        } catch (BadCredentialsException e) {
            //Credenciales invalidas
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse("Credenciales invalidas"));
        }
    }
}
