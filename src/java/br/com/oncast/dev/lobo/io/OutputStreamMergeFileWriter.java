/* File: OutputStreamMergeFileWriter.java
 * Date: 17/04/2007
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
package br.com.oncast.dev.lobo.io;

import java.io.OutputStream;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import br.com.oncast.dev.lobo.merge.bean.MergeMetric;
import br.com.oncast.dev.lobo.merge.bean.MetricValue;

/**
 * A {@link OutputStream} implementation of {@link MergeFileWriter}.
 */
public class OutputStreamMergeFileWriter implements MergeFileWriter {

   /** The XML writer. */
   private final XMLStreamWriter xmlStreamWriter;

   /** The simplified XML writer. */
   private final XMLWriter writer;

   /** Flag that indicates if a metric is open. */
   private boolean isMetricOpen;

   /** Flag that indicates if a scenario is open. */
   private boolean isScenarioOpen;

   /** Flag that indicates if a case is open. */
   private boolean isCaseOpen;

   /**
    * Creates a new instance of PocOutputStreamMergeFileWriter.
    * @param stream the stream to be written to.
    */
   public OutputStreamMergeFileWriter(final OutputStream stream) {
      try {
         xmlStreamWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(stream, "UTF-8");
         writer = new XMLWriter(xmlStreamWriter);
         initialize();
      } catch (Exception e) {
         throw new LoboIOException("Couldn't instantiate the XML writer.", e);
      }
   }

   /**
    * @see br.com.oncast.dev.lobo.io.MergeFileWriter#close()
    */
   @Override
   public void close() {
      try {
         closeMerge();
         writer.close();
         xmlStreamWriter.close();
      } catch (XMLStreamException e) {
         throw new LoboIOException("Couldn't close the generated xml file.", e);
      }
   }

   /**
    * @see br.com.oncast.dev.lobo.io.MergeFileWriter#startCase(java.lang.String)
    */
   @Override
   public void startCase(final String currentCaseName) {
      try {
         if (isCaseOpen) closeCase();
         writer.writeTag("profile-case").writeAttribute("case-class", currentCaseName);
         isCaseOpen = true;
      } catch (XMLStreamException e) {
         throw new LoboIOException("Couldn't write profile-case tag to merged xml.", e);
      }
   }

   /**
    * @see br.com.oncast.dev.lobo.io.MergeFileWriter#startScenario(java.lang.String)
    */
   @Override
   public void startScenario(final String currentScenarioName) {
      try {
         if (isScenarioOpen) closeScenario();
         writer.writeTag("profile-scenario").writeAttribute("scenario-method", currentScenarioName);
         isScenarioOpen = true;
      } catch (XMLStreamException e) {
         throw new LoboIOException("Couldn't write profile-scenario tag to merged xml.", e);
      }
   }

   /**
    * @see br.com.oncast.dev.lobo.io.MergeFileWriter#startMetric(br.com.oncast.dev.lobo.merge.bean.MergeMetric)
    */
   @Override
   public void startMetric(final MergeMetric metric) {
      try {
         if (isMetricOpen) closeMetric();
         writer.writeTag("profile-metric").writeAttribute("key", metric.getMetric());
         isMetricOpen = true;
      } catch (XMLStreamException e) {
         throw new LoboIOException("Couldn't write profile-metric tag to merged xml.", e);
      }
   }

   /**
    * @see br.com.oncast.dev.lobo.io.MergeFileWriter#writeMetricValue(br.com.oncast.dev.lobo.merge.bean.MetricValue)
    */
   @Override
   public void writeMetricValue(final MetricValue value) {
      writer.writeEmptyTag("metric-value");
      writer.writeAttribute("build-name", value.getBuildName());
      writer.writeAttribute("type", value.getType().name());
      writer.writeAttribute("value", Double.toString(value.getValue()));
   }

   /**
    * Initiates the xml file.
    * @throws XMLStreamException in case xml write operation fails.
    */
   private void initialize() throws XMLStreamException {
      writer.writeTag("profile-report-merge");
   }

   /**
    * Closes the merge file.
    * @throws XMLStreamException 
    */
   private void closeMerge() throws XMLStreamException {
      if (isCaseOpen) closeCase();
      writer.writeEndTag();
   }

   /**
    * Close an open case.
    * @throws XMLStreamException in case xml write operation fails.
    */
   private void closeCase() throws XMLStreamException {
      if (isScenarioOpen) closeScenario();
      writer.writeEndTag();
      isCaseOpen = false;
   }

   /**
    * Close an open scenario.
    * @throws XMLStreamException in case xml write operation fails.
    */
   private void closeScenario() throws XMLStreamException {
      if (isMetricOpen) closeMetric();
      writer.writeEndTag();
      isScenarioOpen = false;
   }

   /**
    * Close an open metric.
    * @throws XMLStreamException in case xml write operation fails.
    */
   private void closeMetric() throws XMLStreamException {
      writer.writeEndTag();
      isMetricOpen = false;
   }
}
