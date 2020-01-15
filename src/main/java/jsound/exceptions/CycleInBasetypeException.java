package jsound.exceptions;

import jsound.exceptions.codes.ErrorCodes;

public class CycleInBasetypeException extends JsoundException {

    public CycleInBasetypeException(String message) {
        super(message, ErrorCodes.CYCLE_IN_BASETYPE);
    }
}
