package com.test_app_movil.service;

import com.test_app_movil.model.Servicio;
import com.test_app_movil.model.Transaccion;
import com.test_app_movil.model.Usuario;
import com.test_app_movil.repository.ServicioRepository;
import com.test_app_movil.repository.TransaccionRepository;
import com.test_app_movil.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class TransaccionService {

    private static final Logger logger = LoggerFactory.getLogger(TransaccionService.class);
    private final TransaccionRepository transaccionRepository;
    private final UsuarioRepository usuarioRepository;
    private final ServicioRepository servicioRepository;

    public TransaccionService(TransaccionRepository transaccionRepository, UsuarioRepository usuarioRepository, ServicioRepository servicioRepository) {
        this.transaccionRepository = transaccionRepository;
        this.usuarioRepository = usuarioRepository;
        this.servicioRepository = servicioRepository;
    }

    public List<Transaccion> obtenerPagosPorRangoDeFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return transaccionRepository.findByFechaBetween(fechaInicio, fechaFin);
    }

    public List<Transaccion> obtenerPagosPorTipoDeServicio(String codigoServicio) {
        return transaccionRepository.findByServicioCodigo(codigoServicio);
    }

    public Transaccion pagarServicio(Long usuarioId, String codigoServicio, Double monto, String referencia) {
        logger.info("Iniciando proceso de pago: usuarioId={}, codigoServicio={}, monto={}, referencia=", usuarioId, codigoServicio, monto, referencia);

        // Validaciones de datos
        if (usuarioId == null || usuarioId <= 0) {
            throw new IllegalArgumentException("El ID del usuario es obligatorio y debe ser positivo.");
        }
        if (codigoServicio == null || codigoServicio.isEmpty()) {
            throw new IllegalArgumentException("El código del servicio es obligatorio.");
        }
        if (monto == null || monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor que cero.");
        }

        // Verificar que el usuario exista
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // Verificar que el servicio exista
        Servicio servicio = servicioRepository.findByCodigo(codigoServicio)
                .orElseThrow(() -> new IllegalArgumentException("Servicio no encontrado"));

        // Verificar saldo suficiente
        if (usuario.getSaldo() < monto) {
            logger.warn("Saldo insuficiente para usuarioId={}", usuarioId);
            throw new IllegalArgumentException("Saldo insuficiente para realizar el pago.");
        }

        // Verificar que el monto no exceda la deuda del servicio
        if (servicio.getDeuda() < monto) {
            logger.warn("El monto excede la deuda del servicio para codigoServicio={}", codigoServicio);
            throw new IllegalArgumentException("El monto excede la deuda del servicio.");
        }

        // Realizar el pago
        usuario.setSaldo(usuario.getSaldo() - monto);
        servicio.setDeuda(servicio.getDeuda() - monto);

        Transaccion transaccion = new Transaccion();
        transaccion.setUsuario(usuario);
        transaccion.setServicio(servicio);
        transaccion.setMonto(monto);
        transaccion.setReferencia(referencia);
        transaccion.setFecha(LocalDateTime.now());

        usuarioRepository.save(usuario);
        servicioRepository.save(servicio);

        logger.info("Pago realizado con éxito: usuarioId={}, codigoServicio={}, monto={}", usuarioId, codigoServicio, monto);

        return transaccionRepository.save(transaccion);
    }
}