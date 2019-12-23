package jsound.exceptions;

import jsound.exceptions.codes.ErrorCodes;

public class TypeNotResolvedException extends JsoundException {

    public TypeNotResolvedException(String message) {
        super(message, ErrorCodes.TYPE_NOT_RESOLVED);
    }
}
