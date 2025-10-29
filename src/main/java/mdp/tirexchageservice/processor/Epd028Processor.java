package mdp.tirexchageservice.processor;

import jakarta.xml.bind.JAXBException;
import lombok.RequiredArgsConstructor;
import mdp.tirexchageservice.dto.Epd028DTO;
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
    public String process(String xmlPayload) throws SoapFaultException, JAXBException {
        Epd028DTO dto = XmlUtils.fromXmlSecure(xmlPayload, Epd028DTO.class);

        // Валидация обязательных полей
        if (dto.getGuaranteeNumber() == null || dto.getGuaranteeNumber().isBlank()) {
            throw new SoapFaultException("CLIENT_VALIDATION_ERROR", "Отсутствует элемент GuaranteeNumber");
        }
        if (dto.getCustomsIndex() == null || dto.getCustomsIndex().isBlank()) {
            throw new SoapFaultException("CLIENT_VALIDATION_ERROR", "Отсутствует элемент CustomsIndex");
        }

        TirMessage tirMessage = TirMessage.builder()
                .messageType("EPD028")
                .guaranteeNumber(dto.getGuaranteeNumber())
                .customsIndex(dto.getCustomsIndex())
                .status("ASSIGNED")
                .payload(xmlPayload)
                .createdAt(LocalDateTime.now())
                .build();

        repository.save(tirMessage);

        return """
                <EPD028>
                    <GuaranteeNumber>%s</GuaranteeNumber>
                    <CustomsIndex>%s</CustomsIndex>
                    <Status>ASSIGNED</Status>
                </EPD028>"""
                .formatted(dto.getGuaranteeNumber(), dto.getCustomsIndex());
    }
}

