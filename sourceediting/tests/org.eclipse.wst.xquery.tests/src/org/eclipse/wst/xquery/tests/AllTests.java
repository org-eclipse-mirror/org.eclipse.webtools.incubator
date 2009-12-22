/*******************************************************************************
 * Copyright (c) 2008, 2009 28msec Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Gabriel Petrovay (28msec) - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xquery.tests;

import org.eclipse.wst.xquery.core.tests.XQDTCoreTestSuite;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("org.eclipse.wst.xquery.tests: All XQDT plugin tests");
		//$JUnit-BEGIN$
//		suite.addTest(XQDTCoreTestSuite.suite());
		suite.addTestSuite(SimpleTest.class);
		suite.addTest(XQDTCoreTestSuite.suite());
		

		//$JUnit-END$
		return suite;
	}

}
