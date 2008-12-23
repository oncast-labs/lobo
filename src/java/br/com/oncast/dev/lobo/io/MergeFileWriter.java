/* File: MergeFileWriter.java
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
package br.com.oncast.dev.lobo.io;

import br.com.oncast.dev.lobo.merge.bean.MergeMetric;
import br.com.oncast.dev.lobo.merge.bean.MetricValue;

/**
 * Writer for a merge file report.
 */
public interface MergeFileWriter {
   /**
    * Initiates a profiler case.
    * @param currentCaseName the profiler case name.
    */
   public abstract void startCase(String currentCaseName);

   /**
    * Initiates a profiler scenario.
    * @param currentScenarioName the profiler scenario name.
    */
   public abstract void startScenario(String currentScenarioName);

   /**
    * Initiates a metric.
    * @param metric the metric to be written.
    */
   public abstract void startMetric(MergeMetric metric);

   /**
    * Writes a metric value to the merge file.
    * @param value the metric value to be written.
    */
   public abstract void writeMetricValue(MetricValue value);

   /**
    * Ends the merge file creation session.
    */
   public abstract void close();
}
