package jsound.exceptions;

import jsound.exceptions.codes.ErrorCodes;

public class ResourceNotFoundException extends JsoundException {

    public ResourceNotFoundException(String message) {
        super(message, ErrorCodes.RESOURCE_NOT_FOUND_ERROR_CODE);
    }
}
