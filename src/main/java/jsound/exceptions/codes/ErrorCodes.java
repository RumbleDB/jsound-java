package jsound.exceptions.codes;

public enum ErrorCodes {
    CLI_ERROR_CODE("ABCDE123"), RUNTIME_EXCEPTION_ERROR_CODE("1234ABC");

    private final String errorCode;

    ErrorCodes(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
