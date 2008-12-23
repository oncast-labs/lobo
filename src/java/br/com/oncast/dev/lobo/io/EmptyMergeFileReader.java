/* File: EmptyMergeFileReader.java
 * Date: 17/04/2007
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
package br.com.oncast.dev.lobo.io;

import java.util.NoSuchElementException;

import br.com.oncast.dev.lobo.merge.bean.MergeMetric;
import br.com.oncast.dev.lobo.merge.bean.MetricValue;

/**
 * {@link MergeFileReader} implementation to simulate an empty merge file reading.
 * @author <a href="mailto:rodrigo.machado@oncast.com.br">Rodrigo Carvalho Machado</a>.
 */
class EmptyMergeFileReader implements MergeFileReader {
   /**
    * @see br.com.oncast.dev.lobo.io.MergeFileReader#currentCaseName()
    */
   @Override
   public String currentCaseName() {
      throw new NoSuchElementException();
   }

   /**
    * @see br.com.oncast.dev.lobo.io.MergeFileReader#currentScenarioName()
    */
   @Override
   public String currentScenarioName() {
      throw new NoSuchElementException();
   }

   /**
    * @see br.com.oncast.dev.lobo.io.MergeFileReader#hasCases()
    */
   @Override
   public boolean hasCases() {
      return false;
   }

   /**
    * @see br.com.oncast.dev.lobo.io.MergeFileReader#hasMetricValues()
    */
   @Override
   public boolean hasMetricValues() {
      return false;
   }

   /**
    * @see br.com.oncast.dev.lobo.io.MergeFileReader#hasMetrics()
    */
   @Override
   public boolean hasMetrics() {
      return false;
   }

   /**
    * @see br.com.oncast.dev.lobo.io.MergeFileReader#hasScenarios()
    */
   @Override
   public boolean hasScenarios() {
      return false;
   }

   /**
    * @see br.com.oncast.dev.lobo.io.MergeFileReader#nextCase()
    */
   @Override
   public void nextCase() {
      throw new NoSuchElementException();
   }

   /**
    * @see br.com.oncast.dev.lobo.io.MergeFileReader#nextMetric()
    */
   @Override
   public MergeMetric nextMetric() {
      throw new NoSuchElementException();
   }

   /**
    * @see br.com.oncast.dev.lobo.io.MergeFileReader#nextMetricValue()
    */
   @Override
   public MetricValue nextMetricValue() {
      throw new NoSuchElementException();
   }

   /**
    * @see br.com.oncast.dev.lobo.io.MergeFileReader#nextScenario()
    */
   @Override
   public void nextScenario() {
      throw new NoSuchElementException();
   }
}
