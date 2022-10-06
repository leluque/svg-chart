package br.com.luque.svgchart.painter.svgsalamander;

import br.com.luque.svgchart.painter.exception.InvalidSVGDimension;
import br.com.luque.svgchart.painter.exception.SVGCouldNotBeLoadedException;
import br.com.luque.svgchart.painter.SVGPainter;
import com.kitfox.svg.SVGCache;
import com.kitfox.svg.SVGDiagram;
import com.kitfox.svg.SVGException;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URI;
import java.net.URL;
import java.util.Objects;
import java.util.UUID;

public class SVGSalamanderPainter implements SVGPainter {

    public enum ResizingInterpolations {
        NEAREST_NEIGHBOR(RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR),
        BILINEAR(RenderingHints.VALUE_INTERPOLATION_BILINEAR),
        BICUBIC(RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        private Object interpolation;

        ResizingInterpolations(Object interpolation) {
            this.interpolation = interpolation;
        }

        public Object getInterpolation() {
            return interpolation;
        }
    }

    private ResizingInterpolations resizingInterpolation;
    private boolean antiAliasing;

    private URI uri;
    private SVGDiagram svgDiagram;

    public SVGSalamanderPainter(URI uri) throws SVGCouldNotBeLoadedException {
        this(uri, true);
    }

    public SVGSalamanderPainter(URI uri, boolean antialiasing) throws SVGCouldNotBeLoadedException {
        this(uri, antialiasing, null);
    }

    public SVGSalamanderPainter(URI uri, boolean antialiasing, ResizingInterpolations resizingInterpolation) throws SVGCouldNotBeLoadedException {
        this.antiAliasing = antialiasing;
        this.resizingInterpolation = Objects.requireNonNullElse(resizingInterpolation, ResizingInterpolations.BICUBIC);

        this.uri = uri;
        this.svgDiagram = SVGCache.getSVGUniverse().getDiagram(uri);
        if (null == this.svgDiagram) {
            throw new SVGCouldNotBeLoadedException();
        }
    }

    @Override
    public URI getURIFor(Reader reader) throws IOException {
        return SVGCache.getSVGUniverse().loadSVG(reader, UUID.randomUUID().toString());
    }

    @Override
    public URI getURIFor(URL url) throws IOException {
        return SVGCache.getSVGUniverse().loadSVG(url);
    }

    @Override
    public URI getURIFor(InputStream in) throws IOException {
        return SVGCache.getSVGUniverse().loadSVG(in, UUID.randomUUID().toString());
    }

    @Override
    public int getOriginalWidth() {
        return (int) this.svgDiagram.getWidth();
    }

    @Override
    public int getOriginalHeight() {
        return (int) this.svgDiagram.getWidth();
    }

    @Override
    public void paintOnGraphics(Graphics2D graphics, int x, int y) throws InvalidSVGDimension {
        if (0 == getOriginalWidth() || 0 == getOriginalHeight()) {
            throw new InvalidSVGDimension();
        }

        paintOnGraphics(graphics, x, y, getOriginalWidth(), getOriginalHeight());
    }

    @Override
    public void paintOnGraphics(Graphics2D graphics, int x, int y, int width, int height) {
        Object previousAntialiasing = graphics.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
        Object previousInterpolation = graphics.getRenderingHint(RenderingHints.KEY_INTERPOLATION);
        AffineTransform previousTransform = graphics.getTransform();

        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, antiAliasing ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF);
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, resizingInterpolation.getInterpolation());

        final Rectangle2D.Double svgDiagramViewRectangle = new Rectangle2D.Double();
        svgDiagram.getViewRect(svgDiagramViewRectangle);
        graphics.scale(width / svgDiagramViewRectangle.width, height / svgDiagramViewRectangle.height);
        graphics.translate(x, y);
        svgDiagram.setIgnoringClipHeuristic(true);

        try {
            this.svgDiagram.render(graphics);
        } catch (SVGException e) {
            throw new RuntimeException(e);
        }

        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, previousAntialiasing);
        if (null != previousInterpolation) {
            graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, previousInterpolation);
        }
        graphics.setTransform(previousTransform);
    }

    @Override
    public URI getURI() {
        return uri;
    }

    public boolean isAntiAliasing() {
        return antiAliasing;
    }

    public ResizingInterpolations getResizingInterpolation() {
        return resizingInterpolation;
    }

}
