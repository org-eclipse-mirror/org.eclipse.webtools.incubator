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
package org.eclipse.wst.xquery.set.ui;

import org.eclipse.dltk.core.IDLTKLanguageToolkit;
import org.eclipse.dltk.ui.IDLTKUILanguageToolkit;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.wst.xquery.core.XQDTLanguageToolkit;
import org.eclipse.wst.xquery.ui.XQDTUILanguageToolkit;

public class SETUILanguageToolkit extends XQDTUILanguageToolkit {

    private static SETUILanguageToolkit sToolkit = null;

    @Override
    public IPreferenceStore getPreferenceStore() {
        return SETUIPlugin.getDefault().getPreferenceStore();
    }

    @Override
    public IDLTKLanguageToolkit getCoreToolkit() {
        return XQDTLanguageToolkit.getDefault();
    }

    @Override
    public String getInterpreterPreferencePage() {
        return ISETUIConstants.ID_INTEREPRTERS_PREFERENCE_PAGE;
    }

    public static IDLTKUILanguageToolkit getInstance() {
        if (sToolkit == null) {
            sToolkit = new SETUILanguageToolkit();
        }
        return sToolkit;
    }

    public String getInterpreterContainerId() {
        return "org.eclipse.wst.xquery.set.launching.INTERPRETER_CONTAINER";
    }
}