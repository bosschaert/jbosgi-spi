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
package org.jboss.osgi.spi.internal;

//$Id$

import static org.jboss.osgi.spi.OSGiConstants.OSGI_HOME;
import static org.jboss.osgi.spi.OSGiConstants.OSGI_SERVER_HOME;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import org.jboss.logging.Logger;
import org.jboss.osgi.spi.framework.OSGiBootstrap;
import org.jboss.osgi.spi.framework.OSGiBootstrapProvider;
import org.jboss.osgi.spi.framework.PropertiesBootstrapProvider;
import org.jboss.osgi.spi.util.ServiceLoader;
import org.kohsuke.args4j.Option;
import org.osgi.framework.BundleException;
import org.osgi.framework.launch.Framework;

/**
 * An internal bean that collabrates with {@link OSGiBootstrap}.
 * 
 * @author thomas.diesler@jboss.com
 * @since 04-Nov-2008
 */
public class OSGiBootstrapBean
{
   private static Logger log;

   private static final String JAVA_PROTOCOL_HANDLERS = "java.protocol.handler.pkgs";
   private static final String JBOSS_BIND_ADDRESS = "jboss.bind.address";
   private static final String OSGI_SERVER_NAME = "osgi.server.name";

   @Option(name = "-c", aliases = { "--server-name" }, usage = "The runtime profile to start. (-c minimal)", required = false)
   public String serverName = "default";

   @Option(name = "-b", aliases = { "--bind-address" }, usage = "The network address various services can bind to (-b 127.0.0.1)", required = false)
   public String bindAddress = "localhost";

   private String osgiHome;
   private String osgiServerHome;

   public void run()
   {
      initBootstrap();

      OSGiBootstrapProvider bootProvider = getBootstrapProvider();
      Framework framework = bootProvider.getFramework();

      Runtime runtime = Runtime.getRuntime();
      runtime.addShutdownHook(new ShutdownThread(framework));

      Thread thread = new StartupThread(framework);
      thread.start();
   }

   private void initBootstrap()
   {
      osgiHome = System.getProperty(OSGI_HOME);
      if (osgiHome == null)
         throw new IllegalStateException("Cannot obtain system property: '" + OSGI_HOME + "'");

      osgiServerHome = osgiHome + "/server/" + serverName;

      // Replace the context class loader with one that contains the server conf dir 
      File serverConfDir = new File(osgiServerHome + "/conf");
      if (serverConfDir.exists())
      {
         ClassLoader ctxLoader = Thread.currentThread().getContextClassLoader();
         URLClassLoader confLoader = new URLClassLoader(new URL[] { toURL(serverConfDir) }, ctxLoader);
         Thread.currentThread().setContextClassLoader(confLoader);
      }

      // This property must be set before the logger is obtained
      System.setProperty(OSGI_SERVER_HOME, osgiServerHome);
      log = Logger.getLogger(OSGiBootstrapBean.class);

      Properties defaults = new Properties();
      defaults.setProperty(OSGI_SERVER_NAME, serverName);
      defaults.setProperty(OSGI_SERVER_HOME, osgiServerHome);
      defaults.setProperty(JBOSS_BIND_ADDRESS, bindAddress);
      defaults.setProperty(JAVA_PROTOCOL_HANDLERS, "org.jboss.net.protocol|org.jboss.virtual.protocol");

      log.debug("JBoss OSGi System Properties");
      log.debug("   " + OSGI_SERVER_HOME + "=" + osgiServerHome);

      Enumeration<?> defaultNames = defaults.propertyNames();
      while (defaultNames.hasMoreElements())
      {
         String propName = (String)defaultNames.nextElement();
         String sysValue = System.getProperty(propName);
         if (sysValue == null)
         {
            String propValue = defaults.getProperty(propName);
            System.setProperty(propName, propValue);
            log.debug("   " + propName + "=" + propValue);
         }
      }
   }

   public static OSGiBootstrapProvider getBootstrapProvider()
   {
      if (log == null)
         log = Logger.getLogger(OSGiBootstrap.class);
      
      OSGiBootstrapProvider provider = null;

      List<OSGiBootstrapProvider> providers = ServiceLoader.loadServices(OSGiBootstrapProvider.class);
      for (OSGiBootstrapProvider aux : providers)
      {
         try
         {
            aux.configure();
            provider = aux;
            break;
         }
         catch (Exception ex)
         {
            log.debug("Cannot configure [" + aux.getClass().getName() + "]", ex);
         }
      }

      if (provider == null)
      {
         provider = new PropertiesBootstrapProvider();
         log.debug("Using default: " + PropertiesBootstrapProvider.class.getName());
      }

      return provider;
   }
   
   private URL toURL(File file)
   {
      try
      {
         return file.toURI().toURL();
      }
      catch (MalformedURLException e)
      {
         throw new IllegalArgumentException("Invalid file: " + file);
      }
   }

   class StartupThread extends Thread
   {
      private Framework framework;

      public StartupThread(Framework framework)
      {
         this.framework = framework;
      }

      public void run()
      {
         // Start the framework
         long beforeStart = System.currentTimeMillis();
         try
         {
            framework.start();
         }
         catch (BundleException ex)
         {
            throw new IllegalStateException("Cannot start framework", ex);
         }
         
         float diff = (System.currentTimeMillis() - beforeStart) / 1000f;
         log.info("JBossOSGi Runtime booted in " + diff + "sec");
         
         Reader br = new InputStreamReader(System.in);
         try
         {
            int inByte = br.read();
            while (inByte != 0)
            {
               inByte = br.read();
            }
         }
         catch (IOException ioe)
         {
            // ignore user input
         }
      }
   }

   class ShutdownThread extends Thread
   {
      private Framework framework;

      public ShutdownThread(Framework framework)
      {
         this.framework = framework;
      }

      public void run()
      {
         log.info("Initiating shutdown ...");
         try
         {
            framework.stop();
         }
         catch (BundleException ex)
         {
            log.error("Cannot stop framework", ex);
         }
         log.info("Shutdown complete");
      }
   }
}