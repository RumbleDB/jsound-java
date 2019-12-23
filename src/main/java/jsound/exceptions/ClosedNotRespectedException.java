package jsound.exceptions;

import jsound.exceptions.codes.ErrorCodes;

public class ClosedNotRespectedException extends JsoundException {

    public ClosedNotRespectedException(String message) {
        super(message, ErrorCodes.CLOSED_NOT_RESPECTED);
    }
}
