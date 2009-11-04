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
package org.eclipse.wst.xquery.set.internal.debug.ui.interpreters;

import org.eclipse.dltk.internal.debug.ui.interpreters.AbstractInterpreterLibraryBlock;
import org.eclipse.dltk.internal.debug.ui.interpreters.AddScriptInterpreterDialog;
import org.eclipse.dltk.internal.debug.ui.interpreters.LibraryLabelProvider;
import org.eclipse.jface.viewers.IBaseLabelProvider;

public class SETCoreSDKLibraryBlock extends AbstractInterpreterLibraryBlock {

    /**
     * the prefix for dialog setting pertaining to this block
     */
    protected static final String DIALOG_SETTINGS_PREFIX = "SETCoreSDKLibraryBlock"; //$NON-NLS-1$

    public SETCoreSDKLibraryBlock(AddScriptInterpreterDialog d) {
        super(d);
    }

    protected IBaseLabelProvider getLabelProvider() {
        return new LibraryLabelProvider();
    }
}
