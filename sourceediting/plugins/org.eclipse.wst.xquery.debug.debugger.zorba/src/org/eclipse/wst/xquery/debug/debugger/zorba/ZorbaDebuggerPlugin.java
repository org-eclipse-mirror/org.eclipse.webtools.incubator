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
package org.eclipse.wst.xquery.debug.debugger.zorba;

import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class ZorbaDebuggerPlugin extends AbstractUIPlugin {

    // The plug-in ID
    public static final String PLUGIN_ID = "org.eclipse.wst.xquery.debug.debugger.zorba";

    // The shared instance
    private static ZorbaDebuggerPlugin plugin;

    public static final boolean DEBUG_DEBUGGER_ENGINE = Boolean.valueOf(
            Platform.getDebugOption(PLUGIN_ID + "/debug/debuggerEngine")).booleanValue();
    public static final boolean DEBUG_ZORBA_DEBUG_PROTOCOL = Boolean.valueOf(
            Platform.getDebugOption(PLUGIN_ID + "/debug/zorbaDebugProtocol")).booleanValue();
    public static final boolean DEBUG_DBGP_TRANSLATOR = Boolean.valueOf(
            Platform.getDebugOption(PLUGIN_ID + "/debug/dbgpTranslator")).booleanValue();

    /**
     * The constructor
     */
    public ZorbaDebuggerPlugin() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
     */
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
     */
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    /**
     * Returns the shared instance
     * 
     * @return the shared instance
     */
    public static ZorbaDebuggerPlugin getDefault() {
        return plugin;
    }

}
