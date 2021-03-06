/*******************************************************************************
 * Copyright (c) 2014, 2015 vogella GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Lars Vogel <Lars.Vogel@vogella.com> - initial API and implementation
 *     Lars Vogel <Lars.Vogel@vogella.com> - Bug 474274
 ******************************************************************************/
package org.eclipse.e4.core.internal.tests.di;

import static org.junit.Assert.fail;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.di.annotations.Execute;
import org.junit.Test;

/**
 * Tests that that no method is called, it the @Execute annotation is not
 * present and that an exception is thrown from the DI framework
 */
public class InvokeTestMissingAnnotation {

	/**
	 * Class to invoke for the test
	 */
	class TestSuperclass {
		public int saveCount = 0;

		// @Execute annotation missing on purpose
		void execute() {
			saveCount++;
		}
	}

	/**
	 * Checks that no methods is called and that an execution is thrown
	 */
	@Test
	public void testCallMethodsWithMissingAnnotation() {
		TestSuperclass editor = new TestSuperclass();
		try {
			ContextInjectionFactory.invoke(editor, Execute.class,
					EclipseContextFactory.create());
			fail("Exception should have been thrown "); //$NON-NLS-1$
		} catch (Exception e) {
			// expected
		}
	}

	/**
	 * Checks that no methods is called and that no execution is thrown if a
	 * default is provide
	 */
	@Test
	public void testCallMethodsWithMissingAnnotationNoExecution() {
		TestSuperclass editor = new TestSuperclass();
		ContextInjectionFactory.invoke(editor, Execute.class,
				EclipseContextFactory.create(), this);

	}

}
