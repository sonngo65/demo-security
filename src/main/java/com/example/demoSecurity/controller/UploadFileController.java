package com.example.demoSecurity.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demoSecurity.payload.FileResponse;
import com.example.demoSecurity.service.StorageFileSevice;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@CrossOrigin
public class UploadFileController {

	@Autowired
	private StorageFileSevice storageFileService;

	@Autowired
	private Path storageLocation;

	@PostMapping("/upload")
	public FileResponse uploadFiles(@RequestParam("file") MultipartFile file ) {
		
		String fileName = storageFileService.store(file);
		
		return new FileResponse(fileName);
	}
	
	@GetMapping(value = "/get-file/{fileName}")
	public ResponseEntity<InputStreamResource> getImage(@PathVariable String fileName) throws IOException, InterruptedException {
		
		while(!storageFileService.checkException(fileName)) {
			Thread.sleep(200);
		}
		var imgFile = new ClassPathResource("com/example/demoSecurity/upload/"+fileName);
			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG)
					.body(new InputStreamResource(imgFile.getInputStream()));

		
		
	}

}
