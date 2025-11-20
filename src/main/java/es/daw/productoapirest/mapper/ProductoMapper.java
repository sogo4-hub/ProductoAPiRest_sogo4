package es.daw.productoapirest.mapper;

import es.daw.productoapirest.dto.ProductoDTO;
import es.daw.productoapirest.entity.Producto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductoMapper {

    // De la entidad al DTO: saca el id del objeto relacionado
    @Mapping(target = "codigoFabricante", source = "fabricante.codigo")
    ProductoDTO toDto(Producto entity);

    // Del DTO a la entidad: pone el id dentro del objeto relacionado
    @Mapping(target = "fabricante.codigo", source = "codigoFabricante")
    Producto toEntity(ProductoDTO dto);

    List<ProductoDTO> toDtos(List<Producto> entity);

    List<Producto> toEntitys(List<ProductoDTO> dtos);
}

