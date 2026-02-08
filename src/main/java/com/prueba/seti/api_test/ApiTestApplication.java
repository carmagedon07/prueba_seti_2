package com.prueba.seti.api_test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

/**
 * Aplicación principal - API REST Reactiva para gestión de Franquicias
 *
 * Arquitectura: Hexagonal (Ports & Adapters)
 * Framework: Spring Boot WebFlux (Reactivo)
 * Persistencia: R2DBC con MySQL
 *
 * @author Prueba Setic 2
 */
@SpringBootApplication
@EnableR2dbcRepositories
public class ApiTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiTestApplication.class, args);
	}

}
