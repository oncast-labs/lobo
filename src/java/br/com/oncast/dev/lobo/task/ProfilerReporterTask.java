/* File: ProfilerReporterTask.java
 * Date: 24/04/2007
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
package br.com.oncast.dev.lobo.task;

import static org.apache.tools.ant.Project.MSG_ERR;

import java.io.File;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import br.com.oncast.dev.lobo.report.ReportPlotter;
import br.com.oncast.dev.lobo.report.ReportTransformer;
import br.com.oncast.dev.lobo.report.ReporterFactory;

/**
 * Ant task for reporting the merge result.
 */
public class ProfilerReporterTask extends Task {

   /** The report plotter. */
   private ReportPlotter plotter;

   /** The report transformer. */
   private ReportTransformer transformer;

   /** The merged source. */
   private File input;

   /** The generate report. */
   private File output;

   /**
    * @see org.apache.tools.ant.Task#init()
    */
   @Override
   public void init() throws BuildException {
      plotter = ReporterFactory.createPlotter();
      transformer = ReporterFactory.createTransformer();
   }

   /** @param input the input to set. */
   public void setInput(File input) {
      this.input = input;
      if (!input.exists()) throw new BuildException("The input file '" + input + "' does not exist.");
   }

   /** @param output The output to set. */
   public void setOutput(File output) {
      this.output = output;
      if (output.exists() && !output.delete()) throw new BuildException("The output '" + output
            + "' could not be deleted.");
   }

   /**
    * @see org.apache.tools.ant.Task#execute()
    */
   @Override
   public void execute() throws BuildException {
      validatePropertyContext();
      plotter.plot(input, output);
      transformer.transform(input, output);
   }

   /**
    * Validates the task's property context.
    */
   private void validatePropertyContext() {
      if (input == null) log("The input merged source file must be defined.", MSG_ERR);
      if (output == null) log("The output must be defined.", MSG_ERR);
   }

}
