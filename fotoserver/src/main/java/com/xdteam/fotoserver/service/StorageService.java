package com.xdteam.fotoserver.service;

import com.xdteam.fotoserver.exception.StorageException;
import com.xdteam.fotoserver.properties.StorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class StorageService {

    private Path fileStoragePath;
    private StorageProperties properties = new StorageProperties();

    @Autowired
    StorageService() {
        this.fileStoragePath = Paths.get(properties.getUploadDir()).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStoragePath);
        } catch (Exception e) {
            throw new StorageException("Could not create destination directory", e);
        }

    }

    private void updateStoragePath(String seriesId) {
        this.fileStoragePath = Paths.get(properties.getUploadDir() + "/" + seriesId).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStoragePath);
        } catch (Exception e) {
            throw new StorageException("Could not create destination directory", e);
        }
    }

    public String storeFile(MultipartFile file, String seriesId) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        updateStoragePath(seriesId);

        try {
            if (fileName.contains("..")) {
                throw new StorageException("Filename contains invalid path sequence " + fileName);
            }

            Path targetLocation = this.fileStoragePath.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException e) {
            throw new StorageException("", e);
        }

    }
}
