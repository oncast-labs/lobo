/* File: ProfilerMergeTask.java
 * Date: 16/04/2007
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

import br.com.oncast.dev.lobo.merge.MergingEngine;

/**
 * Ant task to merge profile results.<br>
 * The profiler merger is design to create a new merge result xml based on an (optional) merge result xml and a profiler
 * result xml. Basically it creates a new merge result xml appending the source merge xml with the profiler result xml.
 */
public class ProfilerMergeTask extends Task {
   /** The name of the current build. */
   private String buildName;

   /** The merge file. */
   private File merge;

   /** The build specific metrics. */
   private File buildMetrics;

   /**
    * The optional build name.<br>
    * If the build name is not set the merger will try to infer the next build name from the merge source.
    * @param buildName the name of the build.
    */
   public void setBuildName(String buildName) {
      this.buildName = buildName;
   }

   /**
    * The merge file.<br>
    * This merge file stores performance information of all the builds it merged. It will be incremented if the
    * information from the profile report.
    * @param mergeFile the merge source file path as a {@link String}.
    */
   public void setMerge(String mergeFile) {
      merge = new File(getProject().getBaseDir(), mergeFile);
   }

   /**
    * The mandatory profiler source file.<br>
    * This is the profiler results that must be merged with the merger source to generate the output file.
    * @param buildMetrics the profiler source file path as a {@link String}.
    */
   public void setBuildMetrics(String buildMetrics) {
      this.buildMetrics = new File(getProject().getBaseDir(), buildMetrics);
      if (!this.buildMetrics.exists()) throw new BuildException("The profiler source file '" + this.buildMetrics
            + "' does not exist.");
   }

   /**
    * @see org.apache.tools.ant.Task#execute()
    */
   @Override
   public void execute() throws BuildException {
      validatePropertyContext();
      final File tmpFile = new File(merge.getParent(), "MERGE-TEMP-" + System.currentTimeMillis() + ".xml");
      MergingEngine.merge(buildName, tmpFile, buildMetrics, merge.exists() ? merge : null);
      if (merge.exists()) merge.delete();
      tmpFile.renameTo(merge);
   }

   /**
    * Validates the task's property context.
    */
   private void validatePropertyContext() {
      if (buildMetrics == null) {
         log("The profiler source file must be defined.", MSG_ERR);
         throw new BuildException("Could not merge profiler results.");
      }
   }
}
