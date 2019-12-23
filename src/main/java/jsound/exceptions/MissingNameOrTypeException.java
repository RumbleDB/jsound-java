package jsound.exceptions;

import jsound.exceptions.codes.ErrorCodes;

public class MissingNameOrTypeException extends JsoundException {

    public MissingNameOrTypeException(String message) {
        super(message, ErrorCodes.MISSING_NAME_OR_TYPE_OBJECT_CONTENT);
    }
}
