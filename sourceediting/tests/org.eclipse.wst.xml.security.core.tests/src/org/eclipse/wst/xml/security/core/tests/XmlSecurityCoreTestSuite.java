/*******************************************************************************
 * Copyright (c) 2009 Dominik Schadow - http://www.xml-sicherheit.de All rights reserved. This
 * program and the accompanying materials are made available under the terms of the Eclipse Public
 * License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Dominik Schadow - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xml.security.core.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.wst.xml.security.core.tests.sign.CreateSignatureTest;
import org.eclipse.wst.xml.security.core.tests.utils.CertificateTest;
import org.eclipse.wst.xml.security.core.tests.utils.KeystoreTest;
import org.eclipse.wst.xml.security.core.tests.utils.UtilsTest;
import org.eclipse.wst.xml.security.core.utils.XmlSecurityConstants;

public class XmlSecurityCoreTestSuite extends TestSuite {
    public static Test suite() {
        return new XmlSecurityCoreTestSuite();
    }

    public XmlSecurityCoreTestSuite() {
        super("XML Security Core Tests");
        addTestSuite(CreateSignatureTest.class);
        addTestSuite(CertificateTest.class);
        addTestSuite(KeystoreTest.class);
        addTestSuite(UtilsTest.class);
        addTestSuite(XmlSecurityConstants.class);
    }
}

