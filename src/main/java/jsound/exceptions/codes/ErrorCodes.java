package jsound.exceptions.codes;

public enum ErrorCodes {
    CLI_ERROR_CODE("cli error"),
    RUNTIME_EXCEPTION_ERROR_CODE("runtime exception"),
    RESOURCE_NOT_FOUND_ERROR_CODE("resource not found"),
    INVALID_TYPE_EXCEPTION("invalid atomicTypes"),
    UNEXPECTED_TYPE_EXCEPTION("unexpected atomicTypes"),
    INVALID_SCHEMA_EXCEPTION("invalid schema");

    private final String errorCode;

    ErrorCodes(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
