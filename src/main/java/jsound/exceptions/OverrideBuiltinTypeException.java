package jsound.exceptions;

import jsound.exceptions.codes.ErrorCodes;

public class OverrideBuiltinTypeException extends JsoundException {

    public OverrideBuiltinTypeException(String message) {
        super(message, ErrorCodes.OVERRIDE_BUILTIN_TYPES);
    }
}
