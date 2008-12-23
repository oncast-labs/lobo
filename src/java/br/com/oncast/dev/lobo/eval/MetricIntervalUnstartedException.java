/* File: MetricIntervalUnstartedException.java
 * Date: 03/04/2007
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
package br.com.oncast.dev.lobo.eval;

/**
 * Thrown when it was attempted to end a metric interval but it was not opened.
 */
public class MetricIntervalUnstartedException extends RuntimeException {
   private static final long serialVersionUID = 1L;
   public MetricIntervalUnstartedException() {
      super();
   }
   public MetricIntervalUnstartedException(String message, Throwable cause) {
      super(message, cause);
   }
   public MetricIntervalUnstartedException(String message) {
      super(message);
   }
   public MetricIntervalUnstartedException(Throwable cause) {
      super(cause);
   }
}
