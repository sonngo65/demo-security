package com.example.demoSecurity.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;


@Service
public class StorageFileServiceIplm implements StorageFileSevice{
	
	@Autowired
	private Path location;
	
	@Override
	@Async
	public  String store(MultipartFile file) {
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		
		try(InputStream inputFile = file.getInputStream()){
			Files.copy(inputFile, location.resolve(fileName),StandardCopyOption.REPLACE_EXISTING);
		}catch (IOException e) {
			System.out.print(e);
		}
		return fileName;
	}

	@Override
	public Stream<Path> loadAll() {
			Stream<Path> stream = null;
			try {
			return Files.walk(location, 1).filter(path -> !path.equals(location)).map(location::relativize);
			}catch(IOException e) {
				System.out.print("Store File Exception : " + e);
			}
			return stream;
	}
	@Override
	public Resource loadAsResource(String filename) {
		Resource resource = null;
		try {
			Path file = load(filename);
			 resource = (Resource) new UrlResource(file.toUri());
				
			
			
		}catch(Exception e) {
			System.out.print("could not read file " + filename);
		}
		return resource;
	}


	@Override
	public Path load(String fileName) {
		return location.resolve(fileName);
	}
	@Override
	public boolean checkException(String fileName) {
		var imgFile = new ClassPathResource("com/example/demoSecurity/upload/"+fileName);
		InputStreamResource resource = null;
		try {
			resource = new InputStreamResource(imgFile.getInputStream());

					return true;
		}catch(FileNotFoundException e) {
			return false;
		}catch(Exception e) {
			 return true;
		}
		
	}
}
