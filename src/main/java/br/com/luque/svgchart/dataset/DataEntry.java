package br.com.luque.svgchart.dataset;

import br.com.luque.svgchart.common.Color;

import java.util.Objects;

public class DataEntry {

    private final DataEntryKey key;
    private final String label;
    private final Double value;
    private final Color color;

    public DataEntry(String label, double value) {
        this(label, value, Color.random());
    }

    public DataEntry(String label, double value, Color color) {
        this.key = new DataEntryKey();

        Objects.requireNonNull(label, "The label cannot be null");
        if (label.isBlank()) {
            throw new IllegalArgumentException("The label cannot be empty");
        }
        this.label = label;

        if (value < 0) {
            throw new IllegalArgumentException("The value cannot be smaller than zero");
        }
        this.value = value;

        this.color = Objects.requireNonNull(color, "The color cannot be null");
    }

    public DataEntryKey getKey() {
        return key;
    }

    public String getLabel() {
        return label;
    }

    public Double getValue() {
        return value;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataEntry dataEntry)) return false;
        return key.equals(dataEntry.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }
}