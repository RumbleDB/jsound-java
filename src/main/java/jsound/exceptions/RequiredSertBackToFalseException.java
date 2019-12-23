package jsound.exceptions;

import jsound.exceptions.codes.ErrorCodes;

public class RequiredSertBackToFalseException extends JsoundException {

    public RequiredSertBackToFalseException(String message) {
        super(message, ErrorCodes.REQUIRED_SET_BACK_TO_FALSE);
    }
}
