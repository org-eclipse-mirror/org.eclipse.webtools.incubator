/*******************************************************************************
 * Copyright (c) 2008 Dominik Schadow - http://www.xml-sicherheit.de
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Dominik Schadow - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xml.security.core.tests.utils;

import java.io.File;
import java.security.PrivateKey;

import javax.crypto.SecretKey;

import junit.framework.TestCase;

import org.eclipse.wst.xml.security.core.utils.Globals;
import org.eclipse.wst.xml.security.core.utils.Keystore;
import org.eclipse.wst.xml.security.core.utils.XmlSecurityCertificate;

/**
 * <p>JUnit tests for {@link org.eclipse.wst.xml.security.core.utils.Keystore}. Uses the existing
 * sample keystore (<i>resources/sample_keystore.jks</i>) for loading operations and creates a
 * temporary keystore (<i>resources/temp_keystore.jks</i>) for creating operations. The sample
 * keystore is not changed in these tests.</p>
 *
 * <p>The sample keystore was generated with the following command (single line):<br/>
 * <code>keytool -genkey -alias sampleKey -keypass sampleKey -keystore sample_keystore.jks
 * -storepass sampleKeystore</code></p>
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public class KeystoreTest extends TestCase {
    private XmlSecurityCertificate certificate = new XmlSecurityCertificate();
    private Keystore keystore = null;
    private Keystore tempKeystore = null;
    private static final String TEMP_KEYSTORE_PATH = "resources/temp_keystore.jks";
    private static final String KEYSTORE_PATH = "resources/sample_keystore.jks";
    private static final String KEYSTORE_PASSWORD = "sampleKeystore";
    private static final String KEY_ALIAS = "sampleKey";
    private static final String KEY_PASSWORD = "sampleKey";

    /**
     * Set up method. Sets up the sample keystore used in these test cases.
     * Deletes the temporary keystore file created for these tests.
     *
     * @throws Exception during loading the sample keystore
     */
    public void setUp() throws Exception {
        File tempFile = new File(TEMP_KEYSTORE_PATH);
        if (tempFile.exists()) {
            assertTrue(tempFile.delete());
        }

        keystore = new Keystore(KEYSTORE_PATH, KEYSTORE_PASSWORD, Globals.KEYSTORE_TYPE);
        tempKeystore = new Keystore(TEMP_KEYSTORE_PATH, KEYSTORE_PASSWORD, Globals.KEYSTORE_TYPE);
    }

    /**
     * Test method for {@link org.eclipse.wst.xml.security.core.utils.Keystore#Keystore(java.lang.String, java.lang.String, java.lang.String)}.
     */
    public void testKeystore() {
        assertNotNull(keystore);
        assertNotNull(tempKeystore);
    }

    /**
     * Test method for {@link org.eclipse.wst.xml.security.core.utils.Keystore#store()}.
     */
    public void testStore() {
        try {
            File tempFile = new File(TEMP_KEYSTORE_PATH);

            assertFalse(tempFile.exists());

            assertNotNull(tempKeystore);
            tempKeystore.store();

            assertTrue(tempFile.exists());
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    /**
     * Test method for {@link org.eclipse.wst.xml.security.core.utils.Keystore#load()}.
     */
    public void testLoad() {
        try {
            assertNotNull(keystore);
            assertTrue(keystore.load());

            Keystore temp = new Keystore(KEYSTORE_PATH, "wrong", Globals.KEYSTORE_TYPE);
            assertNotNull(temp);
            assertFalse(temp.load());

            assertNotNull(tempKeystore);
            tempKeystore.store();
            assertTrue(tempKeystore.load());

            temp = new Keystore(TEMP_KEYSTORE_PATH, "wrong", Globals.KEYSTORE_TYPE);
            assertNotNull(temp);
            assertFalse(temp.load());
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    /**
     * Test method for {@link org.eclipse.wst.xml.security.core.utils.Keystore#generateCertificate(java.lang.String, java.security.cert.Certificate)}.
     */
    public void testGenerateCertificate() {
        try {
            assertNotNull(tempKeystore);
            tempKeystore.store();
            tempKeystore.load();

            assertTrue(tempKeystore.generateCertificate(KEY_ALIAS, certificate));

            tempKeystore.store();

            assertTrue(tempKeystore.containsKey(KEY_ALIAS));
            assertFalse(tempKeystore.containsKey("wrong"));

            assertNotNull(tempKeystore.getCertificate(KEY_ALIAS));
            assertNull(tempKeystore.getCertificate("wrong"));
        } catch (Exception ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        }
    }

    /**
     * Test method for {@link org.eclipse.wst.xml.security.core.utils.Keystore#generateSecretKey(java.lang.String, java.lang.Integer)}.
     */
    public void testGenerateSecretKey() {
        try {
            assertNotNull(tempKeystore);
            tempKeystore.store();

            assertNotNull(tempKeystore.generateSecretKey("AES", 128));
            assertNotNull(tempKeystore.generateSecretKey("AES", 192));
            assertNotNull(tempKeystore.generateSecretKey("AES", 256));

            assertNotNull(tempKeystore.generateSecretKey("Blowfish", 64));
            assertNotNull(tempKeystore.generateSecretKey("Blowfish", 128));
            assertNotNull(tempKeystore.generateSecretKey("Blowfish", 192));
            assertNotNull(tempKeystore.generateSecretKey("Blowfish", 256));
            assertNotNull(tempKeystore.generateSecretKey("Blowfish", 320));
            assertNotNull(tempKeystore.generateSecretKey("Blowfish", 384));
            assertNotNull(tempKeystore.generateSecretKey("Blowfish", 448));

            assertNotNull(tempKeystore.generateSecretKey("DES", 56));

            assertNotNull(tempKeystore.generateSecretKey("DESede", 112));
            assertNotNull(tempKeystore.generateSecretKey("DESede", 168));
        } catch (Exception ex) {
            fail(ex.getMessage());
        }

    }

    /**
     * Test method for {@link org.eclipse.wst.xml.security.core.utils.Keystore#insertSecretKey(java.lang.String, char, javax.crypto.SecretKey)}.
     */
    public void testInsertSecretKey() {
        try {
            assertNotNull(tempKeystore);
            tempKeystore.store();

            SecretKey des = tempKeystore.generateSecretKey("DES", 56);
            assertTrue(tempKeystore.insertSecretKey(KEY_ALIAS, KEY_PASSWORD.toCharArray(), des));

            tempKeystore.store();

            SecretKey key = tempKeystore.getSecretKey(KEY_ALIAS, KEY_PASSWORD.toCharArray());
            assertNotNull(key);

            assertEquals("DES", key.getAlgorithm());
            assertEquals("RAW", key.getFormat());

            key = tempKeystore.getSecretKey(KEY_ALIAS, "wrong".toCharArray());
            assertNull(key);

            SecretKey aes = tempKeystore.generateSecretKey("AES", 256);
            assertTrue(tempKeystore.insertSecretKey(KEY_ALIAS + "2", KEY_PASSWORD.toCharArray(), aes));

            tempKeystore.store();

            key = tempKeystore.getSecretKey(KEY_ALIAS + "2", KEY_PASSWORD.toCharArray());
            assertNotNull(key);

            assertEquals("AES", key.getAlgorithm());
            assertEquals("RAW", key.getFormat());

            key = tempKeystore.getSecretKey(KEY_ALIAS + "2", "wrong".toCharArray());
            assertNull(key);
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    /**
     * Test method for {@link org.eclipse.wst.xml.security.core.utils.Keystore#getPrivateKey(java.lang.String, char[])}.
     */
    public void testGetPrivateKey() {
        try {
            assertNotNull(keystore);
            keystore.load();

            PrivateKey key = keystore.getPrivateKey(KEY_ALIAS, KEY_PASSWORD.toCharArray());
            assertNotNull(key);

            assertEquals("DSA", key.getAlgorithm());
            assertEquals("PKCS#8", key.getFormat());

            assertNotNull(tempKeystore);
            tempKeystore.store();

            key = tempKeystore.getPrivateKey(KEY_ALIAS, KEY_PASSWORD.toCharArray());
            assertNull(key);
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    /**
     * Test method for {@link org.eclipse.wst.xml.security.core.utils.Keystore#containsKey(java.lang.String)}.
     */
    public void testContainsKey() {
        try {
            assertNotNull(keystore);
            keystore.load();

            assertTrue(keystore.containsKey(KEY_ALIAS));
            assertFalse(keystore.containsKey("wrong"));

            assertNotNull(tempKeystore);
            tempKeystore.store();

            assertFalse(tempKeystore.containsKey(KEY_ALIAS));
            assertFalse(tempKeystore.containsKey("wrong"));
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    /**
     * Test method for {@link org.eclipse.wst.xml.security.core.utils.Keystore#getCertificate(java.lang.String)}.
     */
    public void testGetCertificate() {
        try {
            assertNotNull(keystore);
            keystore.load();

            assertNotNull(keystore.getCertificate(KEY_ALIAS));
            assertNull(keystore.getCertificate("wrong"));

            assertNotNull(tempKeystore);
            tempKeystore.store();

            assertNull(tempKeystore.getCertificate(KEY_ALIAS));
            assertNull(tempKeystore.getCertificate("wrong"));
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }
}
