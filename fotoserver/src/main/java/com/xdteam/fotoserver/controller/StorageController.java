package com.xdteam.fotoserver.controller;

import com.xdteam.fotoserver.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class StorageController {

    @Autowired
    private StorageService storageService;

    @PostMapping("/oneFile")
    public ResponseEntity<String> uploadOneFile(@RequestParam("file") MultipartFile file, @RequestParam("seriesId") String seriesId) {
        String fileName = storageService.storeFile(file, seriesId);

        String fileUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/file/").path(fileName).toUriString();

        return ResponseEntity.ok().body(fileUri);
    }

    @PostMapping("/files")
    public ResponseEntity<String> uploadFiles(@RequestParam("files") List<MultipartFile> files, @RequestParam("seriesId") String seriesId){
        List<ResponseEntity<String>> uris = files.stream().map(file->uploadOneFile(file, seriesId)).toList();
        return ResponseEntity.ok().body(uris.toString());
    }
}
