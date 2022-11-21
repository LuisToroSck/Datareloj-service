package com.example.datarelojservice.service;

import com.example.datarelojservice.entity.DatarelojEntity;
import com.example.datarelojservice.repository.DatarelojRepository;
import com.example.datarelojservice.model.AutorizacionModel;
import com.example.datarelojservice.model.EmpleadoModel;
import com.example.datarelojservice.model.JustificativoModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.List;

@Service
public class DatarelojService {

    @Autowired
    DatarelojRepository dataRelojRepository;

    @Autowired
    FileUploadService fileUploadService;

    @Autowired
    RestTemplate restTemplate;

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

    public List<Integer> calcularAtrasos(Long id){

        EmpleadoModel[] empleados = getEmpleados();
        EmpleadoModel empleado = new EmpleadoModel();
        int i = 0;
        while(i<empleados.length){
            if(empleados[i].getId()==id){
                empleado = empleados[i];
            }
            i = i + 1;
        }

        List<DatarelojEntity> marcasReloj = listarMarcasReloj();

        List<Integer> atrasos = new ArrayList<>();
        atrasos.add(0);
        atrasos.add(0);
        atrasos.add(0);

        i=0;
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
        List<JustificativoModel> justificativos = restTemplate.postForObject("http://justificativo-service/justificativo/eliminar",request,List.class);    
        return justificativos;
    }

    public List<AutorizacionModel> eliminarAutorizaciones(){
        List<AutorizacionModel> list = new ArrayList<>();
        HttpEntity<List<AutorizacionModel>> request = new HttpEntity<List<AutorizacionModel>>(list);
        List<AutorizacionModel> autorizaciones = restTemplate.postForObject("http://autorizacion-service/autorizacion/eliminar",request,List.class);    
        return autorizaciones;
    }

    public List<AutorizacionModel> calcularHorasExtras(){
        List<AutorizacionModel> list = new ArrayList<>();
        HttpEntity<List<AutorizacionModel>> request = new HttpEntity<List<AutorizacionModel>>(list);
        List<AutorizacionModel> autorizaciones = restTemplate.getForObject("http://autorizacion-service/autorizacion/calcularHorasExtras",List.class);    
        return autorizaciones;
    }

    public List<JustificativoModel> calcularInasistencias(){
        List<JustificativoModel> list = new ArrayList<>();
        HttpEntity<List<JustificativoModel>> request = new HttpEntity<List<JustificativoModel>>(list);
        List<JustificativoModel> justificativos = restTemplate.getForObject("http://justificativo-service/justificativo/calcularInasistencias",List.class);    
        return justificativos;
    }
    
    public EmpleadoModel[] getEmpleados(){
        EmpleadoModel[] empleados = restTemplate.getForObject("http://empleado-service/empleado", EmpleadoModel[].class);
        return empleados;
    }

    public void uploadClockFile(String filename) {
        try {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy/mm/dd");
            SimpleDateFormat formatoHora = new SimpleDateFormat("hh:mm");

            // Cargar archivo guardado
            Resource data =  fileUploadService.load(filename);
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(data.getInputStream())
            );

            // Lectura de archivo
            String line = reader.readLine();
            while (line != null) {
                String[] values = line.split(";");
                DatarelojEntity marcaReloj = new DatarelojEntity();

                Date fecha = new Date(formatoFecha.parse(values[0]).getTime());
                marcaReloj.setFecha(fecha);
                //marcaReloj.setDate(LocalDate.parse(values[0], formatter));

                Time hora = new Time(formatoHora.parse(values[1]).getTime());
                marcaReloj.setHora(hora);
                //marcaReloj.setTime(values[1]);

                marcaReloj.setRutEmpleadoReloj(values[2]);

                // Guardar en BD
                dataRelojRepository.save(marcaReloj);

                // Siguiente linea
                line = reader.readLine();
            }
            // Cerrar archivo
            reader.close();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}