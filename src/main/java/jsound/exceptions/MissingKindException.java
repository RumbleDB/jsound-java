package jsound.exceptions;

import jsound.exceptions.codes.ErrorCodes;

public class MissingKindException extends JsoundException {

    public MissingKindException(String message) {
        super(message, ErrorCodes.MISSING_KIND);
    }
}
