/*******************************************************************************
 * Copyright (c) 2004, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.core.tools.metadata;

import java.io.*;
import org.eclipse.core.runtime.Platform;
import org.eclipse.osgi.service.resolver.*;

public class StateDumper extends AbstractDumper {

	@Override
	protected void dumpContents(PushbackInputStream input, StringBuffer contents) throws IOException, Exception, DumpException {
		PlatformAdmin admin = Platform.getPlatformAdmin();
		// use the deprecated API to support running against a 3.0 Eclipse
		State state = admin.getFactory().readState(new DataInputStream(input));
		if (state==null){
			contents.append("Unable to read state file.");
			return;
		}
		contents.append("State resolved: "); //$NON-NLS-1$
		contents.append(state.isResolved());
		contents.append("\n"); //$NON-NLS-1$
		BundleDescription[] allBundles = state.getBundles();
		admin.getStateHelper().sortBundles(allBundles);
		for (int i = 0; i < allBundles.length; i++)
			dumpBundle(allBundles[i], contents);
	}

	private void dumpBundle(BundleDescription bundle, StringBuffer contents) {
		contents.append("\n"); //$NON-NLS-1$
		contents.append("Bundle: "); //$NON-NLS-1$
		contents.append(bundle.getSymbolicName());
		contents.append('_');
		contents.append(bundle.getVersion());
		contents.append(" ("); //$NON-NLS-1$
		contents.append(bundle.isResolved() ? "resolved" : "unresolved"); //$NON-NLS-1$ //$NON-NLS-2$
		if (bundle.isSingleton())
			contents.append(", singleton"); //$NON-NLS-1$
		contents.append(")\n"); //$NON-NLS-1$
		HostSpecification host = bundle.getHost();
		if (host != null)
			dumpHost(host, contents);
		BundleSpecification[] required = bundle.getRequiredBundles();
		for (int i = 0; i < required.length; i++)
			dumpRequired(required[i], contents);
	}

	private void dumpRequired(BundleSpecification required, StringBuffer contents) {
		contents.append("\tRequired: "); //$NON-NLS-1$
		contents.append(required.getName());
		contents.append(" - Version: "); //$NON-NLS-1$
		contents.append(required.getVersionRange());
		contents.append(" ("); //$NON-NLS-1$
		contents.append(required.isResolved() ? ("actual: " + required.getSupplier().getVersion().toString()) : "unresolved"); //$NON-NLS-1$ //$NON-NLS-2$
		if (required.isOptional())
			contents.append(", optional"); //$NON-NLS-1$
		contents.append(')');
		contents.append('\n');
	}

	private void dumpHost(HostSpecification host, StringBuffer contents) {
		contents.append("\tHost: "); //$NON-NLS-1$
		contents.append(host.getName());
		contents.append(" - Version: "); //$NON-NLS-1$
		contents.append(host.getVersionRange());
		contents.append(" ("); //$NON-NLS-1$
		contents.append(host.isResolved() ? ("actual: " + host.getSupplier().getVersion().toString()) : "unresolved"); //$NON-NLS-1$ //$NON-NLS-2$
		contents.append(')');
		contents.append('\n');
	}
}
