package jsound.exceptions;

import jsound.exceptions.codes.ErrorCodes;

public class InvalidTypeException extends JsoundException {

    public InvalidTypeException(String message) {
        super(message, ErrorCodes.INVALID_TYPE_EXCEPTION);
    }
}
