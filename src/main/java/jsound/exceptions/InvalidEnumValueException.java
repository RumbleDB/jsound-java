package jsound.exceptions;

import jsound.exceptions.codes.ErrorCodes;

public class InvalidEnumValueException extends JsoundException {

    public InvalidEnumValueException(String message) {
        super(message, ErrorCodes.INVALID_ENUM_VALUE);
    }
}
