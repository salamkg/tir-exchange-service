package mdp.tirexchageservice.processor;

import jakarta.xml.bind.JAXBException;
import lombok.RequiredArgsConstructor;
import mdp.tirexchageservice.dto.Epd051DTO;
import mdp.tirexchageservice.entities.TirMessage;
import mdp.tirexchageservice.exceptions.SoapFaultException;
import mdp.tirexchageservice.respositories.TirMessageRepository;
import mdp.tirexchageservice.util.XmlUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class Epd051Processor implements TirMessageProcessor {
    private final TirMessageRepository repository;

    @Override
    public String process(String xmlPayload) throws JAXBException, SoapFaultException {
        Epd051DTO dto = XmlUtils.fromXmlSecure(xmlPayload, Epd051DTO.class);

        // Валидация обязательных полей
        if (dto.getGuaranteeNumber() == null || dto.getGuaranteeNumber().isBlank()) {
            throw new SoapFaultException("CLIENT_VALIDATION_ERROR", "Отсутствует элемент GuaranteeNumber");
        }

        TirMessage tirMessage = TirMessage.builder()
                .messageType("EPD051")
                .guaranteeNumber(dto.getGuaranteeNumber())
                .status("REJECTED") // отказано в транзите
                .payload(xmlPayload)
                .createdAt(LocalDateTime.now())
                .build();

        repository.save(tirMessage);

        return """
                <EPD051>
                    <GuaranteeNumber>%s</GuaranteeNumber>
                    <Status>REJECTED</Status>
                </EPD051>"""
                .formatted(dto.getGuaranteeNumber());
    }
}
