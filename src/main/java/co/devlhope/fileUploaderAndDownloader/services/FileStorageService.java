package co.devlhope.fileUploaderAndDownloader.services;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${fileRepositoryFolder}")
    private String fileRepositoryFolder;

    /**
     *
     * @param file File from upload controller
     * @return New file name with extension
     * @throws IOException if folder is not writable
     */
    public String upload(MultipartFile file) throws IOException {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        String newFileName = UUID.randomUUID().toString();
        String completeFileName = newFileName + "." + extension;
        File finalFolder = new File(fileRepositoryFolder);
        if(!finalFolder.exists()) throw new IOException("Final folder does not exists");
        if(!finalFolder.isDirectory()) throw new IOException("Final folder is not a directory");
        File finalDestination = new File(fileRepositoryFolder + "\\" + completeFileName);
        if(finalDestination.exists()) throw new IOException("File conflict");
        file.transferTo(finalDestination);
        return completeFileName;
    }

    /**
     Downloads a file from the file repository folder with the given file name.
     @param fileName the name of the file to download.
     @return a byte array representing the contents of the file.
     @throws IOException if the file does not exist or if an I/O error occurs while reading the file.
     */
    public byte[] download(String fileName) throws IOException {
        File fileFromRepository = new File(fileRepositoryFolder + "\\" + fileName);
        if(!fileFromRepository.exists()) throw new IOException("File does not exists");
        return IOUtils.toByteArray(new FileInputStream(fileFromRepository));
    }
}
