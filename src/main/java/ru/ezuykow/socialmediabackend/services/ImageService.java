package ru.ezuykow.socialmediabackend.services;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author ezuykow
 */
@Service
public class ImageService {

    @Value("${image.path}")
    private String imageDirPath;
    @Value("${image.extension}")
    private String imageExtension;

    //-----------------API START-----------------

    public String uploadImage(long postId, MultipartFile image) {
        String fileName = postId + imageExtension;
        Path imageFilePath = Path.of(imageDirPath, fileName);
        uploadFile(imageFilePath, image);
        return fileName;
    }

    //-----------------API END-----------------

    @SneakyThrows
    private void uploadFile(Path path, MultipartFile file) {
        Files.createDirectories(path.getParent());
        Files.deleteIfExists(path);
        file.transferTo(path);
    }
}
