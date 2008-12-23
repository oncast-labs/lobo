/* File: LoboIOException.java
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

/**
 * General exception for merging exceptions.
 */
public class LoboIOException extends RuntimeException {
   private static final long serialVersionUID = 1L;
   public LoboIOException() {
      super();
   }
   public LoboIOException(String message, Throwable cause) {
      super(message, cause);
   }
   public LoboIOException(String message) {
      super(message);
   }
   public LoboIOException(Throwable cause) {
      super(cause);
   }
}
