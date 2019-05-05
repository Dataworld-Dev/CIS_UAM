package com.dw.ngms.cis.ftp.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.dw.ngms.cis.ftp.FTPFileService;
import com.dw.ngms.cis.ftp.FTPProperties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class FTPFileServiceImpl implements FTPFileService {

	private FTPProperties ftpProperties;
    protected FTPClient ftpClient;

    @Autowired
    public FTPFileServiceImpl(@Autowired FTPProperties ftpProperties) {
        this.ftpProperties = ftpProperties;
    }

    @PostConstruct
    public void init() {
        if (this.ftpProperties.isAutoStart()) {
            log.debug("Autostarting connection to FTP server.");
            this.open();
        }
    }

    @Override
    public boolean open() {
        close();
        log.debug("Connecting and logging in to FTP server.");
        ftpClient = new FTPClient();
        boolean loggedIn = false;
        try {
            ftpClient.connect(ftpProperties.getServer());
//            ftpClient.connect(ftpProperties.getServer(), ftpProperties.getPort());
            loggedIn = ftpClient.login(ftpProperties.getUsername(), ftpProperties.getPassword());
            if (ftpProperties.getKeepAliveTimeout() > 0)
                ftpClient.setControlKeepAliveTimeout(ftpProperties.getKeepAliveTimeout());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return loggedIn;
    }

    @Override
    public void close() {
        if (ftpClient != null) {
            try {
                ftpClient.logout();
                ftpClient.disconnect();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }//close

    @Override
    public boolean loadFile(String remotePath, OutputStream outputStream) {
        try {
            log.debug("Trying to retrieve a file from remote path " + remotePath);
            return ftpClient.retrieveFile(remotePath, outputStream);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }//loadFile

    public boolean loadFile(String remotePath, String outputStreamName){
		try {
			OutputStream outputStream = new FileOutputStream(outputStreamName);
			return loadFile(remotePath, outputStream);
		} catch (FileNotFoundException e) {
			log.error(e.getMessage(), e);
            return false;
		}        
    }//loadFile
    
    @Override
    public boolean saveFile(InputStream inputStream, String destPath, boolean append) {
        try {
            log.debug("Trying to store a file to destination path " + destPath);
            if(append)
                return ftpClient.appendFile(destPath, inputStream);
            else
                return ftpClient.storeFile(destPath, inputStream);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }//saveFile

    @Override
    public boolean saveFile(File file, String destPath, boolean append) {
    	try {
        	return this.saveFile(new FileInputStream(file), destPath, append);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return false;
        } 
    }//saveFile
    
    @Override
    public boolean saveFile(String sourcePath, String destPath, boolean append) {
        try {
        	InputStream inputStream = new ClassPathResource(sourcePath).getInputStream();
        	return this.saveFile(inputStream, destPath, append);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return false;
        }        
    }//saveFile

    @Override
    public Collection<String> listFiles(String path) throws IOException {
        FTPFile[] files = ftpClient.listFiles(path);
        return Arrays.stream(files).map(FTPFile::getName).collect(Collectors.toList());
    }//listFiles
    
    @Override
    public boolean isConnected() {
        boolean connected = false;
        if (ftpClient != null) {
            try {
                connected = ftpClient.sendNoOp();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        log.debug("Checking for connection to FTP server. Is connected: " + connected);
        return connected;
    }//isConnected
}