package com.sofkaprueba.myapi.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.sofkaprueba.myapi.entity.UsuarioSofka;

import reactor.core.publisher.Mono;;

/**
 * Interface que determina la conexión en memoria de bd con Mongo
 * @author Felipe Andres Jamioy Girón
 *
 */
@Repository
public interface UsuarioRepository extends ReactiveMongoRepository<UsuarioSofka, String> {
	
	/**
	 * Metodo encargado de buscar el usuario por medio del nombre
	 * @param nombreUsuario
	 * @return
	 */
	Mono<UsuarioSofka> findByNombreUsuario(String nombreUsuario);
	
}
