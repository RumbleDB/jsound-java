package jsound.exceptions;

import jsound.exceptions.codes.ErrorCodes;

public class InvalidInstanceAgainstSchemaException extends JsoundException {

    public InvalidInstanceAgainstSchemaException(String message) {
        super(message, ErrorCodes.NOT_VALID_WHEN_ANNOTATING);
    }
}
