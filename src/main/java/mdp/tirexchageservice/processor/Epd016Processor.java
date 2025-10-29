package mdp.tirexchageservice.processor;

import jakarta.xml.bind.JAXBException;
import lombok.RequiredArgsConstructor;
import mdp.tirexchageservice.dto.Epd016DTO;
import mdp.tirexchageservice.entities.TirMessage;
import mdp.tirexchageservice.respositories.TirMessageRepository;
import mdp.tirexchageservice.util.XmlUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class Epd016Processor implements TirMessageProcessor {
    private final TirMessageRepository repository;

    @Override
    public String process(String xmlPayload) throws JAXBException {
        Epd016DTO dto = XmlUtils.fromXmlSecure(xmlPayload, Epd016DTO.class);

        String guarantee = xmlPayload.contains("<GuaranteeNumber>")
                ? xmlPayload.split("<GuaranteeNumber>")[1].split("</GuaranteeNumber>")[0]
                : "UNKNOWN";

        repository.save(TirMessage.builder()
                .messageType("EPD016")
                .guaranteeNumber(guarantee)
                .status(dto.getStatus()) // отклонение
                .payload(xmlPayload)
                .createdAt(LocalDateTime.now())
                .build());

        return """
                <EPD016>
                    <GuaranteeNumber>%s</GuaranteeNumber>
                    <Status>REJECTED</Status>
                </EPD016>"""
                .formatted(guarantee);
    }
}
