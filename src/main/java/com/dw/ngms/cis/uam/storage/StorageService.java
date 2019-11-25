package com.dw.ngms.cis.uam.storage;

import com.dw.ngms.cis.uam.configuration.ApplicationPropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class StorageService {

    @Autowired
    private ApplicationPropertiesConfiguration applicationPropertiesConfiguration;

    Logger log = LoggerFactory.getLogger(this.getClass().getName());
    //private final Path rootLocation = Paths.get(applicationPropertiesConfiguration.getUploadDirectoryPath());
    //private final Path rootLocationFTP = Paths.get(applicationPropertiesConfiguration.getUploadDirectoryPathFTP());


    public String store(MultipartFile file) {
        String fileName = null;
        String fileNameWithPdf = null;
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        try {
            System.out.println("File name is " + file.getOriginalFilename());
            fileName = file.getOriginalFilename();
            String subString = fileName.substring(fileName.lastIndexOf("."));
           // System.out.println("subString name is " + subString);
            if (fileName.indexOf(".") > 0)
                fileName = fileName.substring(0, fileName.lastIndexOf("."));
            fileNameWithPdf = fileName + "_" + timeStamp + subString;
            System.out.println("fileNameWithPdf " + fileNameWithPdf);
            Files.copy(file.getInputStream(), Paths.get(applicationPropertiesConfiguration.getUploadDirectoryPath()).resolve(fileName + "_" + timeStamp  + subString));
        } catch (Exception e) {
            throw new RuntimeException("FAIL!");
        }

        return fileNameWithPdf;
    }


    public String storeFtpFiles(MultipartFile file) {
        String fileName = null;
        String fileNameWithPdf = null;
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        try {
            System.out.println("File name is " + file.getName());
            fileName = file.getOriginalFilename();
            if (fileName.indexOf(".") > 0)
                fileName = fileName.substring(0, fileName.lastIndexOf("."));
            fileNameWithPdf = fileName + "_" + timeStamp + ".pdf";
            Files.copy(file.getInputStream(), Paths.get(applicationPropertiesConfiguration.getUploadDirectoryPathFTP()).resolve(fileName + "_" + timeStamp + ".pdf"));
        } catch (Exception e) {
            throw new RuntimeException("FAIL!");
        }

        return fileNameWithPdf;
    }

    public Resource loadFile(String filename) {
        try {
            Path file = Paths.get(applicationPropertiesConfiguration.getUploadDirectoryPath()).resolve(filename);
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
        FileSystemUtils.deleteRecursively(Paths.get(applicationPropertiesConfiguration.getUploadDirectoryPath()).toFile());
    }

    public void init() {
        try {
            Files.createDirectory(Paths.get(applicationPropertiesConfiguration.getUploadDirectoryPath()));
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage!");
        }
    }
}
