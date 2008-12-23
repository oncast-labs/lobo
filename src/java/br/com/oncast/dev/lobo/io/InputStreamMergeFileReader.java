/* File: InputStreamMergeFileReader.java
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

import java.io.InputStream;
import java.util.NoSuchElementException;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import br.com.oncast.dev.lobo.MetricType;
import br.com.oncast.dev.lobo.merge.bean.MergeMetric;
import br.com.oncast.dev.lobo.merge.bean.MetricValue;

/**
 * {@link InputStream} implementation for {@link MergeFileReader}.
 * @author <a href="mailto:rodrigo.machado@oncast.com.br">Rodrigo Carvalho Machado</a>.
 */
class InputStreamMergeFileReader implements MergeFileReader {

   /** The current case name. */
   private String caze;

   /** The current scenario name. */
   private String scenario;

   /** The {@link XMLReader} for the {@link InputStream}. */
   private XMLReader xmlReader;

   /**
    * Creates a new instance of InputStreamMergeFileReader.
    * @param stream the stream to be read.
    */
   public InputStreamMergeFileReader(InputStream stream) {
      final XMLInputFactory factory = XMLInputFactory.newInstance();
      XMLStreamReader streamReader;
      try {
         streamReader = factory.createXMLStreamReader(stream, "UTF-8");
         xmlReader = new XMLReader(streamReader);
         xmlReader.consume("profile-report-merge");
      } catch (XMLStreamException e) {
         throw new LoboIOException(e);
      } catch (EODException e) {
         throw new LoboIOException(e);
      }
   }

   /**
    * @see br.com.oncast.dev.lobo.io.MergeFileReader#currentCaseName()
    */
   @Override
   public String currentCaseName() {
      return caze;
   }

   /**
    * @see br.com.oncast.dev.lobo.io.MergeFileReader#currentScenarioName()
    */
   @Override
   public String currentScenarioName() {
      return scenario;
   }

   /**
    * @see br.com.oncast.dev.lobo.io.MergeFileReader#hasCases()
    */
   @Override
   public boolean hasCases() {
      try {
         return xmlReader.isTag("profile-case");
      } catch (XMLStreamException e) {
         throw new LoboIOException(e);
      } catch (EODException e) {
         throw new LoboIOException(e);
      }
   }

   /**
    * @see br.com.oncast.dev.lobo.io.MergeFileReader#hasScenarios()
    */
   @Override
   public boolean hasScenarios() {
      try {
         return xmlReader.isTag("profile-scenario");
      } catch (XMLStreamException e) {
         throw new LoboIOException(e);
      } catch (EODException e) {
         throw new LoboIOException(e);
      }
   }

   /**
    * @see br.com.oncast.dev.lobo.io.MergeFileReader#hasMetrics()
    */
   @Override
   public boolean hasMetrics() {
      try {
         return xmlReader.isTag("profile-metric");
      } catch (XMLStreamException e) {
         throw new LoboIOException(e);
      } catch (EODException e) {
         throw new LoboIOException(e);
      }
   }

   /**
    * @see br.com.oncast.dev.lobo.io.MergeFileReader#hasMetricValues()
    */
   @Override
   public boolean hasMetricValues() {
      try {
         return xmlReader.isTag("metric-value");
      } catch (XMLStreamException e) {
         throw new LoboIOException(e);
      } catch (EODException e) {
         throw new LoboIOException(e);
      }
   }

   /**
    * @see br.com.oncast.dev.lobo.io.MergeFileReader#nextCase()
    */
   @Override
   public void nextCase() {
      if (!hasCases()) throw new NoSuchElementException();
      try {
         caze = xmlReader.getAttribute("case-class");
         xmlReader.consume("profile-case");
      } catch (XMLStreamException e) {
         throw new LoboIOException(e);
      } catch (EODException e) {
         throw new LoboIOException(e);
      }
   }

   /**
    * @see br.com.oncast.dev.lobo.io.MergeFileReader#nextScenario()
    */
   @Override
   public void nextScenario() {
      if (!hasScenarios()) throw new NoSuchElementException();
      try {
         scenario = xmlReader.getAttribute("scenario-method");
         xmlReader.consume("profile-scenario");
      } catch (XMLStreamException e) {
         throw new LoboIOException(e);
      } catch (EODException e) {
         throw new LoboIOException(e);
      }
   }

   /**
    * @see br.com.oncast.dev.lobo.io.MergeFileReader#nextMetric()
    */
   @Override
   public MergeMetric nextMetric() {
      if (!hasMetrics()) throw new NoSuchElementException();
      MergeMetric mergeMetric;
      try {
         mergeMetric = new MergeMetric();
         mergeMetric.setCase(caze);
         mergeMetric.setScenario(scenario);
         mergeMetric.setMetric(xmlReader.getAttribute("key"));
         xmlReader.consume();
      } catch (XMLStreamException e) {
         throw new LoboIOException(e);
      }
      return mergeMetric;
   }

   /**
    * @see br.com.oncast.dev.lobo.io.MergeFileReader#nextMetricValue()
    */
   @Override
   public MetricValue nextMetricValue() {
      if (!hasMetricValues()) throw new NoSuchElementException();
      MetricValue metricValue;
      try {
         metricValue = new MetricValue();
         metricValue.setBuildName(xmlReader.getAttribute("build-name"));
         metricValue.setType(MetricType.valueOf(xmlReader.getAttribute("type")));
         metricValue.setValue(Double.valueOf(xmlReader.getAttribute("value")));
         xmlReader.consume();
      } catch (XMLStreamException e) {
         throw new LoboIOException(e);
      }
      return metricValue;
   }

}
