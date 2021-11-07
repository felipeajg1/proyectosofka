package com.sofkaprueba.myapi.controller;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.sofkaprueba.myapi.entity.UsuarioSofka;
import com.sofkaprueba.myapi.security.JWTAuthorizationFilter;
import com.sofkaprueba.myapi.service.UsuarioService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.temporal.ChronoUnit;


/**
 * Clase que determina la funcionalidad propia del negocio para el Usuario
 * @author Felipe Andres Jamioy Girón
 *
 */
@RestController
@RequestMapping("/controladorUsuario")
public class ControladorUsuario {
	
	/**
	 * Atributo que hace referencia al los servicios del usuario
	 */
	@Autowired
    private UsuarioService usuarioService;
	
	/**
	 * Atributo que hace referencia al request
	 */
	@Autowired
	private HttpServletRequest request;
	
	/**
	 * Atributo que hace referencia al filtro de autorización
	 */
	private JWTAuthorizationFilter filtro = new JWTAuthorizationFilter();
	
	/**
	 * Metodo que devuelve el saludo en tres idiomas al usuario en sesión
	 * @author Felipe Andres Jamioy Girón
	 * @return saludo
	 */
    @GetMapping("/greet")
    public String saludarUsuario() { 
    	
    	UsuarioSofka usuario = convertirMonoUsuarioPorNombre(filtro.validateToken(request).getSubject());
    	return saludarTresIdiomas(usuario);
    }
    
    /**
     * Metodo encargado de dar estructura al saludo en tres idiomas para el usuario en sesión.
     * @param usuario
     * @return
     */
    private String saludarTresIdiomas(UsuarioSofka usuario) {
    	
    	String mensajeUsuario = "Id: " + usuario.getIdUsuario() + "\n" +
    			"Nombre: " + usuario.getNombreUsuario() + "\n" +
    			"Contraseña: " + usuario.getContraseña();
    	
    	return "Hello user in session... " + "\n" + mensajeUsuario + "\n" + "\n" +
    			"Hola usuario en sesion... " + "\n" + mensajeUsuario + "\n" + "\n" +
    			"Bonjour utilisateur en session... " + "\n" + mensajeUsuario;
    }
    
    /**
     * Metodo encargado de obtener la lista de usuarios logueados
     * @author Felipe Andres Jamioy Girón
     * @return lista de usuarios
     */
    @GetMapping("/list")
    public Flux<UsuarioSofka> obtenerUsuarios() { 
    	
    	UsuarioSofka usuarioEnSesion = convertirMonoUsuarioPorNombre(filtro.validateToken(request).getSubject());
    	
    	if ( usuarioEnSesion.getRolUsuario().equals("ADMINISTRADOR") ) {
    		return null;
    	}
    	
    	return usuarioService.obtenerUsuarios();
    }
    
    /**
     * Metodo encargado de loguear a un usuario
     * @param usuarioLogin usuario a loguear
     * @return información del usuario junto al token de seguridad
     */
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<UsuarioSofka> loguearUsuarioSofka(@RequestBody UsuarioSofka usuarioLogin) {
    	    	
    	if ( convertirMonoUsuarioPorNombre(usuarioLogin.getNombreUsuario()) != null ) {
    		return null;
    	}
    	
    	usuarioLogin.setTokenValidador(getJWTToken(usuarioLogin));
        return usuarioService.logearUsuario(usuarioLogin);
    } 
    
    /**
     * Metodo encargado de convertir un Mono<UsuarioSofka> a un objeto UsuarioSofka por medio del nombre
     * @param nombreUsuario
     * @return Usuario
     */
    private UsuarioSofka convertirMonoUsuarioPorNombre(String nombreUsuario) {   
    	
    	Mono<UsuarioSofka> usuarioRetornado = usuarioService.obtenerUsuario(nombreUsuario);    	
    	return usuarioRetornado.block(Duration.of(1000, ChronoUnit.MILLIS));    	
    }
    
    /**
     * Metodo encargado de eliminar el usuario en sesion
     * @author Felipe Andres Jamioy Girón
     * @return lista de usuarios
     */
    @GetMapping("/delete")
    public Boolean eliminarUsuario() { 
    	
    	UsuarioSofka usuarioEnSesion = convertirMonoUsuarioPorNombre(filtro.validateToken(request).getSubject());    	
    	return usuarioService.eliminarUsuario(usuarioEnSesion);
    }
  
    /**
     * Metodo encargado de obtener el token de seguridad
     * @param username
     * @return
     */
	private String getJWTToken(UsuarioSofka usuario) {
		String secretKey = "mySecretKey";
		List<GrantedAuthority> grantedAuthorities = AuthorityUtils
				.commaSeparatedStringToAuthorityList(usuario.getRolUsuario());
		
		String token = Jwts
				.builder()
				.setId("softtekJWT")
				.setSubject(usuario.getNombreUsuario())
				.claim("authorities",
						grantedAuthorities.stream()
								.map(GrantedAuthority::getAuthority)
								.collect(Collectors.toList()))
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 600000))
				.signWith(SignatureAlgorithm.HS512,
						secretKey.getBytes()).compact();

		return "Bearer " + token;
	}
    
}
