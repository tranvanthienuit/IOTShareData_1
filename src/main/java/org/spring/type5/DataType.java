package org.spring.type5;

import lombok.Getter;

@Getter
public enum DataType {
    UI1("17"),
    UI2("18"),
    UI4("19"),
    UI8("21"),
    BSTR("8");
    private final String value;

    DataType(String value) {
        this.value = value;
    }
}
