package com.xdteam.fotoserver.controller;

import com.xdteam.fotoserver.service.StorageService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.List;

@RestController
public class StorageController {

    @Autowired
    private StorageService storageService;

    @PostMapping("/oneFile")
    public ResponseEntity<String> uploadOneFile(@RequestParam("file") MultipartFile file, @RequestParam("seriesId") String seriesId) {
        String fileName = storageService.storeOneFile(file, seriesId);
        String fileUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/file/").path(fileName).toUriString();
        return ResponseEntity.ok().body(fileUri);
    }

    @PostMapping("/files")
    public ResponseEntity<String> uploadFiles(@RequestParam("files") List<MultipartFile> files, @RequestParam("seriesId") String seriesId) throws IOException {
        String pdfName = storageService.storeMultipleFiles(files,seriesId);
        String pdfUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/pdf/").path(pdfName).toUriString();
        return ResponseEntity.ok().body(pdfUri);
    }

    @GetMapping("/pdf/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) throws IOException {
        org.springframework.core.io.Resource resource = storageService.loadFileAsResource(fileName);
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            throw new IOException(ex);
        }
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}

