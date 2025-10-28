package mdp.tirexchageservice.processor;

import lombok.RequiredArgsConstructor;
import mdp.tirexchageservice.entities.TirMessage;
import mdp.tirexchageservice.respositories.TirMessageRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class Epd051Processor implements TirMessageProcessor {
    private final TirMessageRepository repository;

    @Override
    public String process(String xmlPayload) {
        String guarantee = xmlPayload.contains("<GuaranteeNumber>")
                ? xmlPayload.split("<GuaranteeNumber>")[1].split("</GuaranteeNumber>")[0]
                : "UNKNOWN";

        repository.save(TirMessage.builder()
                .messageType("EPD051")
                .guaranteeNumber(guarantee)
                .status("REJECTED") //Отказано в транзите
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
