package mdp.tirexchageservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@Data
@XmlRootElement(name = "EPD028")
@XmlAccessorType(XmlAccessType.FIELD)
public class Epd028DTO {

    @NotBlank(message = "GuaranteeNumber обязателен")
    @XmlElement(name = "GuaranteeNumber", required = true)
    private String guaranteeNumber;

    @NotBlank(message = "CustomsIndex обязателен")
    @XmlElement(name = "CustomsIndex", required = true)
    private String customsIndex;

    @NotBlank(message = "Status обязателен")
    @XmlElement(name = "Status", required = true)
    private String status;
}
