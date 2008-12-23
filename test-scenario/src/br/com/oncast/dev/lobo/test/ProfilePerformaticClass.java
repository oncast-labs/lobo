/* File: ProfilePerformaticClass.java
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
package br.com.oncast.dev.lobo.test;

import static br.com.oncast.dev.lobo.Telemetry.endMetric;
import static br.com.oncast.dev.lobo.Telemetry.startMetric;
import br.com.oncast.dev.lobo.Profile;

/**
 * Performance profiler for {@link PerformaticClass}.
 */
public class ProfilePerformaticClass {
   /**
    * Profiles the {@link PerformaticClass#performaticMethod()} method.
    */
   @Profile
   public void profilePerformaticMethod() {
      startMetric("performaticMethod");
      PerformaticClass.performaticMethod();
      endMetric("performaticMethod");
   }
   
   /**
    * Profiles the {@link PerformaticClass#unperformaticMethod()} method.
    */
   @Profile
   public void profileUnperformaticMethod() {
      startMetric("unperformaticMethod");
      PerformaticClass.unperformaticMethod();
      endMetric("unperformaticMethod");
   }
}
