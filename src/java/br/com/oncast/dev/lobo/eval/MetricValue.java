/* File: MetricValue.java
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
package br.com.oncast.dev.lobo.eval;

/**
 * A metric value.
 * @author <a href="mailto:rodrigo.machado@oncast.com.br">Rodrigo Carvalho Machado</a>.
 */
public class MetricValue {
   /** The metric name. */
   public final String name;

   /** The metric value. */
   public final double value;

   /**
    * Creates a new instance of MetricValue.
    * @param name the metric name.
    * @param value the metric value.
    */
   public MetricValue(String name, double value) {
      super();
      this.name = name;
      this.value = value;
   }

   /**
    * @see java.lang.Object#toString()
    */
   @Override
   public String toString() {
      return name + "=" + value;
   }
}