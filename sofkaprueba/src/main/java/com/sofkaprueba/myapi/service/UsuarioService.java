package com.sofkaprueba.myapi.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sofkaprueba.myapi.entity.UsuarioSofka;
import com.sofkaprueba.myapi.interfaces.UsuarioInterface;
import com.sofkaprueba.myapi.repository.UsuarioRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Clase en la cual se encuentra la logica de negocio para el usuario
 * @author Felipe Andres Jamioy Girón
 *
 */
@Service
public class UsuarioService implements UsuarioInterface {

	/**
	 * Conexión a bd mongo en memoría
	 */
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	/**
	 * Metodo encargado de loguear al usuario con el token
	 */
	@Override
	public Mono<UsuarioSofka> logearUsuario(UsuarioSofka usuario) {
		
		UsuarioSofka usuarioLogueado = new UsuarioSofka();
		usuarioLogueado.setIdUsuario((new Random().nextInt()));
		usuarioLogueado.setNombreUsuario(usuario.getNombreUsuario());
		usuarioLogueado.setContraseña(usuario.getContraseña());
		usuarioLogueado.setRolUsuario(usuario.getRolUsuario());
		usuarioLogueado.setTokenValidador(usuario.getTokenValidador());
		
		return usuarioRepository.insert(usuarioLogueado);
	}
	
	/**
	 * Metodo encargado de obtener los usuarios
	 */
	@Override
	public Flux<UsuarioSofka> obtenerUsuarios() {
		
		return usuarioRepository.findAll().map(usuarioMap -> {
			UsuarioSofka usuario = new UsuarioSofka();
			usuario.setIdUsuario(usuarioMap.getIdUsuario());
			usuario.setNombreUsuario(usuarioMap.getNombreUsuario());
			usuario.setContraseña(usuarioMap.getContraseña());
			usuario.setRolUsuario(usuarioMap.getRolUsuario());
			usuario.setTokenValidador(usuarioMap.getTokenValidador());			
            return usuario;
        });
	}

	/**
	 * Metodo encargado de consultar un usuario por el nombre
	 */
	@Override
	public Mono<UsuarioSofka> obtenerUsuario(String nombreUsuario) {
		return usuarioRepository.findByNombreUsuario(nombreUsuario);
	}

	/**
	 * Metodo encargado de eliminar un usuario
	 */
	@Override
	public Boolean eliminarUsuario(UsuarioSofka usuario) {
		usuarioRepository.delete(usuario);	
		return true;
	}

}
