package ecarrara.eng.vilibra.domain.error;

/**
 * Simple implementation of a Default Error.
 */
public class DefaultError implements Error {

    private static final String DEFAULT_ERROR_MSG = "Unknown error";
    private final Exception exception;

    public DefaultError(Exception exception) {
        this.exception = exception;
    }

    @Override public Exception getException() {
        return this.exception;
    }

    @Override public String getMessage() {
        return (exception != null) ? this.exception.getMessage() : DEFAULT_ERROR_MSG;
    }
}
