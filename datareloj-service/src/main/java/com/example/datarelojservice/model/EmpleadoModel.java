package com.example.datarelojservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpleadoModel {
    private String rutEmpleado;
    private String apellidos;
    private String nombres;
    private Date fechaIngreso;
    private String categoria;
    private Date fechaNacimiento;
}
