package com.example.demoSecurity.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

	@Autowired
	private JwtTokenUtils jwtTokenUtils;
	@Lazy
	@Autowired
	public UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String authHeader = request.getHeader("Authorization") != null ? request.getHeader("Authorization") : "";
		if (!authHeader.startsWith("Bearer")) {
			filterChain.doFilter(request, response);
			return;
		}
		String jwt = authHeader.substring(7);
		if (jwt == null) {
			filterChain.doFilter(request, response);
			return;
		}
		String username = jwtTokenUtils.extractUsernameFromToken(jwt);
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			try {

				UserDetails user = userDetailsService.loadUserByUsername(username);
				if (jwtTokenUtils.isTokenValid(jwt)) {
					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user,
							null, user.getAuthorities());
					authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			} catch (Exception e) {
				System.out.print("Exception: " + e);
			}
		}
		filterChain.doFilter(request, response);

	}

}
