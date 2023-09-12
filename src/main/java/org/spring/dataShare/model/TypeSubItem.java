package org.spring.dataShare.model;

import lombok.Getter;

@Getter
public enum TypeSubItem {
    UI1(17),
    UI2(18),
    UI4(19),
    UI8(21),
    BSTR(8)
    ;
    private final int value;

    TypeSubItem(int value) {
        this.value = value;
    }
}
