/* File: MergeMetric.java
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

/**
 * Represents a metric in a merge file.
 */
public class MergeMetric {
   private String caze;
   private String scenario;
   private String metric;

   /** @return the caze. */
   public String getCase() {
      return caze;
   }

   /** @param caze the caze to set. */
   public void setCase(String caze) {
      this.caze = caze;
   }

   /** @return the scenario. */
   public String getScenario() {
      return scenario;
   }

   /** @param scenario the scenario to set. */
   public void setScenario(String scenario) {
      this.scenario = scenario;
   }

   /** @return the metric. */
   public String getMetric() {
      return metric;
   }

   /** @param metric the metric to set. */
   public void setMetric(String metric) {
      this.metric = metric;
   }
}
