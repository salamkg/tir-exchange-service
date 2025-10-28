package mdp.tirexchageservice.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@Data
@XmlRootElement(name = "EPD051")
@XmlAccessorType(XmlAccessType.FIELD)
public class Epd051DTO {

    @XmlElement(name = "GuaranteeNumber")
    private String guaranteeNumber;

    @XmlElement(name = "Reason")
    private String reason;

}
