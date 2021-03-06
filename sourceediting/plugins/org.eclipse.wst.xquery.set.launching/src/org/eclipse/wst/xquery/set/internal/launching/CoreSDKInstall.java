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
package org.eclipse.wst.xquery.set.internal.launching;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.dltk.launching.IInterpreterInstallType;
import org.eclipse.dltk.launching.IInterpreterRunner;
import org.eclipse.wst.xquery.core.semantic.ISemanticValidator;
import org.eclipse.wst.xquery.launching.ISemanticValidatingInterpreterInstall;
import org.eclipse.wst.xquery.launching.XQDTInterpreterInstall;
import org.eclipse.wst.xquery.set.core.SETNature;

public class CoreSDKInstall extends XQDTInterpreterInstall implements ISemanticValidatingInterpreterInstall {

    public CoreSDKInstall(IInterpreterInstallType type, String id) {
        super(type, id);
    }

    public String getNatureId() {
        return SETNature.NATURE_ID;
    }

    public ISemanticValidator getSemanticValidator() {
        return new CoreSDKSemanticValidator(this);
    }

    @Override
    public String[] getBuiltinModules() {
        return null;
    }

    @Override
    public IInterpreterRunner getInterpreterRunner(String mode) {
        if (mode.equals(ILaunchManager.DEBUG_MODE)) {
            return super.getInterpreterRunner(mode);
        }
        return new CoreSdkRunner(this);
    }

    @Override
    public void setName(String name) {
        try {
            name = VariablesPlugin.getDefault().getStringVariableManager().performStringSubstitution(name, false);
        } catch (CoreException ce) {
        }
        super.setName(name);
    }
}
