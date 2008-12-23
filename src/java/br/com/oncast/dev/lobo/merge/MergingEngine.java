/* File: MergingEngine.java
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
package br.com.oncast.dev.lobo.merge;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import br.com.oncast.dev.lobo.eval.bean.ProfileCase;
import br.com.oncast.dev.lobo.eval.bean.ProfileMetric;
import br.com.oncast.dev.lobo.eval.bean.ProfileReport;
import br.com.oncast.dev.lobo.eval.bean.ProfileScenario;
import br.com.oncast.dev.lobo.io.IOFactory;
import br.com.oncast.dev.lobo.io.IOFactoryImpl;
import br.com.oncast.dev.lobo.io.MergeFileReader;
import br.com.oncast.dev.lobo.io.MergeFileWriter;
import br.com.oncast.dev.lobo.io.ProfileReader;
import br.com.oncast.dev.lobo.merge.bean.MergeMetric;
import br.com.oncast.dev.lobo.merge.bean.MetricValue;

/**
 * Engine to merge merge file xmls and profiler result file xmls into a merge file xmls that unifies both.
 */
public class MergingEngine {
   /** Factory for IO Components. */
   private static IOFactory IO_FACTORY = new IOFactoryImpl();

   /**
    * The merge stream.<br>
    * <code>null</code> if there is no merge stream source.
    */
   private final MergeFileReader mergeReader;

   /** The profiler stream source. */
   private final ProfileReader profilerReader;

   /** The output stream destination. */
   private final MergeFileWriter outputWriter;

   /** The current build name. */
   private final String buildName; // TODO Write the appropriate name.

   /**
    * Creates a new instance of MergingEngine.
    * @param buildName the build identifier.
    * @param outputStream the output stream source.
    * @param mergeStream the merge stream source.
    * @param profilerStream the profiler stream source.
    */
   private MergingEngine(String buildName, OutputStream outputStream, InputStream mergeStream, InputStream profilerStream) {
      super();
      this.buildName = buildName;
      this.mergeReader = mergeStream == null ? IO_FACTORY.createEmptyMergeReader() : IO_FACTORY
            .createMergeReader(mergeStream);
      this.profilerReader = IO_FACTORY.createProfilerReader(profilerStream);
      this.outputWriter = IO_FACTORY.createMergeWriter(outputStream);
   }

   /**
    * Merges the given <code>profilerSource</code> and <code>mergeSource</code> into the <code>output</code>.
    * @param buildName the build identifier.
    * @param output the output file to store the results of the merge.
    * @param profilerSource the profiler to be merged.
    * @param mergeSource the merge source. <code>null</code> to start from scratch.
    */
   public static void merge(String buildName, File output, File profilerSource, File mergeSource) {
      FileInputStream pfs = null;
      FileInputStream mfs = null;
      FileOutputStream oos = null;
      BufferedInputStream pbis = null;
      BufferedInputStream mbis = null;
      BufferedOutputStream obos = null;

      try {
         pfs = new FileInputStream(profilerSource);
         mfs = mergeSource == null ? null : new FileInputStream(mergeSource);
         oos = new FileOutputStream(output);

         pbis = new BufferedInputStream(pfs);
         mbis = mfs == null ? null : new BufferedInputStream(mfs);
         obos = new BufferedOutputStream(oos);

         new MergingEngine(buildName, obos, mbis, pbis).merge();
      } catch (IOException e) {
         // TODO Handle error.
         throw new RuntimeException("Untreated IOException on MergingEngine#merge.", e);
      } finally {
         close(obos, mbis, pbis, oos, mfs, pfs);
      }
   }

   /**
    * Closes a {@link Closeable} and suppresses any thrown {@link IOException}.
    * @param cs the list of {@link Closeable}s to be closed.
    */
   private static void close(Closeable... cs) {
      for (Closeable c : cs) {
         if (c==null) continue;
         
         try {
            c.close();
         } catch (IOException e) {
            // Ignore exception.
         }
      }
   }

