/* File: MergeFileReader.java
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
 * Reader for a merge file report.
 */
public interface MergeFileReader {
   /**
    * Returns <code>true</code> if the {@link MergeFileReader} has unread cases.
    * @return <code>true</code> if the {@link MergeFileReader} has unread cases.
    */
   public abstract boolean hasCases();

   /**
    * Advances to the following case.
    */
   public abstract void nextCase();

   /**
    * Returns <code>true</code> if the {@link MergeFileReader} has unread scenarios within the current case.
    * @return <code>true</code> if the {@link MergeFileReader} has unread scenarios.
    */
   public abstract boolean hasScenarios();

   /**
    * Advances to the following scenario.
    */
   public abstract void nextScenario();

   /**
    * Returns <code>true</code> if the {@link MergeFileReader} has unread metrics within the current scenario.
    * @return <code>true</code> if the {@link MergeFileReader} has unread metrics.
    */
   public abstract boolean hasMetrics();

   /**
    * Returns the next {@link MergeMetric}.
    * @return the next {@link MergeMetric}.
    */
   public abstract MergeMetric nextMetric();

   /**
    * Returns <code>true</code> if the {@link MergeFileReader} has unread metric values within the current metric.
    * @return <code>true</code> if the {@link MergeFileReader} has unread metric values.
    */
   public abstract boolean hasMetricValues();

   /**
    * Returns the next {@link MetricValue}.
    * @return the next {@link MetricValue}.
    */
   public abstract MetricValue nextMetricValue();

   /**
    * The name of the current case.
    * @return the name of the current case.
    */
   public abstract String currentCaseName();

   /**
    * The name of the current scenario.
    * @return the name of the current scenario.
    */
   public abstract String currentScenarioName();
}
