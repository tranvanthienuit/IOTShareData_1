package org.spring.dataShare.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Comparator;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SubItem implements Comparable<SubItem>{
    private String itemCharacter;
    private String dataType;
    private String dataForm;
    private String dataSize;
    private Address address;

    @Override
    public String toString() {
        return itemCharacter + " " + dataType + " " + dataSize + " " + address.toString();
    }

    @Override
    public int compareTo(SubItem subItem) {
        return this.getAddress().compareTo(subItem.getAddress());
    }
}
