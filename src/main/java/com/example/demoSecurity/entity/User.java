package com.example.demoSecurity.entity;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Id;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails{
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID  Id;
	@Column(nullable = false,length=50,unique=true)
	private String username;
	@Column(nullable = false,length=60)
	private String password;
	
	private String role;
	
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		return Arrays.stream(role.split(",")).map(t->new SimpleGrantedAuthority(t)).collect(Collectors.toList());
	}
	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
	
	
}
