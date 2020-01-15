package jsound.exceptions;

import jsound.exceptions.codes.ErrorCodes;

public class RequiredSetBackToFalseException extends JsoundException {

    public RequiredSetBackToFalseException(String message) {
        super(message, ErrorCodes.REQUIRED_SET_BACK_TO_FALSE);
    }
}
