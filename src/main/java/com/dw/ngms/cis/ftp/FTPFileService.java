package com.dw.ngms.cis.ftp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;

public interface FTPFileService {

	/**
     * Connects to a server and tries to log in the user.
     *
     * @return boolean True if successful, False otherwise.
     */
    boolean open();

    /**
     * Logouts the current user and disconnects from the server.
     */
    void close();

    /**
     * Retrieve a file from the ftp server.
     *
     * @param remotePath   Remote path for the file to retrieve.
     * @param outputStream Stream the file is read into.
     * @return boolean True if successful, False otherwise.
     */
    boolean loadFile(String remotePath, OutputStream outputStream);
    
    /**
     * Retrieve a file from the ftp server.
     * 
     * @param remotePath   Remote path for the file to retrieve.
     * @param outputStreamName File name of the stream to read into.
     * @return boolean True if successful, False otherwise.
     */
    boolean loadFile(String remotePath, String outputStreamName);

    /**
     * Store a file on the ftp server.
     *
     * @param inputStream Stream the new file is read from.
     * @param destPath    Remote path the file should be placed at.
     * @param append      Append to an existing file or write as a new file.
     * @return boolean True if successful, False otherwise.
     */
    boolean saveFile(InputStream inputStream, String destPath, boolean append);

    /**
     * Store a file on the ftp server.
     *
     * @param file		  new file is read from.
     * @param destPath    Remote path the file should be placed at.
     * @param append      Append to an existing file or write as a new file.
     * @return boolean 	  True if successful, False otherwise.
     */
    boolean saveFile(File file, String destPath, boolean append);
    
    /**
     * Store a file on the ftp server.
     *
     * @param sourcePath Local path the file is read from.
     * @param destPath   Remote path the file should be placed at.
     * @param append      Append to an existing file or write as a new file.
     * @return boolean True if successful, False otherwise.
     */
    boolean saveFile(String sourcePath, String destPath, boolean append);

    /**
     * Retrieves the list of files in the path provided
     * 
     * @param path Remote path the files should be read from.
     * @return collection of files in the path
     */
    Collection<String> listFiles(String path) throws IOException;
    
    /**
     * Does a NOOP to see if the connection is valid.
     *
     * @return boolean True if connected, False otherwise.
     */
    boolean isConnected();
}
