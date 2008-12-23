/* File: JFreeChartReportPlotter.java
 * Date: 24/04/2007
 * 
 * Copyright (C) 2007 OnCast Technologies
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package br.com.oncast.dev.lobo.report;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import br.com.oncast.dev.lobo.io.IOFactory;
import br.com.oncast.dev.lobo.io.IOFactoryImpl;
import br.com.oncast.dev.lobo.io.MergeFileReader;
import br.com.oncast.dev.lobo.merge.bean.MergeMetric;
import br.com.oncast.dev.lobo.merge.bean.MetricValue;

/**
 * JFreeChart implementation for {@link ReportPlotter}.
 */
class JFreeChartReportPlotter implements ReportPlotter {

   /**
    * Internal class that grup a {@link MergeMetric} and a {@link List} of {@link MetricValue}.
    */
   private static class MetricAndValues {
      /** The {@link MergeMetric}. */
      public MergeMetric mergeMetric = null;
      /** The {@link List} of {@link MetricValue}. */
      public List<MetricValue> metricValues = new ArrayList<MetricValue>();
   }

   /** The graphic width. */
   private static final int GRAPHIC_WIDTH = 400;
   /** The graphic height. */
   private static final int GRAPHIC_HEIGHT = 300;
   /** The graphic formar. */
   private static final String GRAPHIC_FORMAT = "jpeg";
   /** The title of value axis. */
   private static final String TITLE_VALUE = "Execution time";
   /** The tigle of build axis. */
   private static final String TITLE_BUILD = "Build version";

   /**
    * @see br.com.oncast.dev.lobo.report.ReportPlotter#plot(java.io.File, java.io.File)
    */
   @Override
   public void plot(final File input, final File outputDir) {
      final List<MetricAndValues> values;
      try {
         values = retrieveMetricAndValues(input);
         for (final MetricAndValues metricAndValues : values)
            plotGraphic(metricAndValues, outputDir);
      } catch (FileNotFoundException e) {
         throw new ReportException(e);
      } catch (IOException e) {
         throw new ReportException(e);
      }
   }

   /**
    * Plot a graphic for the {@link MetricAndValues}.
    * @param metricAndValues The source for the graphic.
    * @param baseDir The base dir for plot the graphics.
    * @throws IOException in case of exception on writing the image.
    */
   private void plotGraphic(final MetricAndValues metricAndValues, final File baseDir) throws IOException {
      final MergeMetric mergeMetric = metricAndValues.mergeMetric;
      final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
      for (final MetricValue metricValue : metricAndValues.metricValues)
         dataset.addValue(metricValue.getValue(), mergeMetric.getMetric(), metricValue.getBuildName());
      final JFreeChart chart = ChartFactory.createLineChart(mergeMetric.getMetric(), TITLE_BUILD, TITLE_VALUE, dataset,
            PlotOrientation.VERTICAL, true, false, false);
      final BufferedImage bufferedImage = chart.createBufferedImage(GRAPHIC_WIDTH, GRAPHIC_HEIGHT);
      final String filePath = mergeMetric.getCase() + "/";
      final String fileName = mergeMetric.getScenario() + "-" + mergeMetric.getMetric() + "." + GRAPHIC_FORMAT;
      final String fullFileName = filePath + fileName;
      final File outputFileImage = new File(baseDir, fullFileName);
      outputFileImage.getParentFile().mkdirs();
      ImageIO.write(bufferedImage, GRAPHIC_FORMAT, outputFileImage);
   }

   /**
    * Retrieve from a merged report a {@link List} of {@link MetricAndValues}.
    * @param input The input XML merged report file.
    * @return A {@link List} of {@link MetricAndValues}.
    * @throws FileNotFoundException in case of not found input {@link File}.
    */
   private List<MetricAndValues> retrieveMetricAndValues(final File input) throws FileNotFoundException {
      final IOFactory factory = new IOFactoryImpl();
      final MergeFileReader mergeReader = factory.createMergeReader(new FileInputStream(input));
      final List<MetricAndValues> result = new ArrayList<MetricAndValues>();
      while (mergeReader.hasCases()) {
         mergeReader.nextCase();
         while (mergeReader.hasScenarios()) {
            mergeReader.nextScenario();
            populateResult(mergeReader, result);
         }
      }
      return result;
   }

   /**
    * Populate the result {@link List} with {@link MetricAndValues}.
    * @param mergeReader To be iterated.
    * @param result The populated {@link List}.
    */
   private void populateResult(final MergeFileReader mergeReader, final List<MetricAndValues> result) {
      while (mergeReader.hasMetrics()) {
         final MetricAndValues metricAndValues = new MetricAndValues();
         metricAndValues.mergeMetric = mergeReader.nextMetric();
         while (mergeReader.hasMetricValues())
            metricAndValues.metricValues.add(mergeReader.nextMetricValue());
         result.add(metricAndValues);
      }
   }

}
