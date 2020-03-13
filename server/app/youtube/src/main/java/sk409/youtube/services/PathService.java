package sk409.youtube.services;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;

@Service
public class PathService {

    public Path getImagesPath() {
        return Paths.get(getStaticPath().toString(), "images");
    }

    public Path getNoImagePath() {
        return Paths.get(getImagesPath().toString(), "no-image.png");
    }

    public Path getRelativeNoImagePath() {
        return Paths.get("images", "no-image.png");
    }

    public Path getStaticPath() {
        return Paths.get("src", "main", "resources", "static");
    }

    public Path getVideosPath() {
        return Paths.get(getStaticPath().toString(), "videos");
    }

}