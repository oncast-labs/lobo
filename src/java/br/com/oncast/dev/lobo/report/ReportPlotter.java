/* File: ReportPlotter.java
 * Date: 04/04/2007
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
package br.com.oncast.dev.lobo.report;

import java.io.File;

/**
 * Interface definition of a report plotter, to plot graphics used on the report.
 */
public interface ReportPlotter {

   /**
    * Plot's graphics based on the merged report.
    * @param input The merged report.
    * @param outputDir The base dir to plot the graphics.
    */
   void plot(File input, File outputDir);

}
