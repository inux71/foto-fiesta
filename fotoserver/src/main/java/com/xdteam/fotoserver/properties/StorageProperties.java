package com.xdteam.fotoserver.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "file")
public class StorageProperties {

    private String uploadDir = "src/main/resources/uploads";

    public String getUploadDir(){
        return this.uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }
}
