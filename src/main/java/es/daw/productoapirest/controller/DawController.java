package es.daw.productoapirest.controller;

import es.daw.productoapirest.config.DawConfig;
import es.daw.productoapirest.dto.DawResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DawController {
    //Inteccion por propiedad
//    @Autowired
    //private DawConfig dawConfig;
    //Inyeccion por contructor
    private final DawConfig dawConfig;
    @GetMapping("/values-conf-externo")
    public DawResponseDTO values() {
        return new DawResponseDTO(dawConfig.getCode(), dawConfig.getMessage());
    }

}
