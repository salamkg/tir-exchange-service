package mdp.tirexchageservice.processor;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.RequiredArgsConstructor;
import mdp.tirexchageservice.exceptions.SoapFaultException;
import mdp.tirexchageservice.models.TirMessage;
import mdp.tirexchageservice.respositories.TirMessageRepository;
import mdp.tirexchageservice.util.XmlUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class Epd015Processor implements TirMessageProcessor {
    private final TirMessageRepository repository;

    @Override
    public String process(String xmlPayload) {
        String guarantee = XmlUtils.extract(xmlPayload, "GuaranteeNumber");
        String iru = XmlUtils.extract(xmlPayload, "IruReference");
        String holder = XmlUtils.extract(xmlPayload, "HolderNumber");

        if (guarantee == null)
            throw new SoapFaultException("CLIENT_VALIDATION_ERROR", "Отсутствует элемент GuaranteeNumber");
        if (holder == null)
            throw new SoapFaultException("CLIENT_VALIDATION_ERROR", "Отсутствует элемент HolderNumber");

        String responseType;
        String responseXml;
        String status;
        String customsIndex = "CSTM-" + System.currentTimeMillis();

        if (guarantee.startsWith("KG")) {
            responseType = "EPD028";
            status = "APPROVED";
            responseXml = """
                <EPD028>
                    <GuaranteeNumber>%s</GuaranteeNumber>
                    <CustomsIndex>%s</CustomsIndex>
                    <Status>APPROVED</Status>
                </EPD028>
            """.formatted(guarantee, customsIndex);
        } else if (guarantee.startsWith("XX")) {
            responseType = "EPD016";
            status = "REJECTED";
            responseXml = """
                <EPD016>
                    <GuaranteeNumber>%s</GuaranteeNumber>
                    <Reason>Invalid guarantee prefix</Reason>
                </EPD016>
            """.formatted(guarantee);
        } else {
            responseType = "EPD029";
            status = "IN_PROGRESS";
            responseXml = """
                <EPD029>
                    <GuaranteeNumber>%s</GuaranteeNumber>
                    <CustomsIndex>%s</CustomsIndex>
                    <Status>IN_PROGRESS</Status>
                </EPD029>
            """.formatted(guarantee, customsIndex);
        }

        repository.save(TirMessage.builder()
                .messageType("EPD015")
                .guaranteeNumber(guarantee)
                .iruReference(iru)
                .customsIndex(customsIndex)
                .status(status)
                .payload(xmlPayload)
                .createdAt(LocalDateTime.now())
                .build());

        return responseXml;
    }
}
