package org.spring.dataShare.model;

import lombok.Getter;

@Getter
public enum DataSize {
    ZERO(0),
    ONE(1),
    TWO(2),
    FOUR(4),
    SIZ(6),
    EGHT(8)
    ;

    public int getValue() {
        return value;
    }

    final int value;

    DataSize(int value) {
        this.value = value;
    }
}
