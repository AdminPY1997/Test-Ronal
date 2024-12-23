package com.test_app_movil.repository;

import com.test_app_movil.model.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Long> {
    Optional<Servicio> findByCodigo(String codigo);
}
