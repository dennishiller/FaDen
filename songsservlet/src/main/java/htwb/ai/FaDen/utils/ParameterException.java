package htwb.ai.FaDen.utils;

public class ParameterException extends Exception {

    int errorCode;
    String errorMessage;

    public ParameterException(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
