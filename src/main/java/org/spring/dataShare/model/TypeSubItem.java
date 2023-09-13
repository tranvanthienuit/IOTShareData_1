package org.spring.dataShare.model;

import lombok.Getter;

@Getter
public enum TypeSubItem {
    UI1(17, 1),
    UI2(18, 2),
    UI4(19, 4),
    UI8(21, 8),
    BSTR(8, 8);
    private final int value;
    private final int offset;

    public int getValue() {
        return value;
    }

    public int getOffset() {
        return offset;
    }

    TypeSubItem(int value, int offset) {
        this.value = value;
        this.offset = offset;
    }
}
