package mdp.tirexchageservice.processor;

import jakarta.xml.bind.JAXBException;
import lombok.RequiredArgsConstructor;
import mdp.tirexchageservice.dto.Epd015DTO;
import mdp.tirexchageservice.exceptions.SoapFaultException;
import mdp.tirexchageservice.entities.TirMessage;
import mdp.tirexchageservice.mappers.TirMessageMapper;
import mdp.tirexchageservice.respositories.TirMessageRepository;
import mdp.tirexchageservice.util.XmlUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class Epd015Processor implements TirMessageProcessor {
    private final TirMessageRepository repository;
    private final TirMessageMapper tirMessageMapper;

    @Override
    public String process(String xmlPayload) throws SoapFaultException, JAXBException {
        //From XML to DTO
        Epd015DTO dto = XmlUtils.fromXml(xmlPayload, Epd015DTO.class);
        String iru = XmlUtils.extract(xmlPayload, "IruReference");

        if (dto.getGuaranteeNumber() == null)
            throw new SoapFaultException("CLIENT_VALIDATION_ERROR", "Отсутствует элемент GuaranteeNumber");
        if (dto.getHolderNumber() == null)
            throw new SoapFaultException("CLIENT_VALIDATION_ERROR", "Отсутствует элемент HolderNumber");

        String responseXml;
        String status;
        String customsIndex = "CSTM-" + System.currentTimeMillis();

        if (dto.getGuaranteeNumber().startsWith("KG")) {
            status = "APPROVED";
            responseXml = """
                <EPD028>
                    <GuaranteeNumber>%s</GuaranteeNumber>
                    <CustomsIndex>%s</CustomsIndex>
                    <Status>APPROVED</Status>
                </EPD028>
            """.formatted(dto.getGuaranteeNumber(), customsIndex);
        } else if (dto.getGuaranteeNumber().startsWith("XX")) {
            status = "REJECTED";
            responseXml = """
                <EPD016>
                    <GuaranteeNumber>%s</GuaranteeNumber>
                    <Reason>Invalid guarantee prefix</Reason>
                </EPD016>
            """.formatted(dto.getGuaranteeNumber());
        } else {
            status = "IN_PROGRESS";
            responseXml = """
                <EPD029>
                    <GuaranteeNumber>%s</GuaranteeNumber>
                    <CustomsIndex>%s</CustomsIndex>
                    <Status>IN_PROGRESS</Status>
                </EPD029>
            """.formatted(dto.getGuaranteeNumber(), customsIndex);
        }

        TirMessage tirMessage = tirMessageMapper.toEntity(dto);
        tirMessage.setMessageType("EPD015");
        tirMessage.setGuaranteeNumber(dto.getGuaranteeNumber());
        tirMessage.setIruReference(iru);
        tirMessage.setCustomsIndex(customsIndex);
        tirMessage.setStatus(status);
        tirMessage.setPayload(xmlPayload);
        tirMessage.setCreatedAt(LocalDateTime.now());

        repository.save(tirMessage);

        return responseXml;
    }
}
