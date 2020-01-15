package jsound.exceptions;

import jsound.exceptions.codes.ErrorCodes;

public class InconsistentBaseTypeException extends JsoundException {

    public InconsistentBaseTypeException(String message) {
        super(message, ErrorCodes.BASETYPE_INCONSISTENT_WITH_KIND);
    }
}
