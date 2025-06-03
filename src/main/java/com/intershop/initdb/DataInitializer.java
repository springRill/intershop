package com.intershop.initdb;

import com.intershop.domain.Item;
import com.intershop.repository.ItemRepository;
import com.intershop.service.ImageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Component
public class DataInitializer implements CommandLineRunner {

    ItemRepository itemRepository;

    ImageService imageService;

    @Value("${image.path:items}")
    private String itemsDirectory;

    public DataInitializer(ItemRepository itemRepository, ImageService imageService) {
        this.itemRepository = itemRepository;
        this.imageService = imageService;
    }

    @Override
    public void run(String... args) throws Exception {
        Path rootDir = Paths.get(itemsDirectory);

        List<Path> sourceItemFolders = Files.list(rootDir)
                .filter(Files::isDirectory)
                .filter(path -> path.getFileName().toString().matches("\\d+"))
                .sorted(Comparator.comparingInt((Path path) -> Integer.parseInt(path.getFileName().toString())))
                .toList();

        for(Path sourceItemPath: sourceItemFolders){

            Path sourceImagePath = getImagePath(sourceItemPath);
            Path targetImagePath = imageService.uploadItemImage(sourceImagePath);

            Path filePath = sourceItemPath.resolve("item.txt");
            if (Files.exists(filePath)){
                List<String> lines = Files.readAllLines(filePath);

                Item item = new Item();
                item.setTitle(lines.get(0));
                item.setDescription(lines.get(1));
                item.setImgPath(targetImagePath.getFileName().toString());
                item.setPrice(Double.valueOf(lines.get(2)));

                itemRepository.save(item);
            }
        }

    }

    private Path getImagePath(Path itemPath) throws IOException {
        try (Stream<Path> paths = Files.walk(itemPath)) {
            return paths
                    .filter(Files::isRegularFile)
                    .filter(this::isImageFile)
                    .findFirst().orElse(null);
        }
    }

    private boolean isImageFile(Path path) {
        String fileName = path.toString().toLowerCase();
        return fileName.endsWith(".jpg") || fileName.endsWith(".png") || fileName.endsWith(".jpeg") || fileName.endsWith(".gif") || fileName.endsWith(".webp");
    }

}
