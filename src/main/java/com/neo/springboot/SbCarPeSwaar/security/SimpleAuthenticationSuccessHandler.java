package com.neo.springboot.SbCarPeSwaar.security;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class SimpleAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		Set<String> authorities = authentication.getAuthorities()
				.stream().map(auth -> auth.toString()).collect(Collectors.toSet());
		
		System.out.println(authorities);
		
		
		
		authorities.forEach(authority -> {
			if(authority.equals("ROLE_CUSTOMER")) {
				try {
					response.sendRedirect("/customer");
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if(authority.equals("ROLE_ADMIN")) {
				try {
					response.sendRedirect("/admin");
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if(authority.equals("ROLE_DEALER")) {
				try {
					response.sendRedirect("/dealer");
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
	            throw new IllegalStateException();
	        }
		});
		

		
	}

}
