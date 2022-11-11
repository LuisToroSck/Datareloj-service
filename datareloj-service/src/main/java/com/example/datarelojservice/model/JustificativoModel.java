package com.example.datarelojservice.model;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JustificativoModel {  
    private String rutEmpleado;
    private int justificada;
    private Date fecha;
}
