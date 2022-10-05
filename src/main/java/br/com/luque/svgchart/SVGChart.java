package br.com.luque.svgchart;

import br.com.luque.svgchart.common.Color;
import br.com.luque.svgchart.dataset.DataEntryKey;
import br.com.luque.svgchart.dataset.DataSet;
import br.com.luque.svgchart.dataset.exception.EmptyDataSetException;
import br.com.luque.svgchart.dataset.exception.EntryNotFoundException;
import br.com.luque.svgchart.painter.SVGPainter;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.*;

@SuppressWarnings("unused")
public class SVGChart {

    public enum Directions {
        HORIZONTAL, VERTICAL
    }

    public enum SortOrders {
        NATURAL, REVERSE, NONE;
    }

    private final SVGPainter svgPainter;
    private final DataSet dataset;
    private final Directions direction;
    private final SortOrders sortOrder;


    public SVGChart(SVGPainter svgPainter, DataSet dataSet) {
        this(svgPainter, dataSet, null, null);
    }

    public SVGChart(SVGPainter svgPainter, DataSet dataSet, Directions direction) {
        this(svgPainter, dataSet, direction, null);
    }

    public SVGChart(SVGPainter svgPainter, DataSet dataSet, Directions direction, SortOrders sortOrder) {
        this.svgPainter = Objects.requireNonNull(svgPainter, "The SVGPainter cannot be null");
        this.dataset = Objects.requireNonNull(dataSet, "The dataset cannot be null");
        this.direction = Objects.requireNonNullElse(direction, Directions.VERTICAL);
        this.sortOrder = Objects.requireNonNullElse(sortOrder, SortOrders.NATURAL);
    }

    public DataSet getDataset() {
        return dataset;
    }

    public Directions getDirection() {
        return direction;
    }

    public BufferedImage generateImage(int width, int height) throws EmptyDataSetException {
        if (this.dataset.isEmpty()) {
            throw new EmptyDataSetException();
        }

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        svgPainter.paintOnGraphics(graphics, 0, 0, width, height);
        graphics.dispose();

        int numNonBackgroundPixels = countNonBackgroundPixels(image);

        List<DataEntryNumPixels> dataEntryNumPixels = calculateNumPixelsPerDataEntry(numNonBackgroundPixels);

        if (SortOrders.NATURAL == this.sortOrder) {
            dataEntryNumPixels.sort(Comparator.comparingInt(DataEntryNumPixels::numPixels));
        } else if (SortOrders.REVERSE == this.sortOrder) {
            dataEntryNumPixels.sort(Comparator.comparingInt(DataEntryNumPixels::numPixels).reversed());
        }

        return drawChart(image, dataEntryNumPixels);
    }

    private BufferedImage drawChart(BufferedImage image, List<DataEntryNumPixels> dataEntryNumPixels) {
        class Drawer {

            private int currentDataEntryIndex;
            private int numDataEntryRemainingPixelsToPaint;
            private Color dataEntryColor;

            public Drawer() {
                this.currentDataEntryIndex = -1;
            }

            public void goToNextDataEntry() {
                this.currentDataEntryIndex++;
                this.numDataEntryRemainingPixelsToPaint = dataEntryNumPixels.get(this.currentDataEntryIndex).numPixels();
                try {
                    dataEntryColor = getDataset().getColor(dataEntryNumPixels.get(this.currentDataEntryIndex).key());
                } catch (EntryNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }

            public void registerPixelDrawing() {
                this.numDataEntryRemainingPixelsToPaint--;
            }

            public void drawPixel(BufferedImage image, int x, int y) {
                // Get next data entry details when finished previous one
                if (0 == numDataEntryRemainingPixelsToPaint) {
                    goToNextDataEntry();
                }

                if (!isBackgroundPixel(image, x, y)) {
                    registerPixelDrawing();
                    image.setRGB(x, y, dataEntryColor.getIntValue());
                }
            }
        }

        Drawer drawer = new Drawer();

        int howManyDrawn = 0;
        if (this.direction == Directions.HORIZONTAL) {
            for (int x = 0; x < image.getWidth(); x++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    if (!isBackgroundPixel(image, x, y)) {
                        drawer.drawPixel(image, x, y);
                    }
                }
            }
        } else {
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    if (!isBackgroundPixel(image, x, y)) {
                        drawer.drawPixel(image, x, y);
                    }
                }
            }
        }

        return image;
    }

    private List<DataEntryNumPixels> calculateNumPixelsPerDataEntry(int numNonBackgroundPixels) {
        List<DataEntryNumPixels> dataEntryNumPixels = new ArrayList<>();
        this.getDataset()
                .getEntries()
                .forEach(
                        entry -> {
                            try {
                                int numPixels = (int) Math.ceil(this.getDataset().getRelativeValue(entry.getKey()) * numNonBackgroundPixels);
                                dataEntryNumPixels.add(new DataEntryNumPixels(entry.getKey(), numPixels));
                            } catch (EntryNotFoundException e) {
                                throw new RuntimeException(e);
                            }

                        }
                );
        return dataEntryNumPixels;
    }

    private boolean isBackgroundPixel(BufferedImage image, int x, int y) {
        return image.getRGB(x, y) >> 24 == 0x00;
    }

    private int countNonBackgroundPixels(BufferedImage image) {
        int nonBackgroundPixels = 0;
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                if (!isBackgroundPixel(image, x, y)) {
                    nonBackgroundPixels++;
                }
            }
        }
        return nonBackgroundPixels;
    }

    private class DataEntryNumPixels {
        private DataEntryKey key;
        private int numPixels;

        public DataEntryNumPixels(DataEntryKey key, int numPixels) {
            this.key = key;
            this.numPixels = numPixels;
        }

        public DataEntryKey key() {
            return key;
        }

        public int numPixels() {
            return numPixels;
        }
    }

}
