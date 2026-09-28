package moinammaoueni.kmtech.api.common.exception;

public class EmailNotVerifiedException extends RuntimeException {

    public EmailNotVerifiedException() {
        super("Email not verified");
    }
}