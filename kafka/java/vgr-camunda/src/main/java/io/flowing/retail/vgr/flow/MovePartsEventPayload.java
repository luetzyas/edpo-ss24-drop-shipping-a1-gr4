package io.flowing.retail.vgr.flow;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import lombok.NoArgsConstructor;
import lombok.Data;

@NoArgsConstructor
@Data
public class MovePartsEventPayload {

    private int active;
    private int code;
    private String description;
    private String station = "vgr"; // Assuming station is always 'vgr' for this payload
    private String target;
    private String ts;
    private String refId; // Trace or reference ID for the process

    public MovePartsEventPayload setTs(Date date) {
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        this.ts = isoFormat.format(date);
        return this;
    }

}
