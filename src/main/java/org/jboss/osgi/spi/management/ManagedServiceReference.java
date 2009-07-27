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
package org.jboss.osgi.spi.management;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import org.osgi.framework.ServiceReference;

/**
 * The management view of an OSGi {@link ServiceReference}
 * 
 * @author Thomas.Diesler@jboss.org
 * @since 25-Sep-2008
 */
public class ManagedServiceReference implements Serializable
{
   private static final long serialVersionUID = 1L;
   
   private Map<String, Object> props;
   
   ManagedServiceReference(Map<String, Object> props)
   {
      this.props = props;
   }

   /**
    * Returns the property value to which the specified property key is mapped 
    * in the properties Dictionary object of the service referenced by this 
    * ServiceReference object.
    */
   public Object getProperty(String key)
   {
      return props.get(key);
   }

   /**
    * Returns an array of the keys in the properties Dictionary 
    * object of the service referenced by this ServiceReference  object. 
    */
   public String[] getPropertyKeys()
   {
      Set<String> keySet = props.keySet();
      return keySet.toArray(new String[keySet.size()]);
   }
}