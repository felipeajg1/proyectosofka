package com.sofkaprueba.myapi.interfaces;

import com.sofkaprueba.myapi.entity.UsuarioSofka;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Interface encargada de proveer los servicios propios del usuario
 * @author Felipe Andres Jamioy Girón
 *
 */
public interface UsuarioInterface {	
	
	/**
	 * Metodo encargado de loguear el usuario a la aplicación
	 * @param usuario a loguear
	 * @return usuario logueado
	 */
	Mono<UsuarioSofka> logearUsuario(UsuarioSofka usuario);

	/**
	 * Metodo encargado de obtener todos los usuarios logueados
	 * @return Usuarios
	 */
	Flux<UsuarioSofka> obtenerUsuarios();
	
	/**
	 * Metodo encargado de obtener todos los usuarios logueados
	 * @return Usuarios
	 */
	Mono<UsuarioSofka> obtenerUsuario(String nombreUsuario);
	
	/**
	 * Metodo encargado de eliminar el usuario logueado
	 * @return Usuarios
	 */
	Boolean eliminarUsuario(UsuarioSofka nombreUsuario);


}
