package com.shyam.file;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.shyam.file.service.FileStorageService;

@SpringBootApplication
public class FileUploadBackendApplication implements CommandLineRunner{

	@Autowired
	FileStorageService storageService;
	public static void main(String[] args) {
		SpringApplication.run(FileUploadBackendApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//storageService.deleteAll();
		storageService.init();
	}

}
