package mdp.tirexchageservice.exceptions;

import lombok.Getter;

@Getter
public class SoapFaultException extends RuntimeException {
    private final String faultCode;
    private final String faultString;

    public SoapFaultException(String code, String message) {
        super(message);
        this.faultCode = code;
        this.faultString = message;
    }
}
