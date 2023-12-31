package org.spring.dataShare.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Comparator;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Address implements Comparable<Address> {
    private String variable;

    private String addressItem;

    private String bit;

    @Override
    public String toString() {
        return variable + " " + addressItem + " " + bit;
    }

    @Override
    public int compareTo(Address address) {
        return Comparator.comparing(Address::getVariable)
                .thenComparing(Address::getAddressItem)
                .thenComparing(Address::getBit, Comparator.nullsFirst(Comparator.naturalOrder()))
                .compare(this, address);
    }
}
