package org.spring.transmitter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ModelTransmitter {
    private String type;
    private int timing;
    private String signID;
    private String address;
    private String item;
    private String subItem;
}
