package htwb.ai.FaDen.utils;

public class PayloadException extends Exception {

    int errorCode;
    String errorMessage;

    public PayloadException(int errorCode, String errorMessage) {
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
