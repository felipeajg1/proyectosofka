package com.sofkaprueba.myapi.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad encargada de persistir los datos del Usuario
 * @author Felipe Andres Jamioy Girón
 *
 */
@Document
@Data
@NoArgsConstructor
public class UsuarioSofka {

	/**
	 * Atributo que determina el id del usuario
	 */
	@Id
	private Integer idUsuario;
	
	/**
	 * Atributo que determina el nombre del usuario, unico.
	 */
	private String nombreUsuario;
	
	/**
	 * Atributo que determina la contraseña
	 */
	private String contraseña;
	
	/**
	 * Atributo el rol del usuario logueado
	 */
	private String rolUsuario;
	
	/**
	 * Atributo que determina el token para validar los datos
	 */
	private String tokenValidador;

}
