package com.example.demoSecurity.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.aspectj.weaver.NewConstructorTypeMunger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.demoSecurity.entity.Product;
import com.example.demoSecurity.entity.User;
import com.example.demoSecurity.payload.AuthRequest;
import com.example.demoSecurity.payload.ProductRequest;
import com.example.demoSecurity.payload.TokenResponse;
import com.example.demoSecurity.repository.ProductRepository;
import com.example.demoSecurity.repository.UserRepository;
import com.example.demoSecurity.security.JwtTokenUtils;
import com.example.demoSecurity.service.StorageFileSevice;
import com.example.demoSecurity.service.UserDetailService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@CrossOrigin
public class DemoController {

	@Autowired
	private UserRepository repo;

	@Autowired
	private PasswordEncoder passEncoder;

	@Autowired
	private StorageFileSevice  storageFileService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private JwtTokenUtils jwtService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private ProductRepository productRepository;

	@GetMapping
	public String hello() {
		return "hello";
	}
	
	@PostMapping("/user")
	public String createNewUser()
	{
		User user1 = new User(null, "sondepvl2", passEncoder.encode("sonkk2002"),"USER");

		repo.save(user1);
		return "add new user successful";
	}	
	@PostMapping("/products")
	public Product createNewProduct(@RequestParam("file") MultipartFile file,@RequestParam("product") String productRequest) {
		Product product = null;
		try {
		
		storageFileService.store(file);
		product = objectMapper.readValue(productRequest, Product.class); 
		
		product.setImage(file.getOriginalFilename());
		
		productRepository.save(product);		
		String image = ServletUriComponentsBuilder.fromCurrentContextPath().path("get-file/").path(product.getImage()).toUriString();
		product.setImage(image);
		}catch (JsonProcessingException e) {
		System.out.print(e);
		}
		return product;
	}
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/products")
	public ResponseEntity<List<Product>> listAllProduct(){
		List<Product> list=productRepository.findAll();
		for (Product product : list) {
			String image = ServletUriComponentsBuilder.fromCurrentContextPath().path("get-file/").path(product.getImage()).toUriString();
			product.setImage(image);
		}
		
		return ResponseEntity.ok().body(list);
	}
	
	@DeleteMapping("/products/{id}")
	public ResponseEntity<Boolean> deleteProduct(@PathVariable UUID id){
		boolean isProductExists = productRepository.existsById(id);
		if(isProductExists) {
			productRepository.deleteById(id);
			 return ResponseEntity.ok().body(true);

		}
		return ResponseEntity.ok().body(false);
	}
	
	@PostMapping("/generateToken")
	public TokenResponse authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return new TokenResponse(authRequest.getUsername(),jwtService.generateTokenJwt(authRequest.getUsername()));
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }
//		User user = (User)userDetailService.loadUserByUsername(authRequest.getUsername());
//		return user.getUsername();
	}
}
