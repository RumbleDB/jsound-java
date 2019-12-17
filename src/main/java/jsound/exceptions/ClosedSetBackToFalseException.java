package jsound.exceptions;

import jsound.exceptions.codes.ErrorCodes;

public class ClosedSetBackToFalseException extends JsoundException {

    public ClosedSetBackToFalseException(String message) {
        super(message, ErrorCodes.CLOSED_SET_BACK_TO_FALSE);
    }
}
