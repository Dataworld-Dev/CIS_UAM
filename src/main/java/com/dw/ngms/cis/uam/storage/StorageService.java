package com.dw.ngms.cis.uam.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import com.dw.ngms.cis.uam.utilities.Constants;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class StorageService {

	Logger log = LoggerFactory.getLogger(this.getClass().getName());
	private final Path rootLocation = Paths.get(Constants.uploadDirectoryPath);

	public String store(MultipartFile file) {
		String fileName  = null;
		String fileNameWithPdf = null;
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		try {
			System.out.println("File name is "+file.getName());
			 fileName = file.getOriginalFilename();
			if (fileName.indexOf(".") > 0)
				fileName = fileName.substring(0, fileName.lastIndexOf("."));
			 fileNameWithPdf = fileName+"_" +timeStamp +".pdf";
			Files.copy(file.getInputStream(), this.rootLocation.resolve(fileName+"_" +timeStamp +".pdf"));
		} catch (Exception e) {
			throw new RuntimeException("FAIL!");
		}

		return fileNameWithPdf;
	}

	public Resource loadFile(String filename) {
		try {
			Path file = rootLocation.resolve(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new RuntimeException("FAIL!");
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException("FAIL!");
		}
	}

	public void deleteAll() {
		FileSystemUtils.deleteRecursively(rootLocation.toFile());
	}

	public void init() {
		try {
			Files.createDirectory(rootLocation);
		} catch (IOException e) {
			throw new RuntimeException("Could not initialize storage!");
		}
	}
}
