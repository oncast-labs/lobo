/* File: ProfileReport.java
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


/**
 * Root bean of the profile reporting bean tree.
 */
public class ProfileReport {
   /** The cases that compose the report. */
   private Map<String, ProfileCase> cases = new LinkedHashMap<String, ProfileCase>();
   
   /**
    * Adds a case to the {@link ProfileReport}.
    * @param caze the case to be added.
    */
   public void addCase(ProfileCase caze) {
      final ProfileCase put = cases.put(caze.getCaseClass(), caze);
      assert put == null;
   }

   /**
    * Returns a previously added case given its <code>caseClass</code>.
    * @param caseClass the case class name.
    * @return the case for the <code>caseClass</code> or <code>null</code> if it could not be found.
    */
   public ProfileCase getCase(String caseClass) {
      return cases.get(caseClass);
   }
   
   /**
    * Returns all the cases in the report.<br>
    * It is guaranteed that the returned collection of {@link ProfileCase}s will iterate over them in the insertion order.
    * @return all the cases in the case.
    */
   public Collection<ProfileCase> getCases() {
      return cases.values();
   }
   
   /**
    * Removes a case from the report.
    * @param caze the case to be removed.
    */
   public void removeCase(ProfileCase caze) {
      cases.remove(caze.getCaseClass());
   }
}
