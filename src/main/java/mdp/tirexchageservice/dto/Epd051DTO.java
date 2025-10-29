package mdp.tirexchageservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@Data
@XmlRootElement(name = "EPD051")
@XmlAccessorType(XmlAccessType.FIELD)
public class Epd051DTO {

    @NotBlank(message = "GuaranteeNumber обязателен")
    @XmlElement(name = "GuaranteeNumber", required = true)
    private String guaranteeNumber;

    @NotBlank(message = "Reason обязателен")
    @XmlElement(name = "Reason", required = true)
    private String reason;

}
