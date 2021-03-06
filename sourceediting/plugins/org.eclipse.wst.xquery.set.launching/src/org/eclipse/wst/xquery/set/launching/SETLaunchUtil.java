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
package org.eclipse.wst.xquery.set.launching;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.dltk.launching.ScriptLaunchConfigurationConstants;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.eclipse.ui.internal.browser.WebBrowserPreference;
import org.eclipse.wst.xquery.set.debug.core.ISETLaunchConfigurationConstants;

@SuppressWarnings("restriction")
public class SETLaunchUtil {

    public static void openBrowser(ILaunch launch) {
        final String startPage, host;
        final int port;
        try {
            ILaunchConfiguration config = launch.getLaunchConfiguration();
            startPage = config.getAttribute(ScriptLaunchConfigurationConstants.ATTR_MAIN_SCRIPT_NAME, "");
            host = config.getAttribute(ISETLaunchConfigurationConstants.ATTR_XQDT_SET_HOST, "127.0.0.1");
            port = config.getAttribute(ISETLaunchConfigurationConstants.ATTR_XQDT_SET_PORT, 8080);
        } catch (CoreException ce) {
            // TODO Auto-generated catch block
            ce.printStackTrace();
            return;
        }

        Display.getDefault().syncExec(new Runnable() {
            public void run() {
                try {
                    String urlStr = "http://" + host;
                    if (port != 80) {
                        urlStr += ":" + port;
                    }
                    urlStr += startPage;

                    URL url = new URL(urlStr);
                    IWorkbenchBrowserSupport browserSupport = PlatformUI.getWorkbench().getBrowserSupport();
                    IWebBrowser browser = null;
                    if (WebBrowserPreference.EXTERNAL == WebBrowserPreference.getBrowserChoice()) {
                        browser = PlatformUI.getWorkbench().getBrowserSupport().getExternalBrowser();
                    } else {
                        int browserStyle = IWorkbenchBrowserSupport.LOCATION_BAR
                                | IWorkbenchBrowserSupport.NAVIGATION_BAR | IWorkbenchBrowserSupport.STATUS;
                        String browserTitle = "Sausalito: " + url.toString();
                        browser = browserSupport.createBrowser(browserStyle, "SausalitoBrowser", browserTitle
                                .toString(), browserTitle.toString());
                    }
                    browser.openURL(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (PartInitException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}