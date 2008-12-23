/* File: XSLTReportTransformer.java
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * The XSLT implementation of {@link ReportTransformer}.
 */
class XSLTReportTransformer implements ReportTransformer {
   /**
    * @see br.com.oncast.dev.lobo.report.ReportTransformer#transfor(java.io.File, java.io.File)
    */
   @Override
   public void transform(final File input, final File outputDir) {
      try {
         final Source xmlSource = new StreamSource(input);
         final Source xsltSource = new StreamSource(getClass().getResourceAsStream("resources/profiler-transformer.xslt"));
         final TransformerFactory transformerFactory = TransformerFactory.newInstance();
         final Transformer transformer;
         final FileOutputStream reportOS = new FileOutputStream(new File(outputDir, "report.html"));
         final StreamResult result = new StreamResult(reportOS);
         transformer = transformerFactory.newTransformer(xsltSource);
         transformer.setParameter("output.dir", outputDir.getPath());
         transformer.transform(xmlSource, result);
         reportOS.close();
      } catch (TransformerConfigurationException e) {
         throw new ReportException(e);
      } catch (TransformerException e) {
         throw new RuntimeException(e);
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
   }
}
