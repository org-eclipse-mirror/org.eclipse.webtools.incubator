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
package org.eclipse.wst.xquery.set.debug.ui;

import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.dltk.core.environment.EnvironmentManager;
import org.eclipse.dltk.core.environment.IFileHandle;
import org.eclipse.dltk.debug.ui.interpreters.InterpretersUpdater;
import org.eclipse.dltk.internal.launching.LazyFileHandle;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.InterpreterStandin;
import org.eclipse.dltk.launching.LibraryLocation;
import org.eclipse.dltk.launching.ScriptRuntime;
import org.eclipse.dltk.launching.ScriptRuntime.DefaultInterpreterEntry;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.prefs.BackingStoreException;
import org.eclipse.wst.xquery.set.core.SETCorePlugin;
import org.eclipse.wst.xquery.set.core.SETNature;
import org.eclipse.wst.xquery.set.internal.debug.ui.launcher.ServerJobTools;
import org.eclipse.wst.xquery.set.internal.launching.CoreSDKInstallType;

/**
 * The activator class controls the plug-in life cycle
 */
public class SETDebugUIPlugin extends AbstractUIPlugin {

    // The plug-in ID
    public static final String PLUGIN_ID = "org.eclipse.wst.xquery.set.debug.ui";

    // The shared instance
    private static SETDebugUIPlugin plugin;

    /**
     * The constructor
     */
    public SETDebugUIPlugin() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
     */
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;

        ServerJobTools.getInstance();

        //searchAndAddDefaultCoreSdk();
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
    public static SETDebugUIPlugin getDefault() {
        return plugin;
    }

    @SuppressWarnings("unused")
    private void searchAndAddDefaultCoreSdk() {
        try {
            // if on Windows, add the default shipped CoreSDK
            if (Platform.getOS().equals(Platform.OS_WIN32)) {
//				// return if the default CoreSDK was already installed
//				if (getPluginPreferences().getBoolean(ISETPreferenceConstants.SET_CORESDK_INITIALIZED)) {
//					//return;
//				}

                // this is only a piece of code to make sure the CoreSDK installation
                // type is loaded form the plugin configuration in the DLTK registry
                DefaultInterpreterEntry entry = new DefaultInterpreterEntry(SETNature.NATURE_ID, EnvironmentManager
                        .getLocalEnvironment().getId());
                ScriptRuntime.getDefaultInterpreterInstall(entry);

                Bundle[] bundles = Platform.getBundles(SETCorePlugin.PLUGIN_ID + "sdk.win32", null);
                if (bundles == null || bundles.length == 0) {
                    return;
                }

                Bundle bundle = bundles[0];
                URL coreSdkURL = bundle.getEntry("/coresdk/bin/sausalito.bat");
                String id = String.valueOf(System.currentTimeMillis());
                IFileHandle installLocation = new LazyFileHandle(EnvironmentManager.getLocalEnvironment().getId(),
                        new Path(FileLocator.toFileURL(coreSdkURL).getFile()));

                final InterpreterStandin coreSdk = new InterpreterStandin(new CoreSDKInstallType(), id);
                coreSdk.setInstallLocation(installLocation);
                coreSdk.setName("Sausalito CoreSDK");
                coreSdk.setInterpreterArgs(null);
                LibraryLocation lib = new LibraryLocation(installLocation.getFullPath().removeLastSegments(2).append(
                        "modules"));
                coreSdk.setLibraryLocations(new LibraryLocation[] { lib });

                // Save the interpreter configuration and set the preference that
                // this was done. This will not be done next time.
                final boolean[] canceled = new boolean[] { false };
//				BusyIndicator.showWhile(null, new Runnable() {
//					public void run() {
                InterpretersUpdater updater = new InterpretersUpdater();
                if (!updater.updateInterpreterSettings(SETNature.NATURE_ID, new IInterpreterInstall[] { coreSdk },
                        new IInterpreterInstall[] { coreSdk })) {
                    canceled[0] = true;
                }
//					}
//				});

                if (canceled[0]) {
                    getLog().log(new Status(IStatus.ERROR, PLUGIN_ID, "Could update the interpreter settings"));
                } else {
                    IEclipsePreferences node = new InstanceScope().getNode(PLUGIN_ID);
//                    node.putBoolean(ISETPreferenceConstants.SET_CORESDK_INITIALIZED, true);
                    node.flush();
                }
                // on Unix-based platforms search the default installation location
                // /opt/sausalito
            } else if (Platform.getOS().equals(Platform.OS_LINUX) || Platform.getOS().equals(Platform.OS_MACOSX)) {
//				IFileHandle installLocation = new LazyFileHandle(EnvironmentManager.getLocalEnvironment().getId(), new Path(FileLocator.toFileURL("").getFile()));				
            }

        } catch (BackingStoreException bse) {
            getLog().log(
                    new Status(IStatus.ERROR, PLUGIN_ID,
                            "Could not mark the enablement of the default CoreSDK in the plugin preferences"));
        } catch (IOException ioe) {
            getLog().log(new Status(IStatus.ERROR, PLUGIN_ID, "Could not find the default CoreSDK Sausalito script"));
        }
    }
}