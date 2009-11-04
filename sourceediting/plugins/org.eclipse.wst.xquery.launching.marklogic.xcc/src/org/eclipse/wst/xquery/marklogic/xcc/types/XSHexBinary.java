/*
 * Copyright (c) 2003-2009 Mark Logic Corporation. All rights reserved.
 *
 * This program and the accompanying materials are made available 
 * under the terms of the Eclipse Public License v1.0 which 
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Mark Logic, Inc.
 */
package org.eclipse.wst.xquery.marklogic.xcc.types;

/**
 * XDM type: xs:hexBinary.
 */
public interface XSHexBinary extends XdmAtomic {
    /**
     * @return A byte array containing the decoded, binary data represented by this xs:hexBinary
     *         item.
     */
    byte[] asBinaryData();
}
