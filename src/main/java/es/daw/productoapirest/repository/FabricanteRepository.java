package es.daw.productoapirest.repository;

import es.daw.productoapirest.entity.Fabricante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FabricanteRepository extends JpaRepository<Fabricante, Integer> {
    Optional<Fabricante> findById(Integer id);
}