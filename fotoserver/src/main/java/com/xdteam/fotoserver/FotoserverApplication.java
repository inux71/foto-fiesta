package com.xdteam.fotoserver;

import com.xdteam.fotoserver.properties.StorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
        StorageProperties.class
})
public class FotoserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(FotoserverApplication.class, args);
    }

}
