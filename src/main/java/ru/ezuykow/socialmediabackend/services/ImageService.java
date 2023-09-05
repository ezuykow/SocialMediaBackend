package ru.ezuykow.socialmediabackend.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.ezuykow.socialmediabackend.exceptions.ImageNotUploadedException;

import java.io.IOException;
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

    private void uploadFile(Path path, MultipartFile file) {
        try {
            Files.createDirectories(path.getParent());
            Files.deleteIfExists(path);
            file.transferTo(path);
        } catch (IOException e) {
            throw new ImageNotUploadedException(e.getMessage());
        }
    }
}
