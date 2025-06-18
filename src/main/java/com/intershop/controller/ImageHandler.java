package com.intershop.controller;

import com.intershop.service.ImageService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class ImageHandler {

    ImageService imageService;

    public ImageHandler(ImageService imageService) {
        this.imageService = imageService;
    }


    public Mono<ServerResponse> getImage(ServerRequest request) {
        String imageName = request.pathVariable("imageName");
        return imageService.getImage(imageName)
                .flatMap(resource -> ServerResponse.ok()
                        .contentType(MediaType.IMAGE_JPEG) // Или определи MIME динамически
                        .bodyValue(resource))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

}
