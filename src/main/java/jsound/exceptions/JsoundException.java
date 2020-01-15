package jsound.exceptions;

import jsound.exceptions.codes.ErrorCodes;


public class JsoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public JsoundException(String message) {
        super("Error [ err: " + ErrorCodes.RUNTIME_EXCEPTION_ERROR_CODE.getErrorCode() + " ] " + message);
    }

    JsoundException(String message, ErrorCodes errorCode) {
        super("Error [ err: " + errorCode.getErrorCode() + " ] " + message);
    }
}
