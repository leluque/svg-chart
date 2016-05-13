package br.com.luque.svgchart;

import com.kitfox.svg.SVGCache;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This class implements sector charts (like pie charts) that are shown through
 * SVG images.
 *
 * Development History
 *
 * 30/apr/2016 : First version developed by Leandro Luque
 */
public class SVGChart {

    /**
     * The chart name.
     */
    private String name;
    /**
     * The chart dataset.
     */
    private Dataset dataset;
    /**
     * Color information about the dataset.
     */
    private final Map<String, Color> colors = new HashMap();

    /**
     * An enumeration of available representation directions.
     */
    public enum Direction {
        HORIZONTAL, VERTICAL
    };
    /**
     * The representation direction.
     */
    private Direction direction = Direction.VERTICAL;

    /**
     * Creates a new SVG sector chart.
     */
    public SVGChart() {
        this(null, null);
    }

    /**
     * Creates a new SVG sector chart with the specified dataset.
     *
     * @param dataset The dataset.
     */
    public SVGChart(Dataset dataset) {
        this(dataset, null);
    }

    /**
     * Creates a new SVG sector chart with the specified direction.
     *
     * @param direction The direction.
     */
    public SVGChart(Direction direction) {
        this(null, direction);
    }

    /**
     * Creates a new SVG sector chart with the specified dataset and direction.
     *
     * @param dataset The database.
     * @param direction The direction.
     */
    public SVGChart(Dataset dataset, Direction direction) {
        this.dataset = dataset;
        this.direction = direction;
    }

    /**
     * Gets the chart name.
     *
     * @return The chart name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the chart name.
     *
     * @param name The chart name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the dataset.
     *
     * @return The dataset.
     */
    public Dataset getDataset() {
        return dataset;
    }

    /**
     * Sets the dataset.
     *
     * @param dataset The dataset.
     */
    public void setDataset(Dataset dataset) {
        this.dataset = dataset;
    }

    /**
     * Gets the color for the section with the specified key.
     *
     * @param key The section key.
     * @return The color for the section with the specified key.
     */
    public Color getColorFor(String key) {
        return this.colors.get(key);
    }

    /**
     * Sets the color for the section with the specified key.
     *
     * @param key The section key.
     * @param color The color for the section with the specified key.
     */
    public void setColor(String key, Color color) {
        this.colors.put(key, color);
    }

    /**
     * Gets the direction.
     *
     * @return The direction.
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Sets the direction.
     *
     * @param direction The direction.
     */
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    /**
     * Generates the graph as an image for the specified reader.
     *
     * @param reader The reader.
     * @return The URI.
     * @throws IOException If any IO-related exception occurs.
     */
    public BufferedImage generateFor(Reader reader) throws IOException {
        return generateFor(SVGCache.getSVGUniverse().loadSVG(reader, this.getName()));
    }

    /**
     * Generates the graph as an image for the specified URL.
     *
     * @param url The URL.
     * @return The URI.
     * @throws IOException If any IO-related exception occurs.
     */
    public BufferedImage generateFor(URL url) throws IOException {
        return generateFor(SVGCache.getSVGUniverse().loadSVG(url));
    }

    /**
     * Generates the graph as an image for the specified inputstream.
     *
     * @param in The inputstream.
     * @return The graph as an image.
     */
    public BufferedImage generateFor(InputStream in) throws IOException {
        return generateFor(SVGCache.getSVGUniverse().loadSVG(in, this.getName()));
    }

    /**
     * Generates the graph as an image for the specified URI.
     *
     * @param uri The URI.
     * @return The graph as an image.
     */
    private BufferedImage generateFor(URI uri) {
        SVGLoader svgLoader = new SVGLoader();
        svgLoader.setSvgURI(uri);
        svgLoader.setAntiAlias(true);

        // Create a new image and draw the original SVG on it.
        BufferedImage image = new BufferedImage(svgLoader.getIconWidth(), svgLoader.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        svgLoader.paintSVG(graphics, 0, 0);
        graphics.dispose();

        // Count the number of non-backgroung pixels.
        int nonBackgroundPixels = 0;
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                if (image.getRGB(x, y) >> 24 != 0x00) {
                    nonBackgroundPixels++;
                }
            }
        }

        // Calculate the number of pixels for each data entry.
        double[] pixelsCount = new double[this.dataset.countEntries()];
        EntryPixels[] entriesPixels = new EntryPixels[this.dataset.countEntries()];
        int i = 0;
        double total = 0;
        Iterator<String> entriesKeys = this.dataset.getKeys();
        while (entriesKeys.hasNext()) {
            String key = entriesKeys.next();
            double value = this.dataset.getValue(key);
            total += value;
            pixelsCount[i] = value;
            entriesPixels[i] = new EntryPixels(key);
            i++;
        }
        for (int j = 0; j < pixelsCount.length; j++) {
            pixelsCount[j] = pixelsCount[j] * nonBackgroundPixels / total;
            entriesPixels[j].setNumberOfPixels((int) pixelsCount[j]);
        }

        // Order the entries.
        Arrays.sort(entriesPixels);

        // Paint each entry with the specified color.
        i = -1;
        int entryCounter = 0;
        Color entryColor = null;
        boolean finalizou = false;

        if (this.direction == Direction.HORIZONTAL) {
            for (int x = 0; x < image.getWidth(); x++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    int pixel = image.getRGB(x, y);
                    if ((pixel >> 24) != 0x00) {
                        if (entryCounter == 0) {
                            if (i == entriesPixels.length - 1) {
                                finalizou = true;
                                break;
                            } else {
                                i++;
                                entryCounter = entriesPixels[i].numberOfPixels;
                                entryColor = this.colors.get(entriesPixels[i].getKey());
                            }
                        }
                        entryCounter--;
                        int a = pixel >> 24;
                        int r = entryColor.getRed();
                        int g = entryColor.getGreen();
                        int b = entryColor.getBlue();
                        int col = (a << 24) | (r << 16) | (g << 8) | b;
                        image.setRGB(x, y, col);
                    }
                }
                if (finalizou) {
                    break;
                }
            }
        } else {
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    int pixel = image.getRGB(x, y);
                    if ((pixel >> 24) != 0x00) {
                        if (entryCounter == 0) {
                            if (i == entriesPixels.length - 1) {
                                finalizou = true;
                                break;
                            } else {
                                i++;
                                entryCounter = entriesPixels[i].numberOfPixels;
                                entryColor = this.colors.get(entriesPixels[i].getKey());
                            }
                        }
                        entryCounter--;
                        int a = pixel >> 24;
                        int r = entryColor.getRed();
                        int g = entryColor.getGreen();
                        int b = entryColor.getBlue();
                        int col = (a << 24) | (r << 16) | (g << 8) | b;
                        image.setRGB(x, y, col);
                    }
                }
                if (finalizou) {
                    break;
                }
            }
        }

        return image;
    }

    private class EntryPixels implements Comparable<EntryPixels> {

        private String key;
        private int numberOfPixels;

        public EntryPixels(String key) {
            this.key = key;
        }

        public EntryPixels(String key, int numberOfPixels) {
            this.key = key;
            this.numberOfPixels = numberOfPixels;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public int getNumberOfPixels() {
            return numberOfPixels;
        }

        public void setNumberOfPixels(int numberOfPixels) {
            this.numberOfPixels = numberOfPixels;
        }

        @Override
        public int compareTo(EntryPixels o) {
            if (numberOfPixels > o.numberOfPixels) {
                return -1;
            } else {
                return 1;
            }
        }

    }

} // End of class.
