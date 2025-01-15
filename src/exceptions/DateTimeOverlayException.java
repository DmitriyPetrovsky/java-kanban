package exceptions;


public class DateTimeOverlayException extends IllegalArgumentException {
    public DateTimeOverlayException() {
    }

    public DateTimeOverlayException(String message) {
        super(message);
    }
}
