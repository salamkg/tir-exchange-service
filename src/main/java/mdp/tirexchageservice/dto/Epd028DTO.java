package mdp.tirexchageservice.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@Data
@XmlRootElement(name = "EPD028")
@XmlAccessorType(XmlAccessType.FIELD)
public class Epd028DTO {

    @XmlElement(name = "GuaranteeNumber")
    private String guaranteeNumber;


    @XmlElement(name = "CustomsIndex")
    private String customsIndex;

    @XmlElement(name = "Status")
    private String status;
}
