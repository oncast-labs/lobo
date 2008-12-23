/* File: InputStreamProfileReader.java
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

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import br.com.oncast.dev.lobo.MetricType;
import br.com.oncast.dev.lobo.eval.bean.ProfileCase;
import br.com.oncast.dev.lobo.eval.bean.ProfileMetric;
import br.com.oncast.dev.lobo.eval.bean.ProfileReport;
import br.com.oncast.dev.lobo.eval.bean.ProfileScenario;

/**
 * {@link ProfileReport} implementation to read {@link InputStream}s.
 */
class InputStreamProfileReader implements ProfileReader {
   /** The xml reader. */
   private final XMLReader xml;

   /**
    * Creates a new instance of InputStreamProfileReader.
    * @param stream the stream to be read.
    */
   public InputStreamProfileReader(InputStream stream) {
      final XMLInputFactory factory = XMLInputFactory.newInstance();
      XMLStreamReader xmlReader;
      try {
         xmlReader = factory.createXMLStreamReader(stream, "UTF-8");
      } catch (XMLStreamException e) {
         throw new LoboIOException(e);
      }
      xml = new XMLReader(xmlReader);
   }

   /**
    * @see br.com.oncast.dev.lobo.io.ProfileReader#readProfile()
    */
   @Override
   public ProfileReport readProfile() {
      final ProfileReport report = new ProfileReport();

      try {
         readReport(report);
      } catch (XMLStreamException e) {
         throw new LoboIOException(e);
      } catch (EODException e) {
         throw new LoboIOException(e);
      }

      return report;
   }

   /**
    * Reads a profile report from the xml stream.
    * @param report the report to be filled up.
    * @throws XMLStreamException
    * @throws EODException
    */
   private void readReport(ProfileReport report) throws XMLStreamException, EODException {
      xml.consume("profile-report");
      while (xml.isTag("profile-case"))
         readCase(report);
   }

   /**
    * Reads a case from the xml stream.
    * @param report the report to have a case appended.
    * @throws XMLStreamException
    * @throws EODException
    */
   private void readCase(ProfileReport report) throws XMLStreamException, EODException {
      final ProfileCase caze = new ProfileCase(xml.getAttribute("case-class"));
      xml.consume();

      while (xml.isTag("profile-scenario"))
         readScenario(caze);

      report.addCase(caze);
   }

   /**
    * Reads a scenario from the xml stream.
    * @param caze the case to have a scenario appened.
    * @throws XMLStreamException
    * @throws EODException
    */
   private void readScenario(ProfileCase caze) throws XMLStreamException, EODException {
      final ProfileScenario scenario = new ProfileScenario(xml.getAttribute("scenario-method"));
      xml.consume();

      while (xml.isTag("profile-metric"))
         readMetric(scenario);

      caze.addScenario(scenario);
   }

   /**
    * Reads a metric from the xml stream.
    * @param scenario the scenario to have a metric appened.
    * @throws XMLStreamException
    */
   private void readMetric(ProfileScenario scenario) throws XMLStreamException {
      final ProfileMetric metric = new ProfileMetric(xml.getAttribute("key"));
      metric.setType(MetricType.valueOf(xml.getAttribute("type")));
      metric.setValue(Double.valueOf(xml.getAttribute("value")));
      scenario.addMetric(metric);
      xml.consume();
   }
}
