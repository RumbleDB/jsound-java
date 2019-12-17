package jsound.exceptions;

import jsound.exceptions.codes.ErrorCodes;

public class InvalidKindException extends JsoundException {

    public InvalidKindException(String message) {
        super(message, ErrorCodes.INVALID_KIND);
    }
}
