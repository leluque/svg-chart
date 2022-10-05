package br.com.luque.svgchart.example;

import br.com.luque.svgchart.SVGChart;
import br.com.luque.svgchart.common.Color;
import br.com.luque.svgchart.dataset.DataSet;
import br.com.luque.svgchart.dataset.exception.EmptyDataSetException;
import br.com.luque.svgchart.painter.SVGPainter;
import br.com.luque.svgchart.painter.exception.SVGCouldNotBeLoadedException;
import br.com.luque.svgchart.painter.svgsalamander.SVGSalamanderPainter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class VerticalSVG {

    public static void main(String[] args) throws SVGCouldNotBeLoadedException, EmptyDataSetException, IOException {

        final DataSet dataSet = new DataSet();
        dataSet.createEntry("Dominant", 5.10, new Color(236, 107, 86));
        dataSet.createEntry("Extrovert", 5.05, new Color(255, 193, 84));
        dataSet.createEntry("Planner", 2.15, new Color(71, 179, 156));
        dataSet.createEntry("Analytic", 1.97, Color.WHITE);

        final SVGPainter painter = new SVGSalamanderPainter(Paths.get("trophy.svg").toUri());
        final SVGChart chart = new SVGChart(painter, dataSet, SVGChart.Directions.VERTICAL, SVGChart.SortOrders.REVERSE);
        final BufferedImage image = chart.generateImage(200, 200);
        ImageIO.write(image, "PNG", new File("trophy.png"));

    }

}
