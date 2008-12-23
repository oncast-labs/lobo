/* File: XMLWriter.java
 * Date: 16/05/2007
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

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 * Writer for simple XML files.<br>
 * Does not support character nor CDATA output.
 */
public class XMLWriter {
   /** The string to be used to tell lines apart. */
   private static final String LINE_SEPARATOR = System.getProperty("line.separator");
   
   /** The string to be used as indent unit. */
   private static final String INDENT_STRING = "   ";
   
   /** The nested {@link XMLStreamWriter}. */
   private final XMLStreamWriter writer;

   /** The current nesting level. */
   private int nestingLevel = 0;
   
   /**
    * Creates a new instance of XMLWriter.
    * @param writer the nested {@link XMLStreamWriter}.
    */
   public XMLWriter(final XMLStreamWriter writer) {
      this.writer = writer;
      try {
         writer.writeStartDocument();
      } catch (XMLStreamException e) {
         throw new LoboIOException(e);
      }
   }
   
   /**
    * Writes a tag with the given name.
    * @param node the name of the tag.
    * @return the self {@link XMLWriter} for convenience.
    */
   public XMLWriter  writeTag(String node) {
      try {
         indent();
         writer.writeStartElement(node);
         nestingLevel++;
      } catch (XMLStreamException e) {
         throw new LoboIOException(e);
      }
      
      return this;
   }
   
   /**
    * Writes an empty tag with the given name.
    * @param node the name of the tag.
    * @return the self {@link XMLWriter} for convenience.
    */
   public XMLWriter writeEmptyTag(String node) {
      try {
         indent();
         writer.writeEmptyElement(node);
      } catch (XMLStreamException e) {
         throw new LoboIOException(e);
      }
      
      return this;
   }
   
   /**
    * Writes an attribute to the current tag.
    * @param name the name of the attribute.
    * @param value the attribute's value.
    * @return the self {@link XMLWriter} for convenience.
    */
   public XMLWriter writeAttribute(String name, String value) {
      try {
         writer.writeAttribute(name, value);
      } catch (XMLStreamException e) {
         throw new LoboIOException(e);
      }
      
      return this;
   }
   
   /**
    * Ends the current tag.
    * @return the self {@link XMLWriter} for convenience.
    */
   public XMLWriter writeEndTag() {
      try {
         nestingLevel--;
         indent();
         writer.writeEndElement();
      } catch (XMLStreamException e) {
         throw new LoboIOException(e);
      }
      
      return this;
   }
   
   /**
    * Closes the document.<br>
    * Note that this method won't close the {@link XMLStreamWriter}.
    */
   public void close() {
      try {
         writer.writeEndDocument();
      } catch (XMLStreamException e) {
         throw new LoboIOException(e);
      }
   }

   /**
    * Creates a new line and indents the xml.
    * @throws XMLStreamException 
    */
   private void indent() throws XMLStreamException {
      writer.writeCharacters(LINE_SEPARATOR);
      for (int i=0;i<nestingLevel;i++) {
         writer.writeCharacters(INDENT_STRING);
      }
   }
}
