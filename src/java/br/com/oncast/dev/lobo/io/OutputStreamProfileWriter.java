/* File: OutputStreamProfileWriter.java
 * Date: 04/04/2007
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

import java.io.IOException;
import java.io.OutputStream;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import br.com.oncast.dev.lobo.eval.bean.ProfileCase;
import br.com.oncast.dev.lobo.eval.bean.ProfileMetric;
import br.com.oncast.dev.lobo.eval.bean.ProfileReport;
import br.com.oncast.dev.lobo.eval.bean.ProfileScenario;

/**
 * {@link ProfileWriter} for XML file.
 */
public class OutputStreamProfileWriter implements ProfileWriter {
   /** The xml file output stream. */
   private final OutputStream stream;

   /** The StAX {@link XMLStreamWriter}. */
   private final XMLStreamWriter streamWriter;

   /** The simplified xml writer. */
   private final XMLWriter writer;

   /**
    * Creates a new instance of OutputStreamProfileWriter.
    * @param stream
    */
   public OutputStreamProfileWriter(OutputStream stream) {
      this.stream = stream;
      final XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
      try {
         streamWriter = outputFactory.createXMLStreamWriter(stream, "UTF-8");
      } catch (XMLStreamException e) {
         throw new LoboIOException(e);
      }
      writer = new XMLWriter(streamWriter);
   }

   /**
    * @see br.com.oncast.dev.lobo.io.ProfileWriter#write(java.io.File,br.com.oncast.dev.lobo.eval.bean.ProfileReport)
    */
   @Override
   public void write(final ProfileReport profileReport) throws LoboIOException {
      try {
         writeProfileReport(profileReport);
         writer.close();
         streamWriter.close();
         stream.close();
      } catch (XMLStreamException e) {
         throw new LoboIOException("Exception on XML document writing.", e);
      } catch (IOException e) {
         throw new LoboIOException("Exception closing the XML file output stream.", e);
      }
   }

   /**
    * Write a {@link ProfileReport} on XML Element.
    * @param profileReport to be written.
    * @throws XMLStreamException
    */
   private void writeProfileReport(final ProfileReport profileReport) throws XMLStreamException {
      writer.writeTag("profile-report");
      for (final ProfileCase profileCase : profileReport.getCases())
         writeProfileCase(profileCase);
      writer.writeEndTag();
   }

   /**
    * Write a {@link ProfileCase} on XML Element.
    * @param profileCase to be written.
    * @throws XMLStreamException
    */
   private void writeProfileCase(final ProfileCase profileCase) throws XMLStreamException {
      final String caseClass = profileCase.getCaseClass();
      writer.writeTag("profile-case").writeAttribute("case-class", caseClass);
      for (final ProfileScenario profileScenario : profileCase.getScenarios())
         writeProfileScenario(profileScenario);
      writer.writeEndTag();
   }

   /**
    * Write a {@link ProfileScenario} on XML Element.
    * @param profileScenario to be written.
    * @throws XMLStreamException
    */
   private void writeProfileScenario(final ProfileScenario profileScenario) throws XMLStreamException {
      final String scenarioMethod = profileScenario.getScenarioMethod();
      writer.writeTag("profile-scenario").writeAttribute("scenario-method", scenarioMethod);
      for (final ProfileMetric profileMetric : profileScenario.getMetrics())
         writeProfileMetric(profileMetric);
      writer.writeEndTag();
   }

   /**
    * Write a {@link ProfileMetric} on XML Element.
    * @param profileMetric to be written.
    * @throws XMLStreamException
    */
   private void writeProfileMetric(final ProfileMetric profileMetric) throws XMLStreamException {
      final String key = profileMetric.getKey();
      final String type = profileMetric.getType().name();
      final String value = Double.toString(profileMetric.getValue());
      writer.writeEmptyTag("profile-metric").writeAttribute("key", key).writeAttribute("type", type).writeAttribute(
            "value", value);
   }
}
