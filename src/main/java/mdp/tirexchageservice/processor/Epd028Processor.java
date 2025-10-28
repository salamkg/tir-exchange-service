package mdp.tirexchageservice.processor;

import lombok.RequiredArgsConstructor;
import mdp.tirexchageservice.exceptions.SoapFaultException;
import mdp.tirexchageservice.entities.TirMessage;
import mdp.tirexchageservice.respositories.TirMessageRepository;
import mdp.tirexchageservice.util.XmlUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class Epd028Processor implements TirMessageProcessor {
    private final TirMessageRepository repository;

    @Override
    public String process(String xmlPayload) throws SoapFaultException {
        String guarantee = XmlUtils.extract(xmlPayload, "GuaranteeNumber");
        String customsIndex = XmlUtils.extract(xmlPayload, "CustomsIndex");

        if (guarantee == null)
            throw new SoapFaultException("CLIENT_VALIDATION_ERROR", "Отсутствует элемент GuaranteeNumber");
        if (customsIndex == null)
            throw new SoapFaultException("CLIENT_VALIDATION_ERROR", "Отсутствует элемент CustomsIndex");

        repository.save(TirMessage.builder()
                .messageType("EPD028")
                .guaranteeNumber(guarantee)
                .customsIndex(customsIndex)
                .status("ASSIGNED") // присвоение таможенного индекса
                .payload(xmlPayload)
                .createdAt(LocalDateTime.now())
                .build());

        return """
                <EPD028>
                    <GuaranteeNumber>%s</GuaranteeNumber>
                    <CustomsIndex>%s</CustomsIndex>
                    <Status>ASSIGNED</Status>
                </EPD028>"""
                .formatted(guarantee, customsIndex);
    }
}