   /**
    * Merges the full profile report.
    */
   private void merge() {
      final ProfileReport profile = profilerReader.readProfile();

      while (mergeReader.hasCases()) {
         mergeReader.nextCase();
         final ProfileCase caze = profile.getCase(mergeReader.currentCaseName());
         if (caze==null) {
            copyCase();
            continue;
         }

         mergeCase(caze);
         profile.removeCase(caze);
      }

      writeMissingCases(profile);
      outputWriter.close();
   }

   /**
    * Merges a case.
    * @param caze the case to be merged.
    */
   private void mergeCase(final ProfileCase caze) {
      outputWriter.startCase(caze.getCaseClass());

      while (mergeReader.hasScenarios()) {
         mergeReader.nextScenario();
         final ProfileScenario scenario = caze.getScenario(mergeReader.currentScenarioName());
         if (scenario==null) {
            copyScenario();
            continue;
         }

         mergeScenario(caze, scenario);
         caze.removeScenario(scenario);
      }

      writeMissingScenarios(caze);
   }

   /**
    * Merges a scenario.
    * @param caze the case in which the scenario is registered.
    * @param scenario the scenario to be merged.
    */
   private void mergeScenario(final ProfileCase caze, final ProfileScenario scenario) {
      outputWriter.startScenario(scenario.getScenarioMethod());

      while (mergeReader.hasMetrics()) {
         final MergeMetric mergeMetric = mergeReader.nextMetric();
         outputWriter.startMetric(mergeMetric);

         while (mergeReader.hasMetricValues()) {
            final MetricValue value = mergeReader.nextMetricValue();
            outputWriter.writeMetricValue(value);
         }

         final ProfileMetric metric = scenario.getMetric(mergeMetric.getMetric());
         if (metric == null) continue;

         final MetricValue value = new MetricValue();
         value.setBuildName(buildName);
         value.setType(metric.getType());
         value.setValue(metric.getValue());

         outputWriter.writeMetricValue(value);
         scenario.removeMetric(metric);
      }

      writeMissingMetrics(caze, scenario);
   }

   /**
    * Copies a case from merge source to output.
    */
   private void copyCase() {
      outputWriter.startCase(mergeReader.currentCaseName());
      while (mergeReader.hasScenarios()) {
         mergeReader.nextScenario();
         copyScenario();
      }
   }

   /**
    * Copies a scenario from merge souce to output.
    */
   private void copyScenario() {
      outputWriter.startScenario(mergeReader.currentScenarioName());
      while (mergeReader.hasMetrics()) {
         final MergeMetric mergeMetric = mergeReader.nextMetric();
         outputWriter.startMetric(mergeMetric);
         while (mergeReader.hasMetricValues()) {
            outputWriter.writeMetricValue(mergeReader.nextMetricValue());
         }
      }
   }

   /**
    * Writes the missing cases in the report.
    * @param report the report filled up with only the missing cases.
    */
   private void writeMissingCases(ProfileReport report) {
      for (final ProfileCase caze : report.getCases()) {
         outputWriter.startCase(caze.getCaseClass());
         writeMissingScenarios(caze);
      }
   }

   /**
    * Writes the missing scenarios in the case.
    * @param caze the case filled up with only the missing scenarios.
    */
   private void writeMissingScenarios(ProfileCase caze) {
      for (final ProfileScenario scenario : caze.getScenarios()) {
         outputWriter.startScenario(scenario.getScenarioMethod());
         writeMissingMetrics(caze, scenario);
      }
   }

   /**
    * Writes the missing metrics in the scenario.
    * @param caze the case in which the scenario is registered.
    * @param scenario the scenario filled up with only the missing metrics.
    */
   private void writeMissingMetrics(ProfileCase caze, ProfileScenario scenario) {
      for (final ProfileMetric metric : scenario.getMetrics()) {
         final MergeMetric mergeMetric = new MergeMetric();
         mergeMetric.setCase(caze.getCaseClass());
         mergeMetric.setScenario(scenario.getScenarioMethod());
         mergeMetric.setMetric(metric.getKey());
         outputWriter.startMetric(mergeMetric);

         final MetricValue value = new MetricValue();
         value.setBuildName(buildName);
         value.setType(metric.getType());
         value.setValue(metric.getValue());
         outputWriter.writeMetricValue(value);
      }
   }
}
