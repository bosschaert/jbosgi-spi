/*
 * #%L
 * JBossOSGi SPI
 * %%
 * Copyright (C) 2010 - 2012 JBoss by Red Hat
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 2.1 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 *
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-2.1.html>.
 * #L%
 */
/*
 * JBoss, Home of Professional Open Source
 * Copyright 2006, JBoss Inc., and individual contributors as indicated
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
package org.jboss.osgi.spi;

import java.io.InputStream;
import java.util.jar.Manifest;

import org.jboss.shrinkwrap.api.asset.Asset;

/**
 * A simple manifest builder.
 *
 * @author thomas.diesler@jboss.com
 * @since 08-Mar-2010
 * @deprecated
 */
public class ManifestBuilder implements Asset {

    private final org.jboss.osgi.metadata.ManifestBuilder delegate = org.jboss.osgi.metadata.ManifestBuilder.newInstance();

    public static ManifestBuilder newInstance() {
        return new ManifestBuilder();
    }

    protected ManifestBuilder() {
    }

    public ManifestBuilder addManifestHeader(String key, String value) {
        delegate.addManifestHeader(key, value);
        return this;
    }

    public Manifest getManifest() {
        return delegate.getManifest();
    }

    @Override
    public InputStream openStream() {
        return delegate.openStream();
    }

    protected void append(String line) {
        delegate.append(line);
    }
}
