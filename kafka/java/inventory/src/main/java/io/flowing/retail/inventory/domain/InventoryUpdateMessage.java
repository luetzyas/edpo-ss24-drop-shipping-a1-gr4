package io.flowing.retail.inventory.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class InventoryUpdateMessage {
    private List<StockItem> stockItems;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date ts;

    @Data
    public static class StockItem {
        private String location;
        private Workpiece workpiece;
    }

    @Data
    public static class Workpiece {
        private String id;
        private String state;
        private String type;
    }
}



