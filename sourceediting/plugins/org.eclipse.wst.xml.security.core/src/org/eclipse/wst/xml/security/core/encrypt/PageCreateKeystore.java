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
package org.eclipse.wst.xml.security.core.encrypt;

import java.io.File;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.xml.security.core.utils.Algorithms;
import org.eclipse.wst.xml.security.core.utils.Globals;
import org.eclipse.wst.xml.security.core.utils.Keystore;
import org.eclipse.wst.xml.security.core.utils.XmlSecurityImageRegistry;

/**
 * <p>Third alternative page of the Encryption Wizard. Lets the user create a new <i>Key</i> and
 * inserts the generated private key in a new <i>Java KeyStore</i> (type <i>JCEKS</i>).</p>
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public class PageCreateKeystore extends WizardPage implements Listener {
    /** Wizard page name. */
    public static final String PAGE_NAME = "EncryptPageCreateKeystore"; //$NON-NLS-1$
    /** KeyStore. */
    private String keystore;
    /** KeyStore name. */
    private String keyStoreName;
    /** Name of the opened project. */
    private String name;
    /** Path of the opened project. */
    private String project;
    /** KeyStore and key generation successful. */
    private boolean generatedKeyStore = false;
    /** Create KeyStore button. */
    private Button bCreateKeyStore = null;
    /** Button <i>Echo Key Password</i>. */
    private Button bEchoKeyPassword = null;
    /** Button <i>Echo KeyStore Password</i>. */
    private Button bEchoKeyStorePassword = null;
    /** Combo box <i>Key Algorithm</i>. */
    private Combo cKeyAlgorithm = null;
    /** Combo box <i>Key Algorithm Size</i>. */
    private Combo cKeyAlgorithmSize = null;
    /** Key alias name. */
    private Text tKeyName = null;
    /** Key password. */
    private Text tKeyPassword = null;
    /** KeyStore name. */
    private Text tKeyStoreName = null;
    /** KeyStore password. */
    private Text tKeyStorePassword = null;
    /** KeyStore and key generation result label. */
    private Label lResult = null;
    /** Default label width. */
    private static final int LABELWIDTH = 120;
    /** Default preview textfield height. */
    private static final int TEXTHEIGHT = 40;
    /** Model for the XML Encryption Wizard. */
    private Encryption encryption = null;
    /** The KeyStore containing all required key information. */
    private Keystore keyStore = null;

    /**
     * Constructor for PageCreateKeystore.
     *
     * @param encryption The encryption wizard model
     * @param project The path of the opened project
     * @param name The name of the opened project
     */
    public PageCreateKeystore(final Encryption encryption, final String project, final String name) {
        super(PAGE_NAME);
        setTitle(Messages.encryptionTitle);
        setDescription(Messages.createKeystoreDescription);

        this.encryption = encryption;
        this.project = project;
        this.name = name;
    }

    /**
     * Creates the wizard page with the layout settings.
     *
     * @param parent Parent composite
     */
    public void createControl(final Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);
        FormLayout formLayout = new FormLayout();
        container.setLayout(formLayout);

        createPageContent(container);
        addListeners();
        setControl(container);
        setPageComplete(false);

        PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(), "org.eclipse.wst.xml.security.doc.encryptcreatekeystore");
    }

    /**
     * Fills this wizard page with content. Two groups (<i>Key</i> and <i>Keystore</i>) and all
     * their widgets are inserted.
     *
     * @param parent Parent composite
     */
    private void createPageContent(final Composite parent) {
        FormLayout layout = new FormLayout();
        layout.marginTop = Globals.MARGIN / 2;
        layout.marginBottom = Globals.MARGIN / 2;
        layout.marginLeft = Globals.MARGIN / 2;
        layout.marginRight = Globals.MARGIN / 2;
        parent.setLayout(layout);

        // Two groups
        Group gKey = new Group(parent, SWT.SHADOW_ETCHED_IN);
        gKey.setLayout(layout);
        gKey.setText(Messages.key);
        FormData data = new FormData();
        data.top = new FormAttachment(0, Globals.MARGIN, SWT.DEFAULT);
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(Globals.GROUP_NUMERATOR);
        gKey.setLayoutData(data);

        Group gKeyStore = new Group(parent, SWT.SHADOW_ETCHED_IN);
        gKeyStore.setLayout(layout);
        gKeyStore.setText(Messages.keyStore);
        data = new FormData();
        data.top = new FormAttachment(gKey, Globals.MARGIN, SWT.DEFAULT);
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(Globals.GROUP_NUMERATOR);
        gKeyStore.setLayoutData(data);

        // Elements for group "Key"
        Label lKeyAlgorithm = new Label(gKey, SWT.SHADOW_IN);
        lKeyAlgorithm.setText(Messages.keyAlgorithm);
        data = new FormData();
        data.width = LABELWIDTH;
        data.top = new FormAttachment(gKey);
        data.left = new FormAttachment(gKey);
        lKeyAlgorithm.setLayoutData(data);

        cKeyAlgorithm = new Combo(gKey, SWT.READ_ONLY);
        cKeyAlgorithm.setItems(Algorithms.KEY_FILE_ALOGRITHMS);
        cKeyAlgorithm.setText(Algorithms.KEY_FILE_ALOGRITHMS[0]);
        data = new FormData();
        data.top = new FormAttachment(lKeyAlgorithm, 0, SWT.CENTER);
        data.left = new FormAttachment(lKeyAlgorithm);
        data.width = Globals.COMBO_WIDTH;
        cKeyAlgorithm.setLayoutData(data);

        Label lKeyAlgorithmSize = new Label(gKey, SWT.SHADOW_IN);
        lKeyAlgorithmSize.setText(Messages.keyAlgorithmSize);
        data = new FormData();
        data.width = LABELWIDTH;
        data.top = new FormAttachment(lKeyAlgorithm, Globals.MARGIN);
        data.left = new FormAttachment(gKey);
        lKeyAlgorithmSize.setLayoutData(data);

        cKeyAlgorithmSize = new Combo(gKey, SWT.READ_ONLY);
        cKeyAlgorithmSize.setItems(Algorithms.KEY_SIZES_AES);
        cKeyAlgorithmSize.setText(Algorithms.KEY_SIZES_AES[0]);
        data = new FormData();
        data.top = new FormAttachment(lKeyAlgorithmSize, 0, SWT.CENTER);
        data.left = new FormAttachment(lKeyAlgorithmSize);
        data.width = Globals.COMBO_WIDTH;
        cKeyAlgorithmSize.setLayoutData(data);

        Label lKeyAlias = new Label(gKey, SWT.SHADOW_IN);
        lKeyAlias.setText(Messages.name);
        data = new FormData();
        data.width = LABELWIDTH;
        data.top = new FormAttachment(lKeyAlgorithmSize, Globals.MARGIN);
        data.left = new FormAttachment(gKey);
        lKeyAlias.setLayoutData(data);

        tKeyName = new Text(gKey, SWT.SINGLE);
        tKeyName.setTextLimit(Globals.KEY_ALIAS_MAX_SIZE);
        data = new FormData();
        data.width = Globals.SHORT_TEXT_WIDTH;
        data.top = new FormAttachment(lKeyAlias, 0, SWT.CENTER);
        data.left = new FormAttachment(lKeyAlias);
        tKeyName.setLayoutData(data);

        Label lKeyPassword = new Label(gKey, SWT.SHADOW_IN);
        lKeyPassword.setText(Messages.password);
        data = new FormData();
        data.width = LABELWIDTH;
        data.top = new FormAttachment(lKeyAlias, Globals.MARGIN);
        data.left = new FormAttachment(gKey);
        lKeyPassword.setLayoutData(data);

        tKeyPassword = new Text(gKey, SWT.SINGLE);
        tKeyPassword.setTextLimit(Globals.KEYSTORE_PASSWORD_MAX_SIZE);
        data = new FormData();
        data.width = Globals.SHORT_TEXT_WIDTH;
        data.top = new FormAttachment(lKeyPassword, 0, SWT.CENTER);
        data.left = new FormAttachment(lKeyPassword);
        tKeyPassword.setEchoChar('*');
        tKeyPassword.setLayoutData(data);

        bEchoKeyPassword = new Button(gKey, SWT.PUSH);
        bEchoKeyPassword.setImage(XmlSecurityImageRegistry.getImageRegistry().get("echo_password"));
        bEchoKeyPassword.setToolTipText(Messages.echoPassword);
        data = new FormData();
        data.top = new FormAttachment(tKeyPassword, 0, SWT.CENTER);
        data.left = new FormAttachment(tKeyPassword, Globals.MARGIN);
        bEchoKeyPassword.setLayoutData(data);

        // Elements for group "KeyStore"
        Label lKeyStoreName = new Label(gKeyStore, SWT.SHADOW_IN);
        lKeyStoreName.setText(Messages.name);
        data = new FormData();
        data.width = LABELWIDTH;
        data.top = new FormAttachment(gKeyStore, Globals.MARGIN);
        data.left = new FormAttachment(gKeyStore);
        lKeyStoreName.setLayoutData(data);

        tKeyStoreName = new Text(gKeyStore, SWT.SINGLE);
        data = new FormData();
        data.width = Globals.SHORT_TEXT_WIDTH;
        data.top = new FormAttachment(lKeyStoreName, 0, SWT.CENTER);
        data.left = new FormAttachment(lKeyStoreName);
        tKeyStoreName.setLayoutData(data);

        Label lKeyStorePassword = new Label(gKeyStore, SWT.SHADOW_IN);
        lKeyStorePassword.setText(Messages.password);
        data = new FormData();
        data.width = LABELWIDTH;
        data.top = new FormAttachment(lKeyStoreName, Globals.MARGIN);
        data.left = new FormAttachment(gKeyStore);
        lKeyStorePassword.setLayoutData(data);

        tKeyStorePassword = new Text(gKeyStore, SWT.SINGLE);
        tKeyStorePassword.setTextLimit(Globals.KEYSTORE_PASSWORD_MAX_SIZE);
        data = new FormData();
        data.width = Globals.SHORT_TEXT_WIDTH;
        data.top = new FormAttachment(lKeyStorePassword, 0, SWT.CENTER);
        data.left = new FormAttachment(lKeyStorePassword);
        tKeyStorePassword.setEchoChar('*');
        tKeyStorePassword.setLayoutData(data);

        bEchoKeyStorePassword = new Button(gKeyStore, SWT.PUSH);
        bEchoKeyStorePassword.setImage(XmlSecurityImageRegistry.getImageRegistry().get("echo_password"));
        bEchoKeyStorePassword.setToolTipText(Messages.echoPassword);
        data = new FormData();
        data.top = new FormAttachment(tKeyStorePassword, 0, SWT.CENTER);
        data.left = new FormAttachment(tKeyStorePassword, Globals.MARGIN);
        bEchoKeyStorePassword.setLayoutData(data);

        bCreateKeyStore = new Button(gKeyStore, SWT.PUSH);
        bCreateKeyStore.setText(Messages.createKeystoreButton);
        bCreateKeyStore.setEnabled(false);
        data = new FormData();
        data.top = new FormAttachment(lKeyStorePassword, Globals.MARGIN * 2);
        data.left = new FormAttachment(gKeyStore);
        bCreateKeyStore.setLayoutData(data);

        lResult = new Label(gKeyStore, SWT.WRAP);
        data = new FormData();
        data.height = TEXTHEIGHT;
        data.top = new FormAttachment(bCreateKeyStore, Globals.MARGIN * 2);
        data.left = new FormAttachment(gKeyStore);
        data.right = new FormAttachment(Globals.GROUP_NUMERATOR);
        lResult.setLayoutData(data);
    }

    /**
     * Adds all listeners for the current wizard page.
     */
    private void addListeners() {
        bCreateKeyStore.addListener(SWT.MouseDown, this);
        bEchoKeyPassword.addListener(SWT.Selection, this);
        bEchoKeyStorePassword.addListener(SWT.Selection, this);
        cKeyAlgorithm.addListener(SWT.Selection, this);
        cKeyAlgorithm.addModifyListener(new ModifyListener() {
            public void modifyText(final ModifyEvent e) {
                dialogChanged();
            }
        });
        cKeyAlgorithmSize.addModifyListener(new ModifyListener() {
            public void modifyText(final ModifyEvent e) {
                dialogChanged();
            }
        });
        tKeyName.addModifyListener(new ModifyListener() {
            public void modifyText(final ModifyEvent e) {
                dialogChanged();
            }
        });
        tKeyStoreName.addModifyListener(new ModifyListener() {
            public void modifyText(final ModifyEvent e) {
                dialogChanged();
            }
        });
        tKeyStorePassword.addModifyListener(new ModifyListener() {
            public void modifyText(final ModifyEvent e) {
                dialogChanged();
            }
        });
    }

    /**
     * Determines the (error) message for the missing field.
     */
    private void dialogChanged() {
        if (tKeyName.getText().length() < Globals.KEY_ALIAS_MIN_SIZE) {
            updateStatus(Messages.enterNewKeyAlias);
            return;
        }
        if (tKeyStoreName.getText().length() > 0) {
            keyStoreName = tKeyStoreName.getText() + Globals.KEYSTORE_EXTENSION;
            keystore = project + System.getProperty("file.separator") + keyStoreName; //$NON-NLS-1$

            File keyFile = new File(keystore);
            if (keyFile.exists()) {
                updateStatus(Messages.keyStoreAlreadyExists);
                return;
            }
        } else {
            updateStatus(Messages.enterNewKeyStoreName);
            return;
        }
        if (tKeyStorePassword.getText().length() < Globals.KEYSTORE_PASSWORD_MIN_SIZE) {
            updateStatus(Messages.enterNewKeyStorePassword);
            return;
        }
        setErrorMessage(null);
        updateStatus(null);
    }

    /**
     * Shows a message to the user to complete the fields on this page.
     *
     * @param message The message for the user
     */
    private void updateStatus(final String message) {
        setMessage(message);
        if (!generatedKeyStore && message == null) {
            bCreateKeyStore.setEnabled(true);
        } else {
            bCreateKeyStore.setEnabled(false);
        }
        setPageComplete(generatedKeyStore);
    }

    /**
     * Handles the events from this wizard page.
     *
     * @param e The triggered event
     */
    public void handleEvent(final Event e) {
        if (e.widget == bCreateKeyStore) {
            createKeystore();
            updateStatus(null);
        } else if (e.widget == bEchoKeyStorePassword) {
            echoPassword(e);
        } else if (e.widget == bEchoKeyPassword) {
            echoPassword(e);
        } else if (e.widget == cKeyAlgorithm) { // Combo Key Algorithm
            if (cKeyAlgorithm.getText().equalsIgnoreCase("AES")) { //$NON-NLS-1$
                cKeyAlgorithmSize.setItems(Algorithms.KEY_SIZES_AES);
                cKeyAlgorithmSize.setText(Algorithms.KEY_SIZES_AES[0]);
            } else if (cKeyAlgorithm.getText().equalsIgnoreCase("Blowfish")) { //$NON-NLS-1$
                cKeyAlgorithmSize.setItems(Algorithms.KEY_SIZES_BLOWFISH);
                cKeyAlgorithmSize.setText(Algorithms.KEY_SIZES_BLOWFISH[0]);
            } else if (cKeyAlgorithm.getText().equalsIgnoreCase("DES")) { //$NON-NLS-1$
                cKeyAlgorithmSize.setItems(Algorithms.KEY_SIZES_DES);
                cKeyAlgorithmSize.setText(Algorithms.KEY_SIZES_DES[0]);
            } else if (cKeyAlgorithm.getText().equalsIgnoreCase("Triple DES")) { //$NON-NLS-1$
                cKeyAlgorithmSize.setItems(Algorithms.KEY_SIZES_DESEDE);
                cKeyAlgorithmSize.setText(Algorithms.KEY_SIZES_DESEDE[0]);
            } else {
                cKeyAlgorithmSize.setItems(Algorithms.KEY_FILE_ALGORITHMS_SIZES);
                cKeyAlgorithmSize.setText(Algorithms.KEY_FILE_ALGORITHMS_SIZES[0]);
            }
        }
    }

    /**
     * Shows plain text or cipher text in the password field.
     *
     * @param e The triggered event
     */
    private void echoPassword(final Event e) {
        if (e.widget == bEchoKeyStorePassword) {
            if (tKeyStorePassword.getEchoChar() == '*') {
                // show plain text
                tKeyStorePassword.setEchoChar('\0');
            } else {
                // show cipher text
                tKeyStorePassword.setEchoChar('*');
            }
        } else if (e.widget == bEchoKeyPassword) {
            if (tKeyPassword.getEchoChar() == '*') {
                // show plain text
            	tKeyPassword.setEchoChar('\0');
            } else {
                // show cipher text
            	tKeyPassword.setEchoChar('*');
            }
        }
    }

    /**
     * Generates the KeyStore and the key based on the entered data and shows the user an
     * information text about the result.
     */
    private void createKeystore() {
        try {
            keyStore = new Keystore(keystore, tKeyStorePassword.getText(), "JCEKS");
            keyStore.store();
            generatedKeyStore = keyStore.generateSecretKey(cKeyAlgorithm.getText(),
            		Integer.parseInt(cKeyAlgorithmSize.getText()), tKeyName.getText(),
            		tKeyPassword.getText());
        } catch (Exception ex) {
            generatedKeyStore = false;
        }

        if (generatedKeyStore) {
        	lResult.setText(NLS.bind(Messages.keystoreGenerated, new Object[] {keyStoreName, tKeyName.getText(), name}));
            updateStatus(null);
        } else {
            lResult.setText(Messages.keystoreGenerationFailed);
        }
    }

    /**
     * Returns the next wizard page after all the necessary data is entered correctly.
     *
     * @return IWizardPage The next wizard page
     */
    public IWizardPage getNextPage() {
        saveDataToModel();
        PageAlgorithms page = ((NewEncryptionWizard) getWizard()).getPageAlgorithms();
        page.onEnterPage();
        return page;
    }

    /**
     * Saves the selections on this wizard page to the model. Called on exit of the page.
     */
    private void saveDataToModel() {
        encryption.setKeyName(tKeyName.getText());
        encryption.setKeyPassword(tKeyPassword.getText().toCharArray());
        encryption.setKeyStore(keyStore);
        encryption.setKeyStorePassword(tKeyStorePassword.getText());
    }
}