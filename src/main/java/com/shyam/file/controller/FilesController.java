package com.shyam.file.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.shyam.file.message.ResponseMessage;
import com.shyam.file.model.FileInfo;
import com.shyam.file.service.FileStorageService;

@RestController
@CrossOrigin
public class FilesController {

	@Autowired
	private FileStorageService storageService;

	@PostMapping("/upload")
	public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
		String message = "";
		try {
			storageService.save(file);
			message = "Uploaded The File Successfully: " + file.getOriginalFilename();
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
		} catch (Exception e) {
			message = "Could Not Upload The File: " + file.getOriginalFilename();
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
		}
	}
	
	@GetMapping("/files")
	public ResponseEntity<List<FileInfo>> getListFiles(){
		List<FileInfo> fileinfos = storageService.loadAll().map(path->{
			String filename = path.getFileName().toString();
		String url = MvcUriComponentsBuilder.fromMethodName(FilesController.class,"getFile",path.getFileName().toString()).build().toString();
		return new FileInfo(filename,url);
		}).collect(Collectors.toList());
		
		return ResponseEntity.status(HttpStatus.OK).body(fileinfos);
	}
	
	@GetMapping("/files/{filename}")
	@ResponseBody
	public ResponseEntity<Resource> getFile(@PathVariable String filename){
	Resource file = storageService.load(filename);	
	return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\""+file.getFilename()+"\"").body(file);
	}
}
