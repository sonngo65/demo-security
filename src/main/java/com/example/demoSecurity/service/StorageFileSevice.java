package com.example.demoSecurity.service;

import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

public interface StorageFileSevice {
	String store(MultipartFile file);
	Stream<Path> loadAll();
	Path load(String fileName);
	Resource loadAsResource(String filename);
	boolean checkException(String fileName);
}
