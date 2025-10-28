package mdp.tirexchageservice.models.dto;

import jakarta.xml.bind.annotation.*;
import lombok.Data;

import java.util.List;

@Data
@XmlRootElement(name = "EPD015")
@XmlAccessorType(XmlAccessType.FIELD)
public class Epd015 {

    @XmlElement(name = "GuaranteeNumber", required = true)
    private String guaranteeNumber;

    @XmlElement(name = "IruReference", required = true)
    private String iruReference;

    @XmlElement(name = "VehicleNumber")
    private String vehicleNumber;

    @XmlElement(name = "HolderNumber")
    private String holderNumber;

    @XmlElementWrapper(name = "Goods")
    @XmlElement(name = "Item")
    private List<GoodsItem> goods;

    @XmlElementWrapper(name = "Route")
    @XmlElement(name = "Point")
    private List<String> route;

    @Data
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class GoodsItem {
        private String hsCode;
        private String description;
        private double grossWeight;
        private int packages;
    }
}
