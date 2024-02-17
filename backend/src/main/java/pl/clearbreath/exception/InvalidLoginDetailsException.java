package pl.clearbreath.exception;

public class InvalidLoginDetailsException extends RuntimeException {
    public InvalidLoginDetailsException(String message) {
        super(message);
    }
}

