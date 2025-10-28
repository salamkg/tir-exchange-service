package mdp.tirexchageservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SoapFaultException.class)
    public ResponseEntity<String> handleSoapFault(SoapFaultException ex) {
        String soapFault =
                "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                        "  <soap:Body>\n" +
                        "    <soap:Fault>\n" +
                        "      <faultcode>" + ex.getFaultCode() + "</faultcode>\n" +
                        "      <faultstring>" + ex.getFaultString() + "</faultstring>\n" +
                        "    </soap:Fault>\n" +
                        "  </soap:Body>\n" +
                        "</soap:Envelope>";
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_XML)
                .body(soapFault);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneric(Exception ex) {
        String soapFault =
                "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                        "  <soap:Body>\n" +
                        "    <soap:Fault>\n" +
                        "      <faultcode>SERVER_ERROR</faultcode>\n" +
                        "      <faultstring>" + ex.getMessage() + "</faultstring>\n" +
                        "    </soap:Fault>\n" +
                        "  </soap:Body>\n" +
                        "</soap:Envelope>";
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_XML)
                .body(soapFault);
    }
}
