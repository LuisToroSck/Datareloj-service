package com.example.datarelojservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AutorizacionModel {
    private String rutEmpleado;
    private Date fecha;
    private int cantidadHorasExtras;
    private int autorizado;
}
