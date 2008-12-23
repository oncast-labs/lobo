/* File: ExecutionRunner.java
 * Date: 28/03/2007
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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import br.com.oncast.dev.lobo.MetricType;
import br.com.oncast.dev.lobo.Profile;
import br.com.oncast.dev.lobo.eval.bean.ProfileReport;

/**
 * Runner for a Collection of Profiler.
 * @author <a href="mailto:fernando.parisotto@gmail.com">Fernando Luiz Parisotto</a>.
 */
public class ExecutionProfiler {
   /**
    * ExecutionListener for execution profiler.
    */
   public static interface ExecutionListener {
      /**
       * Notifies the listener that a scenario was started.
       * @param clazz the profile case.
       * @param method the profile scenario within the case.
       */
      public abstract void runningScenario(Class<?> clazz, Method method);

      /**
       * Notifies that the running scenario terminated because of an error.
       * @param t the error that terminated the scenario.
       */
      public abstract void runningScenarioError(Throwable t);

      /**
       * Notifies the scenarios available in the given case as it is analised.
       * @param caze the case class.
       * @param methods the scenarios available.
       */
      public abstract void caseScenarios(Class<?> caze, List<Method> methods);
   }

   /** The Collection of Classes with {@link Profile}'s */
   private Collection<Class<?>> classes;

   /** The listener to be notified on execution events. */
   private final ExecutionListener listener;

   /**
    * Creates a new instance of ExecutionRunner.
    * @param classes the profile cases to be executed.
    * @param listener the execution listener to be notified.
    */
   public ExecutionProfiler(Collection<Class<?>> classes, ExecutionListener listener) {
      this.classes = classes;
      this.listener = listener;
   }

   /**
    * Execute the Profiler's.
    */
   public ProfileReport execute() {
      final ExecutionContext ctx = ExecutionContext.getInstance();
      for (final Class<?> clazz : classes) {
         ctx.switchCase(clazz);

         final List<Method> scenarios = listScenarios(clazz);
         listener.caseScenarios(clazz, scenarios);
         for (final Method method : scenarios) {
            ctx.switchScenario(method);

            final String metricName = method.getAnnotation(Profile.class).value();
            listener.runningScenario(clazz, method);
            ctx.startMetric(metricName, MetricType.SINGLE);
            try {
               runMetricsSource(clazz, method);
               ctx.endMetricIfOpened(metricName);
            } catch (Throwable t) {
               // Not using finally block to end metric as soon as possible.
               ctx.endMetricIfOpened(metricName);
               listener.runningScenarioError(t);
               // TODO handle this throwable.
            }
         }
      }

      return ctx.getReport();
   }

   /**
    * Run the method annotated with {@link Profile}.
    * @param clazz the profiler class.
    * @param method the method to be run.
    * @throws ExecutionProfilerException
    */
   private void runMetricsSource(final Class<?> clazz, final Method method) throws ExecutionProfilerException {
      try {
         method.invoke(clazz.newInstance());
      } catch (InvocationTargetException e) {
         listener.runningScenarioError(e.getTargetException());
      } catch (IllegalAccessException e) {
         throw new ExecutionProfilerException("Unaccessible lobo-scenario: '" + method + "'.", e);
      } catch (Exception e) {
         // TODO handle this.
         throw new RuntimeException(e);
      }
   }

   /**
    * Lists the profile scenarios within a case class.
    * @param clazz the case class to be introspected.
    * @return the profile scenarios within a case class.
    */
   private List<Method> listScenarios(Class<?> clazz) {
      final List<Method> scenarios = new ArrayList<Method>();
      for (Method method : clazz.getMethods()) {
         if (!isProfileScenario(method)) continue;
         scenarios.add(method);
      }

      return scenarios;
   }

   /**
    * Verify if a method is a respect a contract of {@link Profile}.
    * @param method the method to be verified.
    * @return <code>true</code> if the method is a {@link Profile}
    */
   private boolean isProfileScenario(final Method method) {
      // TODO handle invalid method.
      final boolean isPublic = Modifier.isPublic(method.getModifiers());
      final boolean isVoid = method.getReturnType() == void.class;
      final boolean isNoArgs = method.getGenericParameterTypes().length == 0;
      boolean isAnnotated = method.isAnnotationPresent(Profile.class);

      return isAnnotated && isNoArgs && isPublic && isVoid;
   }

}
