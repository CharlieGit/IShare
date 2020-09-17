package com.dp.ishare.service;

import com.dp.ishare.dao.FileInfoDao;
import com.dp.ishare.entry.FileInfo;
import com.dp.ishare.exception.FileException;
import com.dp.ishare.properties.FileProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

@Service
public class FileService {

    @Autowired
    private FileProperties fileProperties;

    @Autowired
    private FileInfoDao fileInfoDao;

    public String storeFile(MultipartFile file, String userId) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            String directory = fileProperties.getUploadDir() + userId;
            Path targetLocation = Paths.get(directory).toAbsolutePath().normalize().resolve(fileName);;
            try{
                Files.createDirectories(targetLocation);
            } catch (Exception e) {

            }
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new FileException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    /**
     * download file
     * @param fileName
     * @param userId
     * @return
     */
    public Resource loadFileAsResource(String fileName, String userId) {
        try {
            String directory = fileProperties.getUploadDir() + userId;
            Path targetLocation = Paths.get(directory).toAbsolutePath().normalize();;
            Path filePath = targetLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new FileException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new FileException("File not found " + fileName, ex);
        }
    }

    public FileInfo getFileInfoById(String fileId){
        return fileInfoDao.getById(fileId).orElse(null);
    }

}
