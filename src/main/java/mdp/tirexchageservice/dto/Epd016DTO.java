package mdp.tirexchageservice.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@Data
@XmlRootElement(name = "EPD016")
@XmlAccessorType(XmlAccessType.FIELD)
public class Epd016DTO {

    @XmlElement(name = "Reason")
    private String reason;

    @XmlElement(name = "Status")
    private String status;
}
