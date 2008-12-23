/* File: IOFactory.java
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
import java.io.OutputStream;

/**
 * Factory for Merge IO Components.
 */
public interface IOFactory {
   /**
    * Creates a {@link ProfileReader} for the given {@link InputStream}.
    * @param stream the stream to be read.
    * @return the created {@link ProfileReader}.
    */
   public abstract ProfileReader createProfilerReader(InputStream stream);
   
   /**
    * Creates a {@link ProfileWriter} for the given {@link OutputStream}.
    * @param stream the stream to be written to.
    * @return the created {@link ProfileWriter}.
    */
   public abstract ProfileWriter createProfileWriter(OutputStream stream);
   
   /**
    * Creates a {@link MergeFileReader} for the given {@link InputStream}.
    * @param stream the stream to be read.
    * @return the created {@link MergeFileReader}.
    */
   public abstract MergeFileReader createMergeReader(InputStream stream);
   
   /**
    * Creates a {@link MergeFileWriter} for the given {@link OutputStream}.
    * @param stream the stream to be written.
    * @return the created {@link MergeFileWriter}.
    */
   public abstract MergeFileWriter createMergeWriter(OutputStream stream);
   
   /**
    * Creates a {@link MergeFileReader} simulating an empty merge file.
    * @return the created {@link MergeFileReader}.
    */
   public abstract MergeFileReader createEmptyMergeReader();
}
