# SVG-Chart
A Java-based library that draws sector charts (like Pie Charts) inside SVG images.

## Motivation
Sometimes, it is interesting to generate sector charts inside images. This approach can convey a different result than traditional pie charts and aggregate value to a report. As an example, a report with the profile of a person could be presented with a chart with a person filled with sections that represents the intensity of different characteristics that form the person's profile (Figure 1).

![Image of a man with sectors painted in different colors](/man.png "Figure 1. Example of sector chart inside a man")

## How to use it
Sometimes, it is interesting to generate sector charts inside images. This approach can convey a different result than traditional pie charts and aggregate value to a report. As an example, a report with the profile of a person could be presented with a chart with a person filled with sections that represents the intensity of different characteristics that form the person's profile (Figure 1).

        // Generate a chart.
        SVGChart chart = new SVGChart();
        chart.setName("Profile");
        br.com.luque.svgchart.Dataset dataset = new br.com.luque.svgchart.Dataset();
        dataset.setValue("A", "Dominant", 5.10);
        dataset.setValue("C", "Extrovert", 5.05);
        dataset.setValue("I", "Planner", 2.15);
        dataset.setValue("O", "Analytic", 1.97);
        chart.setDataset(dataset);
        chart.setColor("A", new Color(181, 230, 29));
        chart.setColor("C", new Color(153, 217, 234));
        chart.setColor("I", new Color(239, 228, 176));
        chart.setColor("O", new Color(112, 146, 190));
        chart.setDirection(SVGChart.Direction.VERTICAL);
        final BufferedImage image = chart.generateFor(svgIn);

After generating an image, the chart can be easily added to a PDF through iText.

         com.itextpdf.text.Image pdfImage = com.itextpdf.text.Image.getInstance(image, null);
        pdfImage.setAbsolutePosition((float) ((pageSize.getWidth() - image.getWidth()) / 2), (float) ((pageSize.getHeight() - image.getHeight()) / 2) - 50f);
        pdfImage.setDpi(300, 300);
        document.add(pdfImage);

We are currently working on legends and other chart elements.
