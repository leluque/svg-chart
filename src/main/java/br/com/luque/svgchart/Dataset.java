package br.com.luque.svgchart;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This class implements charts datasets.
 */
public class Dataset {

    private final Map<String, DataEntry> dataEntries = new HashMap();

    public void setValue(String label, Double value) {
        setValue(label, label, value);
    }

    public void setValue(String key, String label, Double value) {
        this.dataEntries.put(key, new DataEntry(key, label, value));
    }

    public Double getValue(String key) {
        DataEntry dataEntry = this.dataEntries.get(key);
        return dataEntry == null ? null : dataEntry.getValue();
    }

    public int countEntries() {
        return this.dataEntries.size();
    }

    public Iterator<String> getKeys() {
        return this.dataEntries.keySet().iterator();
    }

    /**
     * This class implements a data entry.
     *
     * Development History
     *
     * 30/apr/2016 : First version developed by Leandro Luque
     */
    private class DataEntry {

        /**
         * The entry unique identifier in a dataset.
         */
        private String key;
        /**
         * The entry label.
         */
        private String label;
        /**
         * The entry value.
         */
        private Double value;

        public DataEntry() {
        }

        public DataEntry(String key, String label, Double value) {
            this.key = key;
            this.label = label;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public Double getValue() {
            return value;
        }

        public void setValue(Double value) {
            this.value = value;
        }

    } // End of class.

} // End of class.
