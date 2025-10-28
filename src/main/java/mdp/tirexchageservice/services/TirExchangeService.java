package mdp.tirexchageservice.services;

import lombok.RequiredArgsConstructor;
import mdp.tirexchageservice.processor.ProcessorFactory;
import mdp.tirexchageservice.processor.TirMessageProcessor;
import mdp.tirexchageservice.util.XmlUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TirExchangeService {
    private final ProcessorFactory factory;

    public String handleMessage(String xml) {
        String rootTag = XmlUtils.getRootElementName(xml);
        TirMessageProcessor processor = factory.getProcessor(rootTag);
        return processor.process(xml);
    }

}
