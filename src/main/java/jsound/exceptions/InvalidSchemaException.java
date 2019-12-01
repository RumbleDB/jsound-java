package jsound.exceptions;

import jsound.exceptions.codes.ErrorCodes;

public class InvalidSchemaException extends JsoundException {

    public InvalidSchemaException(String message) {
        super(message, ErrorCodes.INVALID_SCHEMA_EXCEPTION);
    }
}
