package mdp.tirexchageservice.util;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mdp.tirexchageservice.exceptions.SoapFaultException;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Component
@Slf4j
@RequiredArgsConstructor
public class UniversalXmlValidator {

    private final Validator validator;

    private static final Map<String, Class<?>> DTO_MAP = new HashMap<>();

    // Добавляем все DTO для валидации
    static {
        DTO_MAP.put("EPD015", mdp.tirexchageservice.dto.Epd015DTO.class);
        DTO_MAP.put("EPD016", mdp.tirexchageservice.dto.Epd016DTO.class);
        DTO_MAP.put("EPD028", mdp.tirexchageservice.dto.Epd028DTO.class);
        DTO_MAP.put("EPD029", mdp.tirexchageservice.dto.Epd029DTO.class);
        DTO_MAP.put("EPD045", mdp.tirexchageservice.dto.Epd045DTO.class);
        DTO_MAP.put("EPD051", mdp.tirexchageservice.dto.Epd051DTO.class);
    }

    public void validate(String xml) throws SoapFaultException {
        String root = getRootTag(xml);
        Class<?> dtoClass = DTO_MAP.get(root);

        if (dtoClass == null) {
            log.warn("Валидация не поддерживается для типа: {}", root);
            return; // если нет DTO - то пропускаем валидацию
        }

        Object dto;
        try {
            JAXBContext context = JAXBContext.newInstance(dtoClass);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            dto = unmarshaller.unmarshal(new StringReader(xml));
        } catch (JAXBException e) {
            throw new SoapFaultException("CLIENT_PARSING_ERROR",
                    "Ошибка разбора XML: " + e.getMessage());
        }

        Set<ConstraintViolation<Object>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            String message = violations.stream()
                    .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                    .collect(Collectors.joining("; "));

            throw new SoapFaultException("CLIENT_VALIDATION_ERROR", message);
        }

        log.info("XML [{}] успешно прошёл валидацию", root);
    }

    private String getRootTag(String xml) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            Document document = factory.newDocumentBuilder()
                    .parse(new ByteArrayInputStream(xml.getBytes()));
            return document.getDocumentElement().getNodeName();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка определения корневого элемента XML", e);
        }
    }

}
