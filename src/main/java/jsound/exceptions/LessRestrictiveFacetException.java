package jsound.exceptions;

import jsound.exceptions.codes.ErrorCodes;

public class LessRestrictiveFacetException extends JsoundException {

    public LessRestrictiveFacetException(String message) {
        super(message, ErrorCodes.LESS_RESTRICTIVE_FACET);
    }
}
