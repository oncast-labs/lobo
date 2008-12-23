/* File: XMLReader.java
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

import static javax.xml.stream.XMLStreamConstants.END_DOCUMENT;
import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * XML Reader.<br>
 * Wrapper for {@link XMLStreamReader} that defines methods to read and consume tags.
 */
class XMLReader {
   /** XML Stream Reader. */
   private final XMLStreamReader xml;

   /** The current tag. */
   private String currentTag;

   /**
    * Creates a new instance of XMLReader.
    * @param xml the xml reader.
    */
   XMLReader(XMLStreamReader xml) {
      this.xml = xml;
   }

   /**
    * Advances until the next element starts (it is assumed that the xml is valid).
    * @throws XMLStreamException
    * @throws EODException
    */
   public boolean isTag(String tag) throws XMLStreamException, EODException {
      if (currentTag != null) return currentTag.equals(tag);

      while (xml.hasNext()) {
         final int next = xml.next();
         if (next == END_DOCUMENT) throw new EODException();
         else if (next == START_ELEMENT) return (currentTag = xml.getLocalName()).equals(tag);
         else if (next == END_ELEMENT && !xml.getLocalName().equals(tag)) return false;
      }

      return false;
   }

   /**
    * Returns the value for the given attribute.
    * @param attributeName the name of the attribute.
    * @return the value for the given attribute.
    * @throws XMLStreamException
    */
   public String getAttribute(String attributeName) throws XMLStreamException {
      final String value = xml.getAttributeValue(null, attributeName);
      if (value == null) throw new XMLStreamException("Could not find the attribute '" + attributeName + "'.");
      return value;
   }

   /**
    * Consumes the last read tag.
    */
   public void consume() {
      currentTag = null;
   }

   /**
    * Consumes a predefined tag.
    * @param tag the tag to be consumed.
    * @throws XMLStreamException
    * @throws EODException
    */
   public void consume(String tag) throws XMLStreamException, EODException {
      if (!isTag(tag)) throw new XMLStreamException("Invalid xml, expected '" + tag + "' tag not found.");
      consume();
   }
}