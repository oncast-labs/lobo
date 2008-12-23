/* File: ProfileCase.java
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

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import br.com.oncast.dev.lobo.Profile;

/**
 * Sub-tree of the profile reporting tree specific to a profile case.<br>
 * A profile case is mapped into a java class, called ProfileCase Class. ProfileCase Classes have at least one of its
 * methods annotated with the {@link Profile} annotation.
 * @author <a href="mailto:rodrigo.machado@oncast.com.br">Rodrigo Carvalho Machado</a>.
 */
public class ProfileCase {
   /**
    * The name of class coding the profile case.<br>
    * {@link String} is used instead of {@link Class} since these beans may be used during report mearging phase, where
    * no reflection to the class may be available.
    */
   private final String caseClass;

   /** The scenarios presented in the case. */
   private Map<String, ProfileScenario> scenarios = new LinkedHashMap<String, ProfileScenario>();

   /**
    * Creates a new instance of ProfileCase.
    * @param caseClass the name of the class coding the profile case.
    */
   public ProfileCase(String caseClass) {
      this.caseClass = caseClass;
   }

   /** @return the caseClass. */
   public String getCaseClass() {
      return caseClass;
   }

   /**
    * Adds a scenario to the {@link ProfileCase}.
    * @param scenario the scenario to be added.
    */
   public void addScenario(ProfileScenario scenario) {
      final ProfileScenario put = scenarios.put(scenario.getScenarioMethod(), scenario);
      assert put == null;
   }

   /**
    * Returns a previously added scenario given its <code>scenarioMethod</code>.
    * @param scenarioMethod the scenario method name.
    * @return the scenario for the <code>scenarioMethod</code> or <code>null</code> if it could not be found.
    */
   public ProfileScenario getScenario(String scenarioMethod) {
      return scenarios.get(scenarioMethod);
   }

   /**
    * Returns all the scenarios in the case.<br>
    * It is guaranteed that the returned collection of {@link ProfileScenario}s will iterate over them in the insertion
    * order.
    * @return all the scenarios in the case.
    */
   public Collection<ProfileScenario> getScenarios() {
      return scenarios.values();
   }
   
   /**
    * Removes a scenario from the case.
    * @param scenario the scenario to be removed.
    */
   public void removeScenario(ProfileScenario scenario) {
      scenarios.remove(scenario.getScenarioMethod());
   }
}
