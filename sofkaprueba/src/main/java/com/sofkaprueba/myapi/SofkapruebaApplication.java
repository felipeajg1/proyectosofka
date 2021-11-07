package com.sofkaprueba.myapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.sofkaprueba.myapi.security.JWTAuthorizationFilter;

/**
 * Clase encargada de subir el servidor y configurar el acceso a la aplicación
 * @author Felipe Andres Jamioy Girón
 *
 */
@SpringBootApplication
public class SofkapruebaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SofkapruebaApplication.class, args);
	}
	
	/**
	 * 
	 * Metodo encargado de dar acceso a la seguridad en la aplicación
	 *
	 */
	@EnableWebSecurity
	@Configuration
	class WebSecurityConfig extends WebSecurityConfigurerAdapter {

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.csrf().disable()
				.addFilterAfter(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
				.authorizeRequests()
				.antMatchers(HttpMethod.POST, "/controladorUsuario/login").permitAll()
				.anyRequest().authenticated();
		}
	}

}
