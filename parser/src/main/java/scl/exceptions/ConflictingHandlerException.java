package scl.exceptions;

import java.lang.IllegalStateException;
public class ConflictingHandlerException extends IllegalStateException {
    public ConflictingHandlerException(String msg) {
        super(msg);
    }
}
