/* File: ProfilerTask.java
 * Date: 20/03/2007
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;

import br.com.oncast.dev.lobo.eval.ExecutionProfiler;
import br.com.oncast.dev.lobo.eval.ExecutionProfiler.ExecutionListener;
import br.com.oncast.dev.lobo.eval.bean.ProfileReport;
import br.com.oncast.dev.lobo.io.IOFactory;
import br.com.oncast.dev.lobo.io.IOFactoryImpl;
import br.com.oncast.dev.lobo.io.LoboIOException;
import br.com.oncast.dev.lobo.io.ProfileWriter;
import br.com.oncast.dev.lobo.task.loader.FilesetClassLoader;

/**
 * Ant task for performance analysis.
 */
public class ProfilerTask extends Task {
   /** The factory for io objects. */
   private static IOFactory IO_FACTORY = new IOFactoryImpl();

   /** The output file to write the results to. */
   private File output;

   /** The batch profile to be executed. */
   private BatchProfile batchProfile;

   /** The reference for classpath execution. */
   private Reference classpathRef;

   /** The list of classpath for execution. */
   private List<Path> paths = new ArrayList<Path>();

   /**
    * Add a new Path.
    * @param path to added.
    */
   public void addClasspath(Path path) {
      paths.add(path);
   }

   /**
    * Setter for ClasspathRef.
    * @param classpathRef the classpathRef to be settled.
    */
   public void setClasspathRef(Reference classpathRef) {
      this.classpathRef = classpathRef;
   }

   /**
    * Creates the {@link BatchProfile} to be executed.
    * @return the created {@link BatchProfile}.
    */
   public BatchProfile createBatchProfile() {
      if (batchProfile != null) throw new BuildException("BuildProfile already defined.");
      batchProfile = new BatchProfile();
      return batchProfile;
   }

   /**
    * Sets the output file to write results to.
    * @param outputFile the output file.
    */
   public void setOutput(String outputFile) {
      output = new File(getProject().getBaseDir(), outputFile);

      if (output.exists() && output.isFile() && !output.delete()) {
         throw new BuildException("Could not delete file '" + output + "'.");
      }
   }

   /**
    * Executes the task.
    */
   public void execute() {
      if (output == null) throw new BuildException("output must be defined.");
      if (batchProfile == null) throw new BuildException("batchProfile must be defined.");
      if (classpathRef == null && paths.isEmpty()) throw new BuildException("classpathRef or path must be defined.");

      runTask();
   }

   /**
    * Retrieves the Execution ClassLoader.
    * @return the execution ClassLoader.
    */
   private ClassLoader getExecutionClassLoader() {
      final Path mergedClasspath = new Path(getProject());
      for (Path path : paths) {
         mergedClasspath.add(path);
      }
      if (classpathRef != null) {
         Path referencedPath = (Path) classpathRef.getReferencedObject(getProject());
         mergedClasspath.add(referencedPath);
      }

      return new AntClassLoader(getClass().getClassLoader(), getProject(), mergedClasspath, true);
   }

   /**
    * Runs the profile task.
    */
   private void runTask() {
      log("Running performance tests...");

      final ExecutionListener listener = new ExecutionListener() {
         @Override
         public void runningScenario(Class<?> clazz, Method method) {
            log("Running scenario '" + clazz.getName() + "#" + method.getName() + "'.");
         }

         @Override
         public void runningScenarioError(Throwable t) {
            log("Error during scenario evaluation: '" + t.toString() + "'.", Project.MSG_ERR);
            t.printStackTrace(System.err);
         }

         @Override
         public void caseScenarios(Class<?> caze, List<Method> methods) {
            log("Running case '" + caze.getName() + "'. Scenarios found:" + (methods.size() == 0 ? " NONE." : ""));
            for (Method method : methods) {
               log("  - " + method.getName());
            }
         }
      };

      final FilesetClassLoader loader = new FilesetClassLoader(batchProfile.getFilesets(), getExecutionClassLoader(),
            getProject());
      final ProfileReport report = new ExecutionProfiler(loader.getLoadedClasses(), listener).execute();
      writeReport(report);
   }

   /**
    * Writes the report the the output file.
    * @param report the report to be written.
    */
   private void writeReport(final ProfileReport report) {
      FileOutputStream fos = null;
      try {
         fos = new FileOutputStream(output);
         final ProfileWriter writer = IO_FACTORY.createProfileWriter(fos);
         writer.write(report);
      } catch (LoboIOException e) {
         throw new BuildException("Cannot write to output file '" + output + "'.", e);
      } catch (FileNotFoundException e) {
         throw new BuildException("Cannot write to output file '" + output + "'.", e);
      } finally {
         try {
            if (fos!=null) fos.close();
         } catch (IOException e) {
            // Ignore exception
         }
      }
   }
}
