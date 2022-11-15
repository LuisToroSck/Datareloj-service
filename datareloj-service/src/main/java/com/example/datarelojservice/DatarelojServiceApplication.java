package com.example.datarelojservice;

import javax.annotation.Resource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import com.example.datarelojservice.service.FileUploadService;

@SpringBootApplication
@EnableEurekaClient
public class DatarelojServiceApplication implements CommandLineRunner {

	@Resource
	FileUploadService fileUploadService;

	public static void main(String[] args) {
		SpringApplication.run(DatarelojServiceApplication.class, args);
	}

	// Al momento de inicializar la aplicación
	// Se borran las subidas
	// y se genera la carpeta uploads en la raiz del proyecto/contenedor
	@Override
	public void run(String... args) throws Exception {
		fileUploadService.deleteAll();
		fileUploadService.init();
	}

}
