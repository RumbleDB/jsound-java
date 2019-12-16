package jsound.exceptions;

import jsound.exceptions.codes.ErrorCodes;

public class AlreadyExistingTypeException extends JsoundException {

    public AlreadyExistingTypeException(String message) {
        super(message, ErrorCodes.ALREADY_EXISTING_TYPE);
    }
}
