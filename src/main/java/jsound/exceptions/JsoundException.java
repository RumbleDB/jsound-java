package jsound.exceptions;

import jsound.exceptions.codes.ErrorCodes;


public class JsoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private final ErrorCodes errorCode;
    private final String errorMessage;

    public JsoundException(String message) {
        super("Error [err: " + ErrorCodes.RUNTIME_EXCEPTION_ERROR_CODE + " ] " + message);
        this.errorCode = ErrorCodes.RUNTIME_EXCEPTION_ERROR_CODE;
        this.errorMessage = message;
    }

    JsoundException(String message, ErrorCodes errorCode) {
        super("Error [err: " + errorCode + " ] " + message);
        this.errorCode = errorCode;
        this.errorMessage = message;
    }


    public String getErrorCode() {
        return errorCode.getErrorCode();
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
