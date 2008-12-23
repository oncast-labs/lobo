/* File: ProfileScenario.java
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

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A sub-tree of the reporting profile bean tree specific to a profile scenario.<br>
 * Profile Scenarios are java methods inside a Profile Case.
 * @author <a href="mailto:rodrigo.machado@oncast.com.br">Rodrigo Carvalho Machado</a>.
 */
public class ProfileScenario {
   /**
    * The name of the method coding the scenario.<br>
    * {@link String} is used instead of {@link Method} since these beans may be used during report mearging phase, where
    * no reflection to the method may be available.
    */
   private final String scenarioMethod;

   /** The metrics collected in the scenario. */
   private Map<String, ProfileMetric> metrics = new LinkedHashMap<String, ProfileMetric>();

   /**
    * Creates a new instance of ProfileScenario.
    * @param scenarioMethod the name of the method coding the scenario.
    */
   public ProfileScenario(String scenarioMethod) {
      this.scenarioMethod = scenarioMethod;
   }

   /** @return the scenarioMethod. */
   public String getScenarioMethod() {
      return scenarioMethod;
   }

   /**
    * Adds a {@link ProfileMetric} to the {@link ProfileScenario}.
    */
   public void addMetric(ProfileMetric metric) {
      final ProfileMetric old = metrics.put(metric.getKey(), metric);
      assert old == null;
   }

   /**
    * Returns a previously added metric given its <code>key</code>.
    * @param key the metric key.
    * @return the metric for the <code>key</code> or <code>null</code> if it could not be found.
    */
   public ProfileMetric getMetric(String key) {
      return metrics.get(key);
   }

   /**
    * Returns all the collected metrics.<br>
    * It is guaranteed that the returned collection of {@link ProfileMetric}s while iterate over them in the insertion
    * order.
    * @return all the collected metrics.
    */
   public Collection<ProfileMetric> getMetrics() {
      return metrics.values();
   }
   
   /**
    * Removes a metric from the scenario.
    * @param metric the metric to be removed.
    */
   public void removeMetric(ProfileMetric metric) {
      metrics.remove(metric.getKey());
   }
}
