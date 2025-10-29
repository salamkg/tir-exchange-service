package mdp.tirexchageservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@Data
@XmlRootElement(name = "EPD016")
@XmlAccessorType(XmlAccessType.FIELD)
public class Epd016DTO {

    @NotBlank(message = "Reason обязателен")
    @XmlElement(name = "Reason", required = true)
    private String reason;

    @NotBlank(message = "Status обязателен")
    @XmlElement(name = "Status", required = true)
    private String status;
}
