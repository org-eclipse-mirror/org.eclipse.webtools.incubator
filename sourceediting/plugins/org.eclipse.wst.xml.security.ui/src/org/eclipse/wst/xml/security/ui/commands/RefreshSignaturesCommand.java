/*******************************************************************************
 * Copyright (c) 2009 Dominik Schadow - http://www.xml-sicherheit.de All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Dominik Schadow - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xml.security.ui.commands;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.apache.xml.security.keys.keyresolver.KeyResolverException;
import org.apache.xml.security.signature.XMLSignatureException;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.wst.xml.security.core.verify.VerificationResult;
import org.eclipse.wst.xml.security.core.verify.VerifyDocument;
import org.eclipse.wst.xml.security.ui.XSTUIPlugin;
import org.eclipse.wst.xml.security.ui.utils.Utils;
import org.eclipse.wst.xml.security.ui.verify.SignatureView;

/**
 * <p>Refreshes all XML Signatures in the <b>XML Signatures</b> view.</p>
 *
 * @author Dominik Schadow
 * @version 0.5.0
 */
public class RefreshSignaturesCommand extends AbstractHandler {
    public Object execute(ExecutionEvent event) throws ExecutionException {
        if (HandlerUtil.getActiveEditor(event) != null) {
            final IEditorPart editorPart = HandlerUtil.getActiveEditor(event);

            if (editorPart.isDirty()) {
                if (null != editorPart.getTitle() && editorPart.getTitle().length() > 0) {
                    IRunnableWithProgress op = new IRunnableWithProgress() {
                        public void run(final IProgressMonitor monitor) {
                            editorPart.doSave(monitor);
                        }
                    };
                    try {
                        PlatformUI.getWorkbench().getProgressService().runInUI(XSTUIPlugin.getActiveWorkbenchWindow(),
                                op, ResourcesPlugin.getWorkspace().getRoot());
                    } catch (InvocationTargetException ite) {
                        Utils.log("Error while saving editor content", ite); //$NON-NLS-1$
                    } catch (InterruptedException ie) {
                        Utils.log("Error while saving editor content", ie); //$NON-NLS-1$
                    }
                } else {
                    editorPart.doSaveAs();
                }
            }

            try {
                ArrayList<VerificationResult> results = new ArrayList<VerificationResult>();
                VerifyDocument verify = new VerifyDocument();
                IFile file = (IFile) editorPart.getEditorInput().getAdapter(IFile.class);
                if (file != null) {
                    results = verify.verify(file.getLocation().toString());
                } else {
                    MessageDialog.openInformation(HandlerUtil.getActiveShell(event),
                            Messages.RefreshSignaturesCommand_0,
                            NLS.bind(Messages.RemoveReadOnlyFlag, Messages.RefreshSignaturesCommand_1));
                }

                if (results.size() == 0) {
                    MessageDialog.openInformation(HandlerUtil.getActiveShell(event),
                            Messages.RefreshSignaturesCommand_0, Messages.RefreshSignaturesCommand_2);
                }

                // show results
                IViewPart vp = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().showView(SignatureView.ID);
                if (vp instanceof SignatureView) {
                    ((SignatureView) vp).setInput(results);
                }
            } catch (XMLSignatureException xmlse) {
                MessageDialog.openError(HandlerUtil.getActiveShell(event), Messages.RefreshSignaturesCommand_3,
                        xmlse.getLocalizedMessage());
            } catch (KeyResolverException kre) {
                MessageDialog.openError(HandlerUtil.getActiveShell(event), Messages.RefreshSignaturesCommand_4,
                        kre.getLocalizedMessage());
            } catch (Exception ex) {
                MessageDialog.openError(HandlerUtil.getActiveShell(event), Messages.RefreshSignaturesCommand_5,
                        ex.getLocalizedMessage());
                Utils.log("Error during verification of XML signature", ex); //$NON-NLS-1$
            }
        }

        return null;
    }
}
