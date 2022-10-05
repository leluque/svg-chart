package br.com.luque.svgchart.painter;

import br.com.luque.svgchart.painter.exception.InvalidSVGDimension;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URI;
import java.net.URL;
import java.nio.file.Paths;

public interface SVGPainter {
    int getOriginalHeight();

    int getOriginalWidth();

    void paintOnGraphics(Graphics2D graphics, int x, int y) throws InvalidSVGDimension;

    void paintOnGraphics(Graphics2D graphics, int x, int y, int width, int height);

    URI getURIFor(Reader reader) throws IOException;

    URI getURIFor(URL url) throws IOException;

    URI getURIFor(InputStream in) throws IOException;

    URI getURI();
}
