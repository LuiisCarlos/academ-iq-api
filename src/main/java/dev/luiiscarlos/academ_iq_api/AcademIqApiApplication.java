package dev.luiiscarlos.academ_iq_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import dev.luiiscarlos.academ_iq_api.services.FileServiceImpl;

@SpringBootApplication
public class AcademIqApiApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(AcademIqApiApplication.class, args);

		FileServiceImpl fileService = context.getBean(FileServiceImpl.class);
		fileService.init();
	}

}
