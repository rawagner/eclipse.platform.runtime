/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.core.tests.runtime.compatibility;

import java.io.IOException;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.eclipse.core.internal.plugins.InternalPlatform;
import org.eclipse.core.runtime.IPluginDescriptor;
import org.eclipse.core.runtime.PluginVersionIdentifier;
import org.eclipse.core.tests.runtime.DynamicPluginTest;
import org.osgi.framework.*;

public class PluginCompatibilityTests extends DynamicPluginTest {

	public PluginCompatibilityTests(String name) {
		super(name);
	}

	// see bug 59013
	public void testPluginWithNoRuntimeLibrary() throws BundleException, IOException {
		Bundle installed = null;
		assertNull("0.0", getBundles("bundle01", "1.0"));
		installed = installBundle("compatibility/bundle01");
		try {
			assertEquals("1.0", "bundle01", installed.getSymbolicName());
			assertEquals("1.1", "1.0", installed.getHeaders().get(Constants.BUNDLE_VERSION));
			assertEquals("1.2", Bundle.RESOLVED, installed.getState());
			IPluginDescriptor descriptor = InternalPlatform.getPluginRegistry().getPluginDescriptor("bundle01", new PluginVersionIdentifier("1.0"));
			assertNotNull("2.0", descriptor);
			assertNotNull("2.1", descriptor.getRuntimeLibraries());
			assertEquals("2.2", 0, descriptor.getRuntimeLibraries().length);
		} finally {
			// clean-up
			installed.uninstall();
		}
	}

	public static  Test suite() {
		return new TestSuite(PluginCompatibilityTests.class);
	}
}