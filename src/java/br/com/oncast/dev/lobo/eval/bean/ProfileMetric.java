/* File: ProfileMetric.java
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
package br.com.oncast.dev.lobo.eval.bean;

import br.com.oncast.dev.lobo.MetricType;


/**
 * A leaf in the profile reporting bean tree.<br>
 * It stores information about a single profiling metric.
 * @author <a href="mailto:rodrigo.machado@oncast.com.br">Rodrigo Carvalho Machado</a>.
 */
public class ProfileMetric {
   /** The key of the metric. Uniquely defined within a {@link ProfileScenario}. */
   private final String key;
   
   /** The type of the metric. */
   private MetricType type;
   
   /** The metric value. */
   private double value;

   /**
    * Creates a new instance of ProfileMetric.
    * @param key the name of the metric.
    */
   public ProfileMetric(String key) {
      this.key = key;
   }
   
   /** @return the key. */
   public String getKey() {
      return key;
   }

   /** @return the type. */
   public MetricType getType() {
      return type;
   }

   /** @param type the type to set. */
   public void setType(MetricType type) {
      this.type = type;
   }

   /** @return the value. */
   public double getValue() {
      return value;
   }

   /** @param value the value to set. */
   public void setValue(double value) {
      this.value = value;
   }
}
