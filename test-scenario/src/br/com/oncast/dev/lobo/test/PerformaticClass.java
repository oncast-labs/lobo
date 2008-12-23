/* File: PerformaticClass.java
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

/**
 * Class for performance tool testing.
 */
public class PerformaticClass {
   /**
    * Counts to 100000 as fast as it can.
    */
   public static void performaticMethod() {
      for (int i = 0; i < 100000; i++)
         ;
   }

   /**
    * Counts to 100 but waits 1 millisecond after each new number.
    */
   public static void unperformaticMethod() {
      for (int i = 0; i < 100; i++) {
         try {
            Thread.sleep(1);
         } catch (InterruptedException e) {
            throw new RuntimeException("Untreated InterruptedException on PerformaticClass#unperformaticMethod.", e);
         }
      }
   }
}
