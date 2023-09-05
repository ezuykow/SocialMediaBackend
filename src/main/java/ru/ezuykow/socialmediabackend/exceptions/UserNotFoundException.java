package ru.ezuykow.socialmediabackend.exceptions;

/**
 * @author ezuykow
 */
public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException() {
        super();
    }

    public UserNotFoundException(String msg) {
        super(msg);
    }

}
