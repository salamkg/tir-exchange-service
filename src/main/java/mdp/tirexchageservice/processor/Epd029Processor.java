package mdp.tirexchageservice.processor;

import lombok.RequiredArgsConstructor;
import mdp.tirexchageservice.entities.TirMessage;
import mdp.tirexchageservice.respositories.TirMessageRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class Epd029Processor implements TirMessageProcessor {
    private final TirMessageRepository repository;

    @Override
    public String process(String xmlPayload) {
        String guarantee = xmlPayload.contains("<GuaranteeNumber>")
                ? xmlPayload.split("<GuaranteeNumber>")[1].split("</GuaranteeNumber>")[0]
                : "UNKNOWN";

        repository.save(TirMessage.builder()
                .messageType("EPD029")
                .guaranteeNumber(guarantee)
                .status("AUTHORIZED") //разрешение на транзит
                .payload(xmlPayload)
                .createdAt(LocalDateTime.now())
                .build());

        return """
                <EPD051>
                    <GuaranteeNumber>%s</GuaranteeNumber>
                    <Status>AUTHORIZED</Status>
                </EPD051>"""
                .formatted(guarantee);
    }
}

