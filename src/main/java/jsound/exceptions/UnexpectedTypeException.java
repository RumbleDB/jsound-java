package jsound.exceptions;

import jsound.exceptions.codes.ErrorCodes;

public class UnexpectedTypeException extends JsoundException {

    public UnexpectedTypeException(String message) {
        super(message, ErrorCodes.UNEXPECTED_TYPE_EXCEPTION);
    }
}
