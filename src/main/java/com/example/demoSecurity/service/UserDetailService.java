package com.example.demoSecurity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.demoSecurity.repository.UserRepository;



public class UserDetailService  implements UserDetailsService{
	
	@Autowired
	private UserRepository userRepo;
	

	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDetails user = userRepo.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("not found user name"));
		return user;
	}
	
}
