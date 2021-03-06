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
package org.eclipse.wst.xquery.debug.debugger.zorba.translator.communication;

import java.io.IOException;

public class TransportTimeoutException extends IOException {

    private static final long serialVersionUID = 1L;

    public TransportTimeoutException() {
    }

    public TransportTimeoutException(String arg1) {
        super(arg1);
    }
}
