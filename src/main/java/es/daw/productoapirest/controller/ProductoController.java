package es.daw.productoapirest.controller;

import es.daw.productoapirest.dto.ProductoDTO;
import es.daw.productoapirest.service.ProductoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor // crear un constructor con propiedades final
@RestController
@RequestMapping("/api/productos")
@Validated
public class ProductoController {

    // 1. inyección por propiedad
    // - Viola la separación de responsabilidades (`SRP`, Single Responsibility Principle) porque el controlador estaría
    // asumiendo parte de la lógica de negocio (manejar el flujo de datos entre el DTO y la entidad).
    // - Escalar y mantener el código podría ser más complicado si en el futuro necesitas lógica de negocio adicional (tendrás que refactorizar para introducir un servicio).
    // Casos muy simples, como un proyecto pequeño de una clase o ejemplos de demostración rápida.
//    @Autowired
//    private ProductoRepository productoRepository;

    // - Al declarar las dependencias como `final`, te aseguras de que éstas se inicialicen una única vez (en el constructor) y no puedan ser sobrescritas.
    //  Esto mejora la estabilidad del código al garantizar que las dependencias no cambien.
    // - Puedes crear fácilmente objetos de prueba y pasar implementaciones "mock" o "fake" a los tests unitarios de la clase.
    // - No dependes de un framework como Spring para inicializar las dependencias en el momento de las pruebas.
    // - Con la inyección por constructor, es inmediato saber cuáles son las dependencias que necesita una clase,
    // ya que están listadas explícitamente en la firma del constructor. Esto evita confusión sobre cómo y cuándo se inicializan.
    // 2. inyección por constructor

    private final ProductoService productoService;

    // ----------------------------------
    // CONFIGURACIÓN PERSONALIZADA
    @Value("${config.daw.code}")
    private String code_conf;
    @Value("${config.daw.message}")
    private String message_conf;
    //-------------------------------------
//    @GetMapping("/list")
    @GetMapping()
    public ResponseEntity<List<ProductoDTO>> findAll() {
        return ResponseEntity.ok(productoService.findAll());
    }
    //    @GetMapping("/findId/{id}")
    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> findById(@PathVariable Integer id) {
        if(productoService.findById(id)==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productoService.findById(id));
    }

    @GetMapping("/findCodigo/{codigo}")
    public ResponseEntity<ProductoDTO> findByCodigo(@Pattern(regexp = "^[0-9]{3}[A-Z]$",
            message = "El codigo debe tener 3 digitos y una letra") @PathVariable String codigo) {
        Optional<ProductoDTO> prodDTO=productoService.findByCodigo(codigo);
        if(prodDTO.isEmpty()) {
            return ResponseEntity.notFound().build(); //Devuelve un 404 NOT FOUND
        }
        return ResponseEntity.ok(prodDTO.get()); //Devuelve un 200 OK
    }

//    @PostMapping("/add")
    @PostMapping()
    public ResponseEntity<ProductoDTO> crearProducto(@Valid @RequestBody ProductoDTO productoDTO) {
        //Cuando se da de alta un
        Optional<ProductoDTO> creado=productoService.crearProducto(productoDTO);
        if(creado.isPresent()){
            return ResponseEntity.status(201).body(creado.get());
        }
            return ResponseEntity.notFound().build();
    }


//    @DeleteMapping("/delete/{id}")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        productoService.delete(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/deleteCodigo/{codigo}")
    public ResponseEntity<Void> deleteByCodigo(@PathVariable
           @Pattern(
               regexp = "^[0-9]{3}[A-Z]$",
                message = "El codigo debe empezar tener 3 digitos y una letra mayuscual")String codigo  ) {
        productoService.deleteByCodigo(codigo);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();//204
    }

    @GetMapping("/parse-int")
    public String parseInteger(@RequestParam(name="numero", defaultValue="666") String number) {
        int parsedNumber = Integer.parseInt(number); // Puede lanzar NumberFormatException
        return "Parsed number: " + parsedNumber;
    }


    @PutMapping("/{codigo}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductoDTO> update(@PathVariable String codigo,
                                              @Valid @RequestBody ProductoDTO productoDTO) {
        Optional<ProductoDTO> actualizado = productoService.update(codigo, productoDTO);
        if (actualizado.isPresent()) {
            return ResponseEntity.ok(actualizado.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/dto/{codigo}")
    public ResponseEntity<ProductoDTO> updatePartial(@PathVariable String codigo,
                                                     @Valid @RequestBody ProductoDTO productoDTO) {
        Optional<ProductoDTO> actualizado = productoService.updatePartial(codigo, productoDTO);
        if (actualizado.isPresent()) {
            return ResponseEntity.ok(actualizado.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{codigo}")
    public ResponseEntity<ProductoDTO> updatePartial(@PathVariable String codigo,
                                                     @Valid @RequestBody Map<String,Object> propiedades) {
    /*
    REFLEXIÓN!!!!
    Se utiliza para acceder y manipular campos (atributos) de una clase mediante la reflexión.
    La reflexión es una poderosa característica de Java que te permite inspeccionar, analizar y
    modificar la estructura y el comportamiento de clases, métodos, constructores y
    campos en tiempo de ejecución.
    */
    // pendiente con reflexión
        Optional<ProductoDTO> dto= productoService.updateParcialReflect(codigo,propiedades);
        if (dto.isPresent()) {
            return ResponseEntity.ok(dto.get());
        }
        return ResponseEntity.notFound().build();
    }
//--------------------ENDPOINTS DE CONFIGURACION DE PORPIEDADES
    @GetMapping("/values-conf")
    public Map<String,String> values(){
        Map<String,String> json = new HashMap<>();
        json.put("code",code_conf);
        json.put("message",message_conf);
        return json;
    }


    @GetMapping("/values-conf2")
    public Map<String,String> values(@Value("${config.daw.code}") String code, @Value("${config.daw.message}") String message){
        Map<String,String> json = new HashMap<>();
        json.put("code",code);
        json.put("message",message);
        return json;
    }

}