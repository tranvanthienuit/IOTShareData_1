package org.spring.dataShare.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SubItem implements Comparable<SubItem> {
    private Integer itemCharacter;
    private String dataForm;
    private Integer dataSize;
    private Address address;
    private TypeSubItem typeSubItem;

    @Override
    public String toString() {
        return itemCharacter + " " + dataSize + " " + address.toString();
    }

    @Override
    public int compareTo(SubItem subItem) {
        return this.getAddress().compareTo(subItem.getAddress());
    }
}
