package mdp.tirexchageservice.processor;

import jakarta.xml.bind.JAXBException;
import lombok.RequiredArgsConstructor;
import mdp.tirexchageservice.dto.Epd029DTO;
import mdp.tirexchageservice.entities.TirMessage;
import mdp.tirexchageservice.exceptions.SoapFaultException;
import mdp.tirexchageservice.respositories.TirMessageRepository;
import mdp.tirexchageservice.util.XmlUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class Epd029Processor implements TirMessageProcessor {
    private final TirMessageRepository repository;

    @Override
    public String process(String xmlPayload) throws JAXBException, SoapFaultException {
        Epd029DTO dto = XmlUtils.fromXmlSecure(xmlPayload, Epd029DTO.class);

        // Валидация обязательных полей
        if (dto.getGuaranteeNumber() == null || dto.getGuaranteeNumber().isBlank()) {
            throw new SoapFaultException("CLIENT_VALIDATION_ERROR", "Отсутствует элемент GuaranteeNumber");
        }
        if (dto.getStatus() == null || dto.getStatus().isBlank()) {
            throw new SoapFaultException("CLIENT_VALIDATION_ERROR", "Отсутствует элемент Status");
        }

        TirMessage tirMessage = TirMessage.builder()
                .messageType("EPD029")
                .guaranteeNumber(dto.getGuaranteeNumber())
                .status("AUTHORIZED")
                .payload(xmlPayload)
                .createdAt(LocalDateTime.now())
                .build();

        repository.save(tirMessage);

        return """
                <EPD029>
                    <GuaranteeNumber>%s</GuaranteeNumber>
                    <Status>AUTHORIZED</Status>
                </EPD029>"""
                .formatted(dto.getGuaranteeNumber());
    }
}

