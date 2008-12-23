/* File: ExecutionContext.java
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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import br.com.oncast.dev.lobo.MetricType;
import br.com.oncast.dev.lobo.UndefinedMetricException;
import br.com.oncast.dev.lobo.eval.bean.ProfileCase;
import br.com.oncast.dev.lobo.eval.bean.ProfileMetric;
import br.com.oncast.dev.lobo.eval.bean.ProfileReport;
import br.com.oncast.dev.lobo.eval.bean.ProfileScenario;

/**
 * Controls the information collected during profiling evaluation.
 */
public class ExecutionContext {
   // -- SINGLETON ASPECT -----------------------------------------------------
   /** The singleton instance. */
   private static final ExecutionContext _instance = new ExecutionContext();

   /**
    * Creates a new instance of ExecutionContext.<br>
    * Private to enforce the singleton design pattern.
    */
   private ExecutionContext() {}

   /**
    * Returns the singleton's instance.
    * @return the singleton's instance.
    */
   public static ExecutionContext getInstance() {
      return _instance;
   }

   // -- EXECUTION CONTEXT ASPECT ---------------------------------------------
   /**
    * Represents the evaluation of a metric.<br>
    * It collects every interval accounted and maintains the last interval's start timestamp.
    * @author <a href="mailto:rodrigo.machado@oncast.com.br">Rodrigo Carvalho Machado</a>.
    */
   private static class MetricEvaluation {
      /** The name of the metric. */
      public String key;

      /** The type of the metric. */
      public MetricType type;

      /** The timestamp of the last interval start. */
      public long lastIntervalStartTimestamp = -1;

      /** The metric intervals collected for the metric. */
      public List<Long> intervals = new ArrayList<Long>();
   }

   /** The report being currently evaluated. */
   private ProfileReport currentReport = new ProfileReport();

   /** The case being currently evaluated. */
   private ProfileCase currentCase;

   /** The scenario being currently evaluated. */
   private ProfileScenario currentScenario;

   /** Maintains all the metric intervals collected during the last/current profile scenario evaluation. */
   private Map<String, MetricEvaluation> metricEvals = new LinkedHashMap<String, MetricEvaluation>();

   /**
    * Switches the case being evaluated.
    * @param clazz the class that codes the profile case.
    */
   public void switchCase(Class<?> clazz) {
      currentReport.addCase(currentCase = new ProfileCase(clazz.getName()));
   }

   /**
    * Swithces the scenario being evaluated.
    * @param method the method that codes the profile scenario.
    */
   public void switchScenario(Method method) {
      mergeCurrentScenario();
      currentCase.addScenario(currentScenario = new ProfileScenario(method.getName()));
   }

   /**
    * Starts a metric interval evaluation.
    * @param metricName the name of the metric to have its interval evaluation started.
    * @param type the type of the metric.
    */
   public void startMetric(String metricName, MetricType type) {
      getMetricEvaluation(metricName, type).lastIntervalStartTimestamp = System.currentTimeMillis();
   }

   /**
    * Switches metric intervals on the metric identified by <code>metricName</code>.<br>
    * It first closes the referred metric and then closes it, but in a faster away.
    * @param metricName the name of the metric to be switched.
    */
   public void switchMetric(String metricName) {
      final MetricEvaluation eval = getMetricEvaluation(metricName, null);
      endMetric(eval);
      eval.lastIntervalStartTimestamp = System.currentTimeMillis();
   }

   /**
    * Ends a metric interval on the metric identified by <code>metricName</code>.
    * @param metricName the name of the metric to have its last interval ended.
    * @throws MetricIntervalUnstartedException if the metric had no opened metric intervals.
    */
   public void endMetric(String metricName) throws MetricIntervalUnstartedException {
      endMetric(getMetricEvaluation(metricName, null));
   }
   
   /**
    * Ends a metric interval on the metric identified by <code>metricName</code> if opened.
    * @param metricName the name of the metric to have its last interval ended.
    */
   public void endMetricIfOpened(String metricName) {
      try {
         endMetric(metricName);
      } catch (MetricIntervalUnstartedException e) {
         // Do nothing, it was already closed.
      }
   }
   
   /**
    * Returns the last report evaluated.
    * @return the last report evaluated.
    */
   public ProfileReport getReport() {
      mergeCurrentScenario();
      final ProfileReport localReport = currentReport;
      reset();
      return localReport;
   }

   /**
    * Resets the execution context.
    */
   public void reset() {
      currentReport = new ProfileReport();
      currentCase = null;
      currentScenario = null;
      metricEvals.clear();
   }

   /**
    * Merges all the {@link MetricEvaluation}s collected and stores than into the current scenario.<br>
    * This method simply returns if the {@link #currentScenario} is not defined (<code>null</code>).
    */
   private void mergeCurrentScenario() {
      if (currentScenario == null) return;

      for (final MetricEvaluation eval : metricEvals.values()) {
         final ProfileMetric metric = new ProfileMetric(eval.key);
         metric.setValue(eval.type.eval(eval.intervals));
         metric.setType(eval.type);
         currentScenario.addMetric(metric);
      }
      
      metricEvals.clear();
      currentScenario = null;
   }

   /**
    * Ends the last metric interval opened in a {@link MetricEvaluation}.
    * @param eval the {@link MetricEvaluation} to have its latest metric interval ended.
    * @throws MetricIntervalUnstartedException in case the given {@link MetricEvaluation} had no metric interval opened.
    */
   private void endMetric(MetricEvaluation eval) throws MetricIntervalUnstartedException {
      final long now = System.currentTimeMillis();
      if (eval.lastIntervalStartTimestamp == -1) throw new MetricIntervalUnstartedException(
            "Attempt to end an unstarted interval has been done on metric '" + eval.key + "'.");

      eval.intervals.add(now - eval.lastIntervalStartTimestamp);
      eval.lastIntervalStartTimestamp = -1;
   }
   
   /**
    * Returns a metric for the given <code>key</code>.<br>
    * If the metric has not been defined it is first defined and then returned; in this case the metric
    * <code>type</code> is needed.
    * @param key the key that identifies the metric within the scenario.
    * @param type the type of the metric. It may be <code>null</code> when it is guaranteed that the metric has
    *        already been defined.
    * @return the metric that corresponds to the <code>key</code>.
    * @throws UndefinedMetricException if the metric is undefined and there is no <code>type</code> supporting its
    *         definition.
    */
   private MetricEvaluation getMetricEvaluation(String key, MetricType type) throws UndefinedMetricException {
      MetricEvaluation eval = metricEvals.get(key);
      if (eval == null) {
         if (type == null) throw new UndefinedMetricException("Undefined metric: '" + key + "'.");

         eval = new MetricEvaluation();
         eval.key = key;
         eval.type = type;
         metricEvals.put(key, eval);
      }

      return eval;
   }
}
