package mdp.tirexchageservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.*;
import lombok.Data;

import java.util.List;

@Data
@XmlRootElement(name = "EPD015")
@XmlAccessorType(XmlAccessType.FIELD)
public class Epd015DTO {

    @NotBlank(message = "GuaranteeNumber обязателен")
    @XmlElement(name = "GuaranteeNumber", required = true)
    private String guaranteeNumber;

    @NotBlank(message = "IruReference обязателен")
    @XmlElement(name = "IruReference", required = true)
    private String iruReference;

    @NotBlank(message = "VehicleNumber обязателен")
    @XmlElement(name = "VehicleNumber")
    private String vehicleNumber;

    @NotBlank(message = "HolderNumber обязателен")
    @XmlElement(name = "HolderNumber")
    private String holderNumber;

    @Valid
    @NotEmpty(message = "Список товаров (Goods) обязателен")
    @XmlElementWrapper(name = "Goods")
    @XmlElement(name = "Item")
    private List<GoodsItem> goods;

    @NotEmpty(message = "Маршрут (Route) обязателен")
    @XmlElementWrapper(name = "Route")
    @XmlElement(name = "Point")
    private List<@NotBlank(message = "Point не может быть пустым") String> route;

    @Data
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class GoodsItem {
        @NotBlank(message = "HsCode обязателен")
        @XmlElement(name = "HsCode", required = true)
        private String hsCode;
        @NotNull(message = "GrossWeight обязателен")
        @XmlElement(name = "Description", required = true)
        private String description;
        @NotNull(message = "GrossWeight обязателен")
        @XmlElement(name = "GrossWeight", required = true)
        private double grossWeight;
        @NotNull(message = "Packages обязателен")
        @XmlElement(name = "Packages", required = true)
        private int packages;
    }
}
