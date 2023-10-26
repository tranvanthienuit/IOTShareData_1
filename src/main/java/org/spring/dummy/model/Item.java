package org.spring.dummy.model;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class Item {
    private String ItemName;
    private Integer element;
}
