package es.daw.productoapirest.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.daw.productoapirest.dto.ProductoDTO;
import es.daw.productoapirest.entity.Fabricante;
import es.daw.productoapirest.entity.Producto;
import es.daw.productoapirest.exception.FabricanteNotFoundException;
import es.daw.productoapirest.exception.ProductoNotFoundException;
import es.daw.productoapirest.mapper.ProductoMapper;
import es.daw.productoapirest.repository.FabricanteRepository;
import es.daw.productoapirest.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.invoke.ParameterValueMapper;
//import org.springframework.data.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor //Crear un constructor con propiedades final
public class ProductoService {


    private final ProductoRepository productoRepository;
    private final FabricanteRepository fabricanteRepository;
    private final ProductoMapper productoMapper;
    @Autowired
    private ParameterValueMapper parameterValueMapper;
//    private final ProductoMapper productoMapper;
// No es necesrio el contructor con @Autowired gracias a @RequiredArgsConstructor
//    @Autowired
//    public ProductoService(ProductoRepository productoRepository, ProductoMapper productoMapper) {
//        this.productoRepository = productoRepository;
//        this.productoMapper = productoMapper;
//    }

    public List<ProductoDTO> findAll() {
        List<Producto> productosEntities = productoRepository.findAll();
        return productoMapper.toDtos(productosEntities);
    }

    public ProductoDTO findById(Integer id) {
        Producto productoEntity = productoRepository.findById(id).orElse(null);
        return productoMapper.toDto(productoEntity);
    }
    /**
     * Dar de alta un producto en la BD
     * @param productoDTO
     * @return
     */
    @Transactional
    public Optional<ProductoDTO> crearProducto(ProductoDTO productoDTO) {
        Producto productoEntity = productoMapper.toEntity(productoDTO);
        //Guardar la entidad en la base de datos
        Optional<Fabricante> fabOpt = fabricanteRepository.findById(productoDTO.getCodigoFabricante());
        if(fabOpt.isPresent()){
            productoEntity.setFabricante(fabOpt.get());
        }else{
            return Optional.empty();//si no encuentra el fabricante devuelve un optional vacio
        }
        productoEntity = productoRepository.save(productoEntity);
        return Optional.of(productoMapper.toDto(productoEntity));
    }
    @Transactional
    public void delete(Integer id) {
        productoRepository.deleteById(id);
    }
    @Transactional
    public void deleteByCodigo(String codigo) {
        Producto producto=productoRepository.findByCodigo(codigo)
                .orElseThrow(() -> new ProductoNotFoundException("El producto con codigo: " + codigo + " no existe en la base de datos"));

        productoRepository.delete(producto);

    }
    public Optional<ProductoDTO> findByCodigo(String codigo) {
        Optional<Producto> productoEntity = productoRepository.findByCodigo(codigo);
        if(productoEntity.isPresent()){
            return Optional.of(productoMapper.toDto(productoEntity.get()));
        }
        return Optional.empty();
    }

/* <<<<<<<<<<<<<<  ✨ Windsurf Command ⭐ >>>>>>>>>>>>>>>> */
    /**
     * Actualiza un producto en la BD
     * @param codigo
     * @param productoDTO
     * @return
     */
    @Transactional
/* <<<<<<<<<<  ef9a081e-63d7-4630-8c08-e1c8b1451a3d  >>>>>>>>>>> */
    public Optional<ProductoDTO> update(String codigo, ProductoDTO productoDTO) {
        Producto productoEntity = productoRepository.findByCodigo(codigo).
                orElseThrow(() -> new ProductoNotFoundException("El producto con codigo: " + codigo + " no existe en la base de datos"));
        if(productoDTO.getCodigoFabricante() != null){
            Optional<Fabricante> fabOpt = fabricanteRepository.findById(productoDTO.getCodigoFabricante());
            if(fabOpt.isPresent()){
                Producto productoEntity2 = productoMapper.toEntity(productoDTO);
                productoEntity2.setId(productoEntity.getId());
                productoEntity2 = productoRepository.save(productoEntity2);
                return Optional.of(productoMapper.toDto(productoEntity2));
            }else{
                //Lanzar fabricant no found exception y devuelbe un 400 (bad request)...me pasa un codigo de fabricante mal
                throw new FabricanteNotFoundException("El fabricante con codigo: " + productoDTO.getCodigoFabricante() + " no existe en la base de datos");
            }
        }
        return Optional.empty();
    }
    /**
     * Actualiza parcialmente un producto en la BD
     * @param codigo
     * @param productoDTO
     * @return
     */
    public Optional<ProductoDTO> updatePartial (String codigo, ProductoDTO productoDTO){
        Producto productoEntity = productoRepository.findByCodigo(codigo).
                orElseThrow(() -> new ProductoNotFoundException("El producto con codigo: " + codigo + " no existe en la base de datos"));
        if(productoDTO.getNombre()!=null) productoEntity.setNombre(productoDTO.getNombre());
        if(productoDTO.getPrecio()!=null) productoEntity.setPrecio(productoDTO.getPrecio());
        if(productoDTO.getCodigo()!=null) productoEntity.setCodigo(productoDTO.getCodigo());
        if(productoDTO.getCodigoFabricante()!=null) {
            Optional<Fabricante> fabOpt = fabricanteRepository.findById(productoDTO.getCodigoFabricante());
            if (fabOpt.isPresent()) {
                productoEntity.setFabricante(fabOpt.get());
            }else{
                //Lanzar fabricant no found exception y devuelbe un 400 (bad request)...me pasa un codigo de fabricante mal
                throw new FabricanteNotFoundException("El fabricante con codigo: " + productoDTO.getCodigoFabricante() + " no existe en la base de datos");
            }

        }

        return Optional.of(productoMapper.toDto(productoRepository.save(productoEntity)));
    }

    @Autowired
    private ObjectMapper objectMapper;
    public Optional<ProductoDTO> updateParcialReflect(String codigo, Map<String,Object> camposActualizados){
        Producto producto = productoRepository.findByCodigo(codigo)
                .orElseThrow(() -> new ProductoNotFoundException("El producto con codigo: " + codigo + " no existe en la base de datos"));
        //-----------------
        camposActualizados.forEach((key, value) -> {
            // CASO ESPECIAL: campo virtual "codigoFabricante"
            if ("codigoFabricante".equals(key)) {
                Integer idFabricante = objectMapper.convertValue(value, Integer.class);
                Fabricante fabricante = fabricanteRepository.findById(idFabricante)
                        .orElseThrow(() -> new RuntimeException(
                                "Fabricante no encontrado con id: " + idFabricante));
                producto.setFabricante(fabricante);
                return;// saltamos al siguiente campo
            }

// Resto de campos: usar reflexión
            Field field = ReflectionUtils.findField(Producto.class, key);

            if (field != null) {
                field.setAccessible(true);
                Object valorConvertido = objectMapper.convertValue(value, field.getType());
                ReflectionUtils.setField(field, producto, valorConvertido);
            } else {
                throw new IllegalArgumentException("Campo no válido: " + key);
            }
        });
        return Optional.of(productoMapper.toDto(productoRepository.save(producto)));
    }
}
