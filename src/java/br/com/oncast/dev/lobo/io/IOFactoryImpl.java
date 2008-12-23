/* File: IOFactoryImpl.java
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
 * Production implementation of {@link IOFactory}.
 */
public class IOFactoryImpl implements IOFactory {

   /**
    * @see br.com.oncast.dev.lobo.io.IOFactory#createEmptyMergeReader()
    */
   @Override
   public MergeFileReader createEmptyMergeReader() {
      return new EmptyMergeFileReader();
   }

   /**
    * @see br.com.oncast.dev.lobo.io.IOFactory#createMergeReader(java.io.InputStream)
    */
   @Override
   public MergeFileReader createMergeReader(InputStream stream) {
      return new InputStreamMergeFileReader(stream);
   }

   /**
    * @see br.com.oncast.dev.lobo.io.IOFactory#createMergeWriter(java.io.OutputStream)
    */
   @Override
   public MergeFileWriter createMergeWriter(OutputStream stream) {
      return new OutputStreamMergeFileWriter(stream);
   }

   /**
    * @see br.com.oncast.dev.lobo.io.IOFactory#createProfilerReader(java.io.InputStream)
    */
   @Override
   public ProfileReader createProfilerReader(InputStream stream) {
      return new InputStreamProfileReader(stream);
   }

   @Override
   public ProfileWriter createProfileWriter(OutputStream stream) {
      return new OutputStreamProfileWriter(stream);
   }
}
