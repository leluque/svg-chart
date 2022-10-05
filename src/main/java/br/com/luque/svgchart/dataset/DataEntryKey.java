package br.com.luque.svgchart.dataset;

import java.util.UUID;

public class DataEntryKey {

    private final UUID value;

    public DataEntryKey() {
        value = UUID.randomUUID();
    }

    public String getValue() {
        return value.toString();
    }

    @Override
    public String toString() {
        return getValue();
    }
}
