package es.daw.productoapirest.repository;

import es.daw.productoapirest.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto,Integer> {

    List<Producto> findAll();
    @Transactional
    @Modifying
    Producto save(Producto producto);

    Optional<Producto> findById(Integer id);
    @Transactional
    @Modifying
    void deleteById(Integer id);

    Optional<Producto> findByCodigo(String codigo);
    @Transactional
    @Modifying
    void deleteByCodigo(String codigo);

    @Override
    void delete(Producto producto);



}
