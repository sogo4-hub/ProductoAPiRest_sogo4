package es.daw.productoapirest.controller;


import es.daw.productoapirest.dto.ApiResponse;
import es.daw.productoapirest.dto.AuthResponse;
import es.daw.productoapirest.dto.AuthRequest;
import es.daw.productoapirest.dto.RegisterRequest;
import es.daw.productoapirest.entity.Role;
import es.daw.productoapirest.entity.User;
import es.daw.productoapirest.exception.UserAlreadyExistsException;
import es.daw.productoapirest.exception.handleSQLException;
import es.daw.productoapirest.repository.RoleRepository;
import es.daw.productoapirest.repository.UserRepository;
import es.daw.productoapirest.security.JwtService;
import es.daw.productoapirest.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final HandlerMapping resourceHandlerMapping;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;


    private final AuthService authService;



    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegisterRequest request) {
        ResponseEntity<ApiResponse> response=authService.registro(request);
        ApiResponse body= response.getBody();
        if(!body.isSuccess()){
            throw new UserAlreadyExistsException("El usuario "+ request.getUsername() +" repetido");
        }
        return response;
        //1.Comprobar si el usuario ya existe
//        if(userRepository.findByUsername(request.getUsername()).isPresent()){
//            return ResponseEntity
//                    .status(HttpStatus.CONFLICT) //409 porque ya existe el usuario
//                    .body(new ApiResponse(false, "El usuario "+ request.getUsername() +" ya existe"));
//        }else{
//            //2. Determinar el rol solicitado o por defecto
//            String roleName = Optional.ofNullable(request.getRole())
//                    .map(String::toLowerCase)
//                    .map( r -> r.startsWith("ROLE_") ? r : "ROLE_" + r)
//                    .orElse("ROLE_USER");
//            //3. Buscar el rol en la base de datos
//            Role role = roleRepository.findByName(roleName)
//                    .orElseThrow(() -> new IllegalArgumentException("El rol "+roleName+" no existe"));
//            if(role==null){
//                return ResponseEntity
//                        .status(HttpStatus.BAD_REQUEST)
//                        .body(new ApiResponse(false, "El rol "+roleName+" no existe"));
//            }
//            //4. Crear el usuario
//
//            User newUser = new User();
//            newUser.setUsername(request.getUsername());
//            newUser.setPassword(passwordEncoder.encode(request.getPassword()));
//            newUser.addRole(role);
////            newUser.setRoles(new HashSet<>());
////            newUser.getRoles().add(role);
//            //5. Guardar el usuario en la base de datos
//
//            if(userRepository.save(newUser)==null){
//                throw new handleSQLException("No se guardo el usuario "+request.getUsername());
//            }
//
////            return ResponseEntity.ok(new ApiResponse(true, "Usuario creado correctamente"));
//
//            ApiResponse response = new ApiResponse(true, "Usuario creado correctamente");
//            response.addDetail("username", newUser.getUsername());
//            response.addDetail("roles", newUser.getRoles()
//                    .stream()
//                    .map(Role::getName)
//                    .toList()
//                    );
//            return ResponseEntity.status(HttpStatus.CREATED).body(response);
//        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request){
//        Optional<User> user = userRepository.findByUsername(request.getUsername());
//        if(user.isEmpty()&&passwordEncoder.matches(request.getPassword(), user.get().getPassword())){
//            return ResponseEntity
//                    .status(HttpStatus.UNAUTHORIZED)
//                    .body(new AuthResponse( "Ususario no existe"));
//        }
//        if(!passwordEncoder.matches(request.getPassword(), user.get().getPassword())){
//            return ResponseEntity
//                    .status(HttpStatus.UNAUTHORIZED)
//                    .body(new AuthResponse("Contraseña incorrecta"));
//        }
//        return ResponseEntity.ok(new AuthResponse(jwtService.generateToken(user.get())));

        /*
        authenticationManager.authenticate(...) es el punto central de validación en Spring Security.
        Internamente llama al UserDetailService.loadUserByUsername().
        Compara la contraseña ingresada con la almacenada (mediante el PasswordEncoder).
        Si las credenciales son correctas, devuelve un Authentication lleno de datos del usuario.
        Si no lo son, lanza una excepción (BadCredentialsException).
         */
        //Pendiente controlar la excepcion BadCredentialsException y el autenticacion

        return authService.autentificacion(request);

//        try {
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(
//                            request.getUsername(),
//                            request.getPassword()
//                    ));
//            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//            String token = jwtService.generateToken(userDetails);
//            return ResponseEntity.ok(new AuthResponse(token));
//        } catch (BadCredentialsException e) {
//            //Credenciales invalidas
//            return ResponseEntity
//                    .status(HttpStatus.UNAUTHORIZED)
//                    .body(new AuthResponse("Credenciales invalidas"));
//        }
    }

    // --------------------------------------
// PENDIENTE MEJORAS EN EL REGISTRO
// 1. SOLUCIONAR ERROR RESPUESTA APIRESPONSE --> Solucionado!!!
// 2. CREAR UN AUTHSERVICE Y QUITAR LA LÓGICA DE ESTE ENDPOINT (REPOSITORY ECT...) --> Para los alumnos!
// 3. TRABAJAR CON UserAlreadyExistsException Y RoleNotFoundException --> Para los alumnos!
// 4. poder dar de alta un usuario con más de un role
// --------------------------------------


}
