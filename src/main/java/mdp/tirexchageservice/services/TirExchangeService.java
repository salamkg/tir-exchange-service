package mdp.tirexchageservice.services;

import jakarta.xml.bind.JAXBException;
import lombok.RequiredArgsConstructor;
import mdp.tirexchageservice.entities.TirMessage;
import mdp.tirexchageservice.exceptions.SoapFaultException;
import mdp.tirexchageservice.processor.ProcessorFactory;
import mdp.tirexchageservice.processor.TirMessageProcessor;
import mdp.tirexchageservice.respositories.TirMessageRepository;
import mdp.tirexchageservice.util.XmlUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TirExchangeService {
    private final ProcessorFactory factory;
    private final TirMessageRepository tirMessageRepository;

    public String handleMessage(String xml) throws SoapFaultException, JAXBException {
        String rootTag = XmlUtils.getRootElementName(xml);
        TirMessageProcessor processor = factory.getProcessor(rootTag);
        return processor.process(xml);
    }

    public List<TirMessage> getMessages(int page, int size) {
        List<TirMessage> allMessages = tirMessageRepository.findAll();
        int start = page * size;
        int end = Math.min(start + size, allMessages.size());

        List<TirMessage> paginatedMessages = allMessages.subList(start, end);

        return paginatedMessages;
    }

}
