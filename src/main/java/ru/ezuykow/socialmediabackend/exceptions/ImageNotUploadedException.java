package ru.ezuykow.socialmediabackend.exceptions;

/**
 * @author ezuykow
 */
public class ImageNotUploadedException extends RuntimeException{

    public ImageNotUploadedException() {
        super();
    }

    public ImageNotUploadedException(String msg) {
        super(msg);
    }
}
