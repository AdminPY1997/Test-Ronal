package com.test_app_movil.repository;

import com.test_app_movil.model.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {

    // Método para encontrar transacciones por rango de fechas
    List<Transaccion> findByFechaBetween(java.time.LocalDateTime fechaInicio, java.time.LocalDateTime fechaFin);

    // Método para encontrar transacciones por código de servicio
    List<Transaccion> findByServicioCodigo(String codigoServicio);
}
