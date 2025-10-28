package mdp.tirexchageservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends RuntimeException {
    @ExceptionHandler(SoapFaultException.class)
    public ResponseEntity<String> handleSoapFault(SoapFaultException ex) {
        String xml = """
            <soap:Fault>
                <faultcode>%s</faultcode>
                <faultstring>%s</faultstring>
            </soap:Fault>
        """.formatted(ex.getFaultCode(), ex.getFaultString());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(xml);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneric(Exception ex) {
        String xml = """
            <soap:Fault>
                <faultcode>SERVER_ERROR</faultcode>
                <faultstring>%s</faultstring>
            </soap:Fault>
        """.formatted(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(xml);
    }
}
