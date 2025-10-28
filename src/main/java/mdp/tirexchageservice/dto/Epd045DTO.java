package mdp.tirexchageservice.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@Data
@XmlRootElement(name = "EPD045")
@XmlAccessorType(XmlAccessType.FIELD)
public class Epd045DTO {

    @XmlElement(name = "GuaranteeNumber")
    private String guaranteeNumber;

    @XmlElement(name = "CompletionDate")
    private String completionDate;

    @XmlElement(name = "Status")
    private String status;

}
