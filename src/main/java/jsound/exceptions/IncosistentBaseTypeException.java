package jsound.exceptions;

import jsound.exceptions.codes.ErrorCodes;

public class IncosistentBaseTypeException extends JsoundException {

    public IncosistentBaseTypeException(String message) {
        super(message, ErrorCodes.BASETYPE_INCOSISTENT_WITH_KIND);
    }
}
