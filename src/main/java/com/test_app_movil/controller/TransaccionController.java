package com.test_app_movil.controller;

import com.test_app_movil.model.Transaccion;
import com.test_app_movil.service.TransaccionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/pagos")
public class TransaccionController {

    private final TransaccionService transaccionService;

    public TransaccionController(TransaccionService transaccionService) {
        this.transaccionService = transaccionService;
    }

    @GetMapping("/rango-fechas")
    public ResponseEntity<?> obtenerPagosPorRangoDeFecha(
            @RequestParam String fechaInicio,
            @RequestParam String fechaFin) {
        try {
            LocalDateTime inicio = LocalDateTime.parse(fechaInicio);
            LocalDateTime fin = LocalDateTime.parse(fechaFin);

            List<Transaccion> transacciones = transaccionService.obtenerPagosPorRangoDeFecha(inicio, fin);
            return ResponseEntity.ok(transacciones);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al procesar las fechas: " + e.getMessage());
        }
    }

    @GetMapping("/tipo-servicio")
    public ResponseEntity<?> obtenerPagosPorTipoDeServicio(@RequestParam String codigoServicio) {
        List<Transaccion> transacciones = transaccionService.obtenerPagosPorTipoDeServicio(codigoServicio);
        if (transacciones.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Datos no encontrados");
        }
        return ResponseEntity.ok(transacciones);
    }

    @PostMapping
    public ResponseEntity<?> pagarServicio(
            @RequestParam Long usuarioId,
            @RequestParam String codigoServicio,
            @RequestParam Double monto,
            @RequestParam(required = false) String referencia) {
        try {
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

            Transaccion transaccion = transaccionService.pagarServicio(usuarioId, codigoServicio, monto, referencia);
            return ResponseEntity.status(HttpStatus.CREATED).body(transaccion);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado: " + e.getMessage());
        }
    }
}
