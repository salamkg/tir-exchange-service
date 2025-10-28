package mdp.tirexchageservice.processor;

import jakarta.xml.bind.JAXBException;
import mdp.tirexchageservice.exceptions.SoapFaultException;

public interface TirMessageProcessor {
    String process(String xmlPayload) throws SoapFaultException, JAXBException;
}
