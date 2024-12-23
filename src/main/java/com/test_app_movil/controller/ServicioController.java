package com.test_app_movil.controller;

import com.test_app_movil.model.Servicio;
import com.test_app_movil.service.ServicioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/servicios")
public class ServicioController {

    private final ServicioService servicioService;

    public ServicioController(ServicioService servicioService) {
        this.servicioService = servicioService;
    }

    @GetMapping
    public ResponseEntity<List<Servicio>> listarServicios() {
        List<Servicio> servicios = servicioService.listarServicios();
        return ResponseEntity.ok(servicios);
    }
}
