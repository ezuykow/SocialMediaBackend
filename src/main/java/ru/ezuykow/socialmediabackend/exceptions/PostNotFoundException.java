package ru.ezuykow.socialmediabackend.exceptions;

/**
 * @author ezuykow
 */
public class PostNotFoundException extends RuntimeException{

    public PostNotFoundException() {
    }

    public PostNotFoundException(String message) {
        super(message);
    }
}
