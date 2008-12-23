/* File: MetricValue.java
 * Date: 16/04/2007
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
package br.com.oncast.dev.lobo.merge.bean;

import br.com.oncast.dev.lobo.MetricType;

/**
 * Value for a metric in merge context.
 */
public class MetricValue {
   /** The build name. */
   private String buildName;

   /** The metric type. */
   private MetricType type;

   /** The metric value. */
   private double value;

   /** @return the buildName. */
   public String getBuildName() {
      return buildName;
   }

   /** @param buildName the buildName to set. */
   public void setBuildName(String buildName) {
      this.buildName = buildName;
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
