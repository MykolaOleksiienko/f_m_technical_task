package automation.shared.library.exceptions;

public class NoSuchPageException extends RuntimeException {

    public NoSuchPageException() {
        super("There is no pages in the browser context");
    }
}
