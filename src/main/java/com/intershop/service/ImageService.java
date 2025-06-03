package com.intershop.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Service
public class ImageService {

    private final ResourceLoader resourceLoader;

    @Value("${image.path:imagesfolder}")
    private String imagePath;

    public ImageService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public Resource getImage(String filename) {
        if (filename != null) {
            try {
                Path filePath = Paths.get(imagePath).resolve(filename);
                Resource resource = new UrlResource(filePath.toUri());
                return resource;
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    public Path uploadItemImage(Path source) throws IOException {
        if (Objects.isNull(source)) {
            Path noImageTarget = Paths.get(imagePath).resolve("no_image.png");
            if (!Files.exists(noImageTarget)) {
                Resource resource = resourceLoader.getResource("classpath:static/no_image.png");
                try (InputStream noImageInputStream = resource.getInputStream()) {
                    Files.copy(noImageInputStream, noImageTarget, StandardCopyOption.REPLACE_EXISTING);
                }
            }
            return noImageTarget;
        }
        String newFileName = UUID.randomUUID() + "_" + source.getFileName();

        Path filePath = Paths.get(imagePath);
        Resource uploadResource = new UrlResource(filePath.toUri());
        File uploadDir = uploadResource.getFile();
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        Path target = Paths.get(imagePath).resolve(newFileName);
        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        return target;
    }

}
