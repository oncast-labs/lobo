/* File: Telemetry.java
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

import br.com.oncast.dev.lobo.eval.ExecutionContext;

/**
 * Telemetry collector.<br>
 * Telemetry is used to collect profiling metrics in each profile scenario.<br>
 * It is simple to define metrics in your scenario, take a look at the example:<br>
 * <pre>
 *   &#0064;Profile
 *   public void profileForLoop() throws Exception {
 *     for (int i=0;i&lt;1000;i++) {
 *       for (int j=0;j&lt;100;j++) Thread.sleep(10);
 *     }
 *   }
 * </pre>
 * In this code the default metric "scenario-timespan" will collect the overall time of the method execution which will
 * be something like 1000000 milliseconds. But imagine that what is to be mesured is not the performance of all code,
 * what is to be done is to test the <code>for (int j=0;j<100;j++) Thread.sleep(10);</code> line 1000 times and
 * collect the average result.<br>
 * This can be done by defining a new metric, let's say "100-sleep", and properly invoke telemetry. This is the final
 * code:<br>
 * <pre>
 *   &#0064;Profile
 *   public void profileForLoop() throws Exception {
 *     for (int i=0;i&lt;1000;i++) {
 *       Telemetry.startMetric("100-sleep",MetricType.AVERAGE);
 *       for (int j=0;j&lt;100;j++) Thread.sleep(10);
 *       Telemetry.endMetric("100-sleep");
 *     }
 *   }
 * </pre>
 */
public class Telemetry {
   /** The execution context. */
   private static ExecutionContext ctx = ExecutionContext.getInstance();

   /**
    * Starts collecting the given metric.
    * @param metricName the metric name.
    */
   public static void startMetric(String metricName) {
      ctx.startMetric(metricName, MetricType.SINGLE);
   }

   /**
    * Starts collecting the given metric.
    * @param metricName the metric name.
    * @param type the metric collection type.
    */
   public static void startMetric(String metricName, MetricType type) {
      ctx.startMetric(metricName, type);
   }

   /**
    * Switches the metric.<br>
    * Only valid for multi valored metrics.
    * @param metricName the metric name.
    */
   public static void switchMetric(String metricName) {
      ctx.switchMetric(metricName);
   }

   /**
    * Ends collecting the given metric.
    * @param metricName the metric name.
    */
   public static void endMetric(String metricName) {
      ctx.endMetric(metricName);
   }
}
