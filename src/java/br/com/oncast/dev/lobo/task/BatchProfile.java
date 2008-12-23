/* File: BatchProfile.java
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
package br.com.oncast.dev.lobo.task;

import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.types.FileSet;

/**
 * {@link ProfilerTask} element to store multiple profiling cases to be executed in batch.
 */
public class BatchProfile {
   /** The list of classes to be processed. */
   private List<FileSet> filesets = new ArrayList<FileSet>();

   /**
    * Adds a {@link FileSet} to the list of filesets to be processed.
    * @param fileSet the {@link FileSet} to be added.
    */
   public void addFileset(FileSet fileSet) {
      filesets.add(fileSet);
   }

   /** @return the filesets. */
   public List<FileSet> getFilesets() {
      return filesets;
   }
}
