package jsound.exceptions;

import jsound.exceptions.codes.ErrorCodes;

public class CliException extends JsoundException {

    public CliException(String message) {
        super(message, ErrorCodes.CLI_ERROR_CODE);
    }
}
