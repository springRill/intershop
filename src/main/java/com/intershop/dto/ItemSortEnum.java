package com.intershop.dto;

public enum ItemSortEnum {

    NO("id"),
    ALPHA("title"),
    PRICE("price");

    private final String sortColumnName;

    ItemSortEnum(String sortColumnName) {
        this.sortColumnName = sortColumnName;
    }

    public String getSortColumnName() {
        return sortColumnName;
    }
}
