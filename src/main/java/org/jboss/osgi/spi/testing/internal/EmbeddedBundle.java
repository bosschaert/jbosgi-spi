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
package org.jboss.osgi.spi.testing.internal;

import java.util.Dictionary;

import org.jboss.osgi.spi.logging.ExportedPackageHelper;
import org.jboss.osgi.spi.testing.OSGiBundle;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;

/**
 * An OSGi Test Case
 * 
 * @author Thomas.Diesler@jboss.org
 * @since 25-Sep-2008
 */
public class EmbeddedBundle extends OSGiBundle
{
   private OSGiRuntimeImpl runtime;
   private Bundle bundle;

   public EmbeddedBundle(OSGiRuntimeImpl runtime, Bundle bundle)
   {
      this.runtime = runtime;
      this.bundle = bundle;
   }

   public Bundle getBundle()
   {
      return bundle;
   }

   @Override
   public int getState()
   {
      return bundle.getState();
   }

   @Override
   public String getSymbolicName()
   {
      return bundle.getSymbolicName();
   }

   @Override
   public String getVersion()
   {
      return getHeaders().get(Constants.BUNDLE_VERSION);
   }

   @Override
   @SuppressWarnings("unchecked")
   public Dictionary<String, String> getHeaders()
   {
      return bundle.getHeaders();
   }

   @Override
   public long getBundleId()
   {
      return bundle.getBundleId();
   }

   @Override
   public String getProperty(String key)
   {
      return bundle.getBundleContext().getProperty(key);
   }

   @Override
   public void start() throws BundleException
   {
      bundle.start();
      
      ExportedPackageHelper helper = new ExportedPackageHelper(bundle.getBundleContext());
      helper.logExportedPackages(bundle);
   }

   @Override
   public void stop() throws BundleException
   {
      bundle.stop();
   }

   @Override
   public void uninstall() throws BundleException
   {
      bundle.uninstall();
      runtime.unregisterBundle(this);
   }
}