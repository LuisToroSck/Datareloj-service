package com.example.datarelojservice.controller;

import com.example.datarelojservice.entity.DatarelojEntity;
import com.example.datarelojservice.model.AutorizacionModel;
import com.example.datarelojservice.model.JustificativoModel;
import com.example.datarelojservice.service.DatarelojService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/datareloj")
public class DatarelojController {

    @Autowired
    DatarelojService datarelojService;

    @GetMapping
    public ResponseEntity<List<DatarelojEntity>> getAll(){
        List<DatarelojEntity> marcasReloj = datarelojService.listarMarcasReloj();
        return ResponseEntity.ok(marcasReloj);
    }

    @PostMapping("/eliminarJustificativos")
    public ResponseEntity<List<JustificativoModel>> eliminarJustificativos(){
        List<JustificativoModel> eliminados = datarelojService.eliminarJustificativos();
        return ResponseEntity.ok(eliminados);
    }

    @PostMapping("/eliminarAutorizaciones")
    public ResponseEntity<List<AutorizacionModel>> eliminarAutorizaciones(){
        List<AutorizacionModel> eliminados = datarelojService.eliminarAutorizaciones();
        return ResponseEntity.ok(eliminados);
    }

    @PostMapping("/calcularHorasExtras")
    public ResponseEntity<List<AutorizacionModel>> calcularHorasExtras(){
        List<AutorizacionModel> horasExtras = datarelojService.calcularHorasExtras();
        return ResponseEntity.ok(horasExtras);
    }

    @PostMapping("/calcularInasistencias")
    public ResponseEntity<List<JustificativoModel>> calcularInasistencias(){
        List<JustificativoModel> inasistencias = datarelojService.calcularInasistencias();
        return ResponseEntity.ok(inasistencias);
    }

    @GetMapping("/cargarReloj")
    public String cargarReloj(RedirectAttributes ms) throws FileNotFoundException{
        // FALTAN MÉTODOS 
        datarelojService.eliminarJustificativos();
        datarelojService.eliminarMarcasReloj();

        datarelojService.guardarDatos();
        List<DatarelojEntity> marcasReloj = datarelojService.listarMarcasReloj();
        ms.addFlashAttribute("mensaje","Archivo subido");
        return "redirect:/";
    }
}