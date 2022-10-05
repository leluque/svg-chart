package br.com.luque.svgchart.dataset;

import br.com.luque.svgchart.common.Color;
import br.com.luque.svgchart.dataset.exception.EntryNotFoundException;

import java.util.*;

public class DataSet {

    private final Map<DataEntryKey, DataEntry> entries = new HashMap<>();

    public DataEntry createEntry(String label, Double value) {
        return createEntry(label, value, Color.random());
    }

    public DataEntry createEntry(String label, Double value, Color color) {
        DataEntry entry = new DataEntry(label, value, color);
        entries.put(entry.getKey(), entry);
        return entry;
    }

    public String getLabel(DataEntryKey key) throws EntryNotFoundException {
        validateEntryExistence(key);
        return this.entries.get(key).getLabel();
    }

    public Double getValue(DataEntryKey key) throws EntryNotFoundException {
        validateEntryExistence(key);
        return this.entries.get(key).getValue();
    }

    public double getRelativeValue(DataEntryKey key) throws EntryNotFoundException {
        double sumOfValues = getSumOfValues();
        if (0 == sumOfValues) {
            return 0;
        }
        return getValue(key) / sumOfValues;
    }

    public Color getColor(DataEntryKey key) throws EntryNotFoundException {
        validateEntryExistence(key);
        return this.entries.get(key).getColor();
    }

    private void validateEntryExistence(DataEntryKey key) throws EntryNotFoundException {
        if (!entries.containsKey(key)) {
            throw new EntryNotFoundException("No entry with key " + key + " has been found");
        }
    }

    private double getSumOfValues() {
        return entries
                .values()
                .stream()
                .map(DataEntry::getValue)
                .reduce((a, b) -> a + b)
                .orElse(0d);
    }

    public int countEntries() {
        return this.entries.size();
    }

    public boolean isEmpty() {
        return 0 == countEntries();
    }

    public Collection<DataEntry> getEntries() {
        return new HashSet<>(entries.values());
    }

}
