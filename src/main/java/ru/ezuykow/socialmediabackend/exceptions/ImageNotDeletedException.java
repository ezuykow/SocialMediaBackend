package ru.ezuykow.socialmediabackend.exceptions;

/**
 * @author ezuykow
 */
public class ImageNotDeletedException extends RuntimeException{

    public ImageNotDeletedException() {
    }

    public ImageNotDeletedException(String message) {
        super(message);
    }
}
