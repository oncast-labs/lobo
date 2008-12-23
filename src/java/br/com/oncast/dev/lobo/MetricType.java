/* File: MetricType.java
 * Date: 20/03/2007
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
package br.com.oncast.dev.lobo;

import java.util.List;

/**
 * The metric collectin type.
 */
public enum MetricType {

   SINGLE {

      /**
       * @see br.com.oncast.dev.lobo.MetricType#eval(List<Long>)
       */
      public double eval(List<Long> values) {
         return values.get(0);
      }
   },

   AVERAGE {

      /**
       * @see br.com.oncast.dev.lobo.MetricType#eval(List<Long>)
       */
      public double eval(List<Long> values) {
         double sum = 0;
         for (final long value : values) {
            sum += value;
         }

         return sum / values.size();
      }
   },

   MAX {

      /**
       * @see br.com.oncast.dev.lobo.MetricType#eval(List<Long>)
       */
      public double eval(List<Long> values) {
         double max = Double.NEGATIVE_INFINITY;

         for (final long value : values) {
            max = Math.max(max, value);
         }

         return max;
      }
   },

   MIN {

      /**
       * @see br.com.oncast.dev.lobo.MetricType#eval(List<Long>)
       */
      public double eval(List<Long> values) {
         double min = Double.POSITIVE_INFINITY;

         for (final long value : values) {
            min = Math.min(min, value);
         }

         return min;
      }
   };

   /**
    * Evaluate the metric.
    * @param values A List of values to evaluate.
    * @return The evaluated metric.
    */
   public abstract double eval(List<Long> values);

}
