package mdp.tirexchageservice.processor;

import lombok.extern.slf4j.Slf4j;
import mdp.tirexchageservice.exceptions.SoapFaultException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ProcessorFactory {

    private final Map<String, TirMessageProcessor> processorMap;

    public ProcessorFactory(List<TirMessageProcessor> processors) {
        processorMap = processors.stream()
                .collect(Collectors.toMap(
                        p -> p.getClass().getSimpleName().replace("Processor", "").toUpperCase(),
                        p -> p
                ));

        log.info("Registered processors: {}" , processorMap.keySet());
    }

    public TirMessageProcessor getProcessor(String rootTag) throws SoapFaultException {
        TirMessageProcessor processor = processorMap.get(rootTag.toUpperCase());
        if (processor == null) {
            throw new SoapFaultException("SERVER_ERROR", "Unknown message type: " + rootTag);
        }
        return processor;
    }
}
