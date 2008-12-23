/* File: FilesetClassLoader.java
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
package br.com.oncast.dev.lobo.task.loader;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.FileSet;

/**
 * {@link ClassLoader} for ant {@link FileSet}s.<br>
 * Extends {@link ClassLoader} behavior by keeping track of all classes loaded by the {@link ClassLoader}.
 */
public class FilesetClassLoader extends ClassLoader {
   /** The list with all loaded classes. */
   private final List<Class<?>> loadedClasses = new ArrayList<Class<?>>();

   /** The build project running. */
   private final Project project;

   /**
    * Creates a new instance of FilesetClassLoader.
    * @param filesets the list of {@link FileSet}s that point to classes to be loaded.
    * @param project the build project running.
    */
   public FilesetClassLoader(List<FileSet> filesets, Project project) {
      super();
      this.project = project;
      init(filesets);
   }

   /**
    * Creates a new instance of FilesetClassLoader.
    * @param filesets the list of {@link FileSet}s that point to classes to be loaded.
    * @param parent the parent {@link ClassLoader}.
    * @param project the build project running.
    */
   public FilesetClassLoader(List<FileSet> filesets, ClassLoader parent, Project project) {
      super(parent);
      this.project = project;
      init(filesets);
   }

   /**
    * Initiates the {@link ClassLoader} by reading the given <code>filesets</code>.
    * @param filesets a list of all {@link FileSet}s source of classes.
    */
   private void init(List<FileSet> filesets) {
      final int tailLength = ".class".length();

      for (final FileSet fileset : filesets) {
         final DirectoryScanner ds = fileset.getDirectoryScanner(project);
         final File baseDir = ds.getBasedir();
         final String[] files = ds.getIncludedFiles();
         for (final String file : files) {
            final File classFile = new File(baseDir, file);
            final String className = file.replace('/', '.').replace('\\', '.').substring(0, file.length() - tailLength);
            final byte[] bytes;
            try {
               bytes = loadBytes(classFile);
            } catch (IOException e) {
               // TODO Handle error.
               continue;
            }
            loadedClasses.add(defineClass(className, bytes, 0, bytes.length));
         }
      }

      for (Class<?> clazz : loadedClasses) {
         resolveClass(clazz);
      }
   }

   /**
    * Loads the bytes from a file to a byte array.
    * @param file the file.
    * @return the byte array.
    * @throws IOException
    */
   private byte[] loadBytes(File file) throws IOException {
      ByteArrayOutputStream baos = null;
      FileInputStream fis = null;
      BufferedInputStream bis = null;

      try {
         baos = new ByteArrayOutputStream();
         fis = new FileInputStream(file);
         bis = new BufferedInputStream(fis);

         for (int data = bis.read(); data != -1; data = bis.read()) {
            baos.write(data);
         }

         return baos.toByteArray();
      } finally {
         if (baos != null) baos.close();
         if (bis != null) bis.close();
         if (fis != null) fis.close();
      }
   }

   /** @return the loadedClasses. */
   public List<Class<?>> getLoadedClasses() {
      return loadedClasses;
   }
}
