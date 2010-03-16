/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.osgi.testing;

import java.io.File;
import java.net.URL;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.logging.Logger;
import org.jboss.osgi.vfs.VirtualFile;
import org.junit.After;
import org.junit.Before;
import org.osgi.framework.Bundle;

/**
 * An abstract OSGi Test.
 * 
 * A convenience wrapper for the functionality provided by {@link OSGiTestHelper}. 
 * 
 * @author Thomas.Diesler@jboss.org
 * @since 25-Sep-2008
 */
public abstract class OSGiTest 
{
   // Provide logging
   private static final Logger log = Logger.getLogger(OSGiTest.class);

   private OSGiTestHelper helper;

   /**
    * Get the test helper used by this test
    * 
    * Overwrite if you need to supply another helper
    * i.e. one that you have statically setup 
    */
   protected OSGiTestHelper getTestHelper()
   {
      if (helper == null)
         helper = new OSGiTestHelper();
         
      return helper;
   }

   /**
    * Writes a a debug start messge
    */
   @Before
   public void setUp() throws Exception
   {
      log.debug("### START " + getLongName());
   }

   /**
    * Writes a a debug stop messge
    */
   @After
   public void tearDown() throws Exception
   {
      log.debug("### END " + getLongName());
   }

   /**
    * Get the last token in the FQN of this test class. 
    */
   protected String getShortName()
   {
      String shortName = getClass().getName();
      shortName = shortName.substring(shortName.lastIndexOf(".") + 1);
      return shortName;
   }

   /**
    * Get the the FQN of this test class. 
    */
   protected String getLongName()
   {
      return getClass().getName();
   }

   /**
    * Delegates to {@link OSGiTestHelper#getResourceURL(String)}
    */
   protected URL getResourceURL(String resource)
   {
      return getTestHelper().getResourceURL(resource);
   }

   /**
    * Delegates to {@link OSGiTestHelper#getResourceFile(String)}
    */
   protected File getResourceFile(String resource)
   {
      return getTestHelper().getResourceFile(resource);
   }

   /**
    * Delegates to {@link OSGiTestHelper#getTestArchiveURL(String)}
    */
   protected URL getTestArchiveURL(String archive)
   {
      return getTestHelper().getTestArchiveURL(archive);
   }

   /**
    * Delegates to {@link OSGiTestHelper#getTestArchivePath(String)}
    */
   protected String getTestArchivePath(String archive)
   {
      return getTestHelper().getTestArchivePath(archive);
   }

   /**
    * Delegates to {@link OSGiTestHelper#getTestArchiveFile(String)}
    */
   protected File getTestArchiveFile(String archive)
   {
      return getTestHelper().getTestArchiveFile(archive);
   }
   
   /**
    * Delegates to {@link OSGiTestHelper#getInitialContext()}
    */
   public InitialContext getInitialContext() throws NamingException
   {
      return getTestHelper().getInitialContext();
   }

   /**
    * Delegates to {@link OSGiTestHelper#getServerHost()}
    */
   public String getServerHost()
   {
      return getTestHelper().getServerHost();
   }
   
   /**
    * Delegates to {@link OSGiTestHelper#getTargetContainer()}
    */
   public String getTargetContainer()
   {
      return getTestHelper().getTargetContainer();
   }
   
   /**
    * Delegates to {@link OSGiTestHelper#getFrameworkName()}
    */
   public String getFrameworkName()
   {
      return getTestHelper().getFrameworkName();
   }

   /**
    * Delegates to {@link OSGiTestHelper#assembleArchive(String, String, Class...)}
    */
   public VirtualFile assembleArchive(String name, String resource, Class<?>... packages) throws Exception
   {
      return getTestHelper().assembleArchive(name, resource, packages);
   }

   /**
    * Delegates to {@link OSGiTestHelper#assembleArchive(String, String[], Class...)}
    */
   public VirtualFile assembleArchive(String name, String[] resources, Class<?>... packages) throws Exception
   {
      return getTestHelper().assembleArchive(name, resources, packages);
   }

   /**
    * Delegates to {@link OSGiTestHelper#assertBundleState(int, int)}
    */
   public void assertBundleState(int expState, int wasState)
   {
      getTestHelper().assertBundleState(expState, wasState);
   }

   /**
    * Delegates to {@link OSGiTestHelper#assertLoadClass(Bundle, String)}
    */
   public Class<?> assertLoadClass(Bundle bundle, String className)
   {
      return getTestHelper().assertLoadClass(bundle, className);
   }
   
   /**
    * Delegates to {@link OSGiTestHelper#assertLoadClassFail(Bundle, String)}
    */
   public void assertLoadClassFail(Bundle bundle, String className)
   {
      getTestHelper().assertLoadClassFail(bundle, className);
   }
}
