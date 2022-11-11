package com.example.datarelojservice.service;

import com.example.datarelojservice.entity.DatarelojEntity;
import com.example.datarelojservice.repository.DatarelojRepository;
import com.example.datarelojservice.model.AutorizacionModel;
import com.example.datarelojservice.model.EmpleadoModel;
import com.example.datarelojservice.model.JustificativoModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.List;

@Service
public class DatarelojService {

    @Autowired
    DatarelojRepository dataRelojRepository;

    RestTemplate restTemplate = new RestTemplate();

    public DatarelojEntity guardarDataReloj(DatarelojEntity reloj){
        return dataRelojRepository.save(reloj);
    }

    public void guardarDatos() throws FileNotFoundException {
        String ruta = "./DATA.txt";
        File archivo = new File(ruta);

        try {
            Scanner scanner = new Scanner(archivo);
            DatarelojEntity datarelojEntity = null;
            while (scanner.hasNextLine()) {
                String linea = scanner.nextLine();
                StringTokenizer atributo = new StringTokenizer(linea, ";");
                datarelojEntity = new DatarelojEntity();

                SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy/mm/dd");
                SimpleDateFormat formatoHora = new SimpleDateFormat("hh:mm");
                while (atributo.hasMoreElements()) {

                    Date fecha = new Date(formatoFecha.parse(atributo.nextElement().toString()).getTime());
                    datarelojEntity.setFecha(fecha);

                    Time hora = new Time(formatoHora.parse(atributo.nextElement().toString()).getTime());
                    datarelojEntity.setHora(hora);

                    datarelojEntity.setRutEmpleadoReloj(atributo.nextElement().toString());
                }
                guardarDataReloj(datarelojEntity);
            }
            scanner.close();
        }
        catch (FileNotFoundException | ParseException e){
            e.printStackTrace();
        }
    }

    public List<DatarelojEntity> listarMarcasReloj(){
        return dataRelojRepository.findAll();
    }

    public List<Integer> calcularAtrasos(List<DatarelojEntity> marcasReloj, EmpleadoModel empleado){
        List<Integer> atrasos = new ArrayList<>();
        atrasos.add(0);
        atrasos.add(0);
        atrasos.add(0);

        int i=0;
        while(i<marcasReloj.size()){
            if(marcasReloj.get(i).getRutEmpleadoReloj().equals(empleado.getRutEmpleado())){
                if( (marcasReloj.get(i).getHora().getHours() == 8) && (marcasReloj.get(i).getHora().getMinutes() > 10) && (marcasReloj.get(i).getHora().getMinutes() <= 25)){
                    atrasos.set(0,atrasos.get(0)+1);
                }
                else if( (marcasReloj.get(i).getHora().getHours() == 8) && (marcasReloj.get(i).getHora().getMinutes() > 25) && (marcasReloj.get(i).getHora().getMinutes() <= 45)){
                    atrasos.set(1,atrasos.get(1)+1);
                }
                else if(((marcasReloj.get(i).getHora().getHours() == 8) && ((marcasReloj.get(i).getHora().getMinutes() > 45))) || ((marcasReloj.get(i).getHora().getHours() == 9) && (marcasReloj.get(i).getHora().getMinutes() <= 10))){
                    atrasos.set(2,atrasos.get(2)+1);
                }
            }
            i = i + 1;
        }

        return atrasos;
    }

    public void eliminarMarcasReloj(){
        dataRelojRepository.deleteAll();
    }

    public List<JustificativoModel> eliminarJustificativos(){
        List<JustificativoModel> list = new ArrayList<>();
        HttpEntity<List<JustificativoModel>> request = new HttpEntity<List<JustificativoModel>>(list);
        List<JustificativoModel> justificativos = restTemplate.postForObject("http://localhost:8004/justificativo/eliminar",request,List.class);    
        return justificativos;
    }

    public List<AutorizacionModel> eliminarAutorizaciones(){
        List<AutorizacionModel> list = new ArrayList<>();
        HttpEntity<List<AutorizacionModel>> request = new HttpEntity<List<AutorizacionModel>>(list);
        List<AutorizacionModel> autorizaciones = restTemplate.postForObject("http://localhost:8001/autorizacion/eliminar",request,List.class);    
        return autorizaciones;
    }

    public List<AutorizacionModel> calcularHorasExtras(){
        List<AutorizacionModel> list = new ArrayList<>();
        HttpEntity<List<AutorizacionModel>> request = new HttpEntity<List<AutorizacionModel>>(list);
        List<AutorizacionModel> autorizaciones = restTemplate.getForObject("http://localhost:8001/autorizacion/calcularHorasExtras",List.class);    
        return autorizaciones;
    }
    
}