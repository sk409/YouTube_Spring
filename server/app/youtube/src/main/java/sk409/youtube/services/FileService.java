package sk409.youtube.services;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;

@Service
public class FileService {

    public void write(String path, byte[] bytes) throws IOException {
        final File directory = new File(Paths.get(path).getParent().toString());
        directory.mkdirs();
        final File file = new File(path);
        file.createNewFile();
        final BufferedOutputStream fileStream = new BufferedOutputStream(new FileOutputStream(file));
        fileStream.write(bytes);
        fileStream.close();
    }

}