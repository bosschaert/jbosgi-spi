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
package org.jboss.osgi.spi.util;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Properties;

/**
 * Priviledged actions for the package
 *
 * @author Scott.Stark@jboss.org
 * @author <a href="david@redhat.com">David Bosschaert</a>
 */
public class SysPropertyActions {

    interface SysProps {

        SysProps NON_PRIVILEDGED = new SysProps() {
            public String getProperty(final String name, final String defaultValue) {
                return System.getProperty(name, defaultValue);
            }

            public Properties getProperties() {
                return System.getProperties();
            }
        };

        SysProps PRIVILEDGED = new SysProps() {
            public String getProperty(final String name, final String defaultValue) {
                PrivilegedAction<String> action = new PrivilegedAction<String>() {

                    public String run() {
                        return System.getProperty(name, defaultValue);
                    }
                };
                return AccessController.doPrivileged(action);
            }

            public Properties getProperties() {
                PrivilegedAction<Properties> action = new PrivilegedAction<Properties>() {
                    @Override
                    public Properties run() {
                        return System.getProperties();
                    }
                };
                return AccessController.doPrivileged(action);
            }
        };

        String getProperty(String name, String defaultValue);

        Properties getProperties();
    }

    public static String getProperty(String name, String defaultValue) {
        String prop;
        if (System.getSecurityManager() == null)
            prop = SysProps.NON_PRIVILEDGED.getProperty(name, defaultValue);
        else
            prop = SysProps.PRIVILEDGED.getProperty(name, defaultValue);
        return prop;
    }

    public static Properties getProperties() {
        if (System.getSecurityManager() == null)
            return SysProps.NON_PRIVILEDGED.getProperties();
        else
            return SysProps.PRIVILEDGED.getProperties();
    }
}
