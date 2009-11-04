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
 * Type indicator class for values that are sequences.
 */
public final class SequenceType extends ValueType {
    SequenceType(String name) {
        super(name);
    }

    /**
     * Always returns true.
     * 
     * @return True to indicate that this is a sequence.
     */
    @Override
    public boolean isSequence() {
        return (true);
    }
}
