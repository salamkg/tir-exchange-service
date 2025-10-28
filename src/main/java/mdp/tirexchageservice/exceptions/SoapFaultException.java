package mdp.tirexchageservice.exceptions;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;

@Getter
@XmlRootElement(name = "soap:Fault")
@XmlAccessorType(XmlAccessType.FIELD)
public class SoapFaultException extends Exception {
    @XmlElement
    private String faultCode;
    @XmlElement
    private String faultString;

    public SoapFaultException() {} // JAXB

    public SoapFaultException(String faultCode, String faultString) {
        super(faultString);
        this.faultCode = faultCode;
        this.faultString = faultString;
    }
}
