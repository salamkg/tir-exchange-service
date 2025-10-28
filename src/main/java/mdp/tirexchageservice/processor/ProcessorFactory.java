package mdp.tirexchageservice.processor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProcessorFactory {

    private final Epd015Processor epd015Processor;

    public TirMessageProcessor getProcessor(String rootTag) {
        return switch (rootTag) {
            case "EPD015" -> epd015Processor;
            default -> throw new IllegalArgumentException("Unknown message type: " + rootTag);
        };
    }
}
