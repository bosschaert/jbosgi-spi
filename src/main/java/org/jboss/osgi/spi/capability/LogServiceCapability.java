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
package org.jboss.osgi.spi.capability;

import org.osgi.service.log.LogService;

/**
 * Adds the OSGi compedium LogService capability to the OSGiRuntime under test.
 *
 * It is ignored if the {@link LogService} is already registered.
 *
 * Installed bundles: org.apache.felix.log.jar, jboss-osgi-logging.jar
 *   slf4j-api.jar, slf4j-log4j12.jar and jcl-over-slf4j.jar
 *
 * @author thomas.diesler@jboss.com
 * @since 14-Sep-2009
 */
public class LogServiceCapability extends Capability {

    public LogServiceCapability() {
        super(LogService.class.getName());

        addDependency(new CompendiumCapability());

        addBundle("bundles/org.apache.felix.log.jar");
        addBundle("bundles/jboss-osgi-logging.jar");
        addBundle("bundles/slf4j-api.jar");
        addBundle("bundles/slf4j-log4j12.jar");
        addBundle("bundles/jcl-over-slf4j.jar");
    }
}