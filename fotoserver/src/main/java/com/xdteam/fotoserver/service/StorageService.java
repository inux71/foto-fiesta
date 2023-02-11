package com.xdteam.fotoserver.service;


import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.xdteam.fotoserver.exception.MyFileNotFoundException;
import com.xdteam.fotoserver.exception.StorageException;
import com.xdteam.fotoserver.properties.StorageProperties;
import org.apache.commons.io.FileUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

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

    public String storeOneFile(MultipartFile file, String seriesId) {
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

    public String storeMultipleFiles(List<MultipartFile> files, String seriesId) throws IOException {

        String htmlContent = imgsToHtml(files, seriesId);

        return htmlToPdf(htmlContent, seriesId);
    }

    private String imgsToHtml(List<MultipartFile> files, String seriesId) throws IOException {
        int i = 1;
        String seriesResourcesPath =  properties.getUploadDir() + "/" + seriesId + "/";

        File htmlTemplateFile = new File("src/main/resources/static/template.html");
        String htmlContent = FileUtils.readFileToString(htmlTemplateFile);

        for (MultipartFile file : files) {
            String filePath = storeOneFile(file, seriesId);
            htmlContent = htmlContent.replace("$image" + i, filePath);
            i++;
        }
        htmlContent = htmlContent.replace("$banner", "../../img/banner.png");

        String outputHtmlPath = seriesResourcesPath + seriesId + ".html";
        File newHtmlFile = new File(outputHtmlPath);
        FileUtils.writeStringToFile(newHtmlFile, htmlContent);

        return htmlContent;
    }

    private String htmlToPdf(String htmlContent, String seriesId) throws FileNotFoundException {
        String seriesResourcesPath =  properties.getUploadDir() + "/" + seriesId + "/";
        String outputPdfPath = seriesResourcesPath + seriesId + ".pdf";
        File newPdfFile = new File(outputPdfPath);

        ConverterProperties props = new ConverterProperties();
        props.setBaseUri(seriesResourcesPath);
        HtmlConverter.convertToPdf(htmlContent, new FileOutputStream(newPdfFile), props);

        return newPdfFile.getName();
    }


    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStoragePath.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        }
    }
}
