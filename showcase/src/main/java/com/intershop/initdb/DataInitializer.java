package com.intershop.initdb;

import com.intershop.domain.Item;
import com.intershop.repository.ItemRepository;
import com.intershop.service.ImageService;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Profile("!test & !redisTest")
public class DataInitializer implements CommandLineRunner {

    private final ItemRepository itemRepository;

    private final ImageService imageService;

    private final ConnectionFactory connectionFactory;


    @Value("${items.path:items}")
    private String itemsDirectory;

    public DataInitializer(ItemRepository itemRepository, ImageService imageService, ConnectionFactory connectionFactory) {
        this.itemRepository = itemRepository;
        this.imageService = imageService;
        this.connectionFactory = connectionFactory;
    }

    @Override
    public void run(String... args) throws Exception {
        executeSqlFromFile("schema.sql");

        Path rootDir = Paths.get(itemsDirectory);

        List<Path> sourceItemFolders;
        try (Stream<Path> stream = Files.list(rootDir)) {
            sourceItemFolders = stream
                    .filter(Files::isDirectory)
                    .filter(path -> path.getFileName().toString().matches("\\d+"))
                    .sorted(Comparator.comparingInt((Path path) -> Integer.parseInt(path.getFileName().toString())))
                    .toList();
        }

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

                itemRepository.save(item).block();
            }
        }

    }

    private void executeSqlFromFile(String filename) throws Exception {
        String sql;
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new ClassPathResource(filename).getInputStream(), StandardCharsets.UTF_8))) {
            sql = reader.lines().collect(Collectors.joining("\n"));
        }

        for (String statement : sql.split(";")) {
            String trimmed = statement.trim();
            if (!trimmed.isEmpty()) {
                DatabaseClient.create(connectionFactory)
                        .sql(trimmed)
                        .then()
                        .block();
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
