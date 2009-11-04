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
package org.eclipse.wst.xquery.marklogic.xcc.types.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;

import org.eclipse.wst.xquery.marklogic.io.IOHelper;
import org.eclipse.wst.xquery.marklogic.xcc.types.ItemType;


public class AbstractStreamableItem extends AbstractItem implements StreamableItem {
    private String stringVal = null;
    private InputStream stream = null;

    public AbstractStreamableItem(ItemType type, String stringVal) {
        super(type);

        this.stringVal = stringVal;
    }

    public AbstractStreamableItem(ItemType type, InputStream stream) {
        super(type);

        this.stream = stream;
    }

    // -------------------------------------------------------------
    // StreamableItem

    public boolean isFetchable() {
        return ((stringVal != null) || (stream != null));
    }

    // TODO: test this
    public void invalidate() {
        stringVal = null;

        if (stream != null) {
            try {
                //noinspection ResultOfMethodCallIgnored
                stream.skip(Long.MAX_VALUE);
                stream.close();
            } catch (IOException e) {
                // do nothing, may have been closed already
            }

            stream = null;
        }
    }

    // -------------------------------------------------------------

    public boolean isCached() {
        return stringVal != null;
    }

    public Reader asReader() {
        if (stream != null) {
            InputStream tmp = stream;

            stream = null;

            try {
                return (new InputStreamReader(tmp, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                return (new InputStreamReader(tmp));
            }
        }

        if (stringVal == null) {
            throw new IllegalStateException("value stream has already been consumed");
        }

        return (new StringReader(stringVal));
    }

    public InputStream asInputStream() {
        if (stream != null) {
            InputStream tmp = stream;

            stream = null;

            return (tmp);
        }

        if (stringVal == null) {
            throw new IllegalStateException("value stream has already been consumed");
        }

        return (IOHelper.newUtf8Stream(stringVal));
    }

    public String asString() {
        if (stringVal != null) {
            return stringVal;
        }

        if (stream == null) {
            throw new IllegalStateException("value stream has already been consumed");
        }

        try {
            stringVal = IOHelper.literalStringFromStream(stream);
        } catch (IOException e) {
            throw new RuntimeException("Could not buffer value as string", e);
        } finally {
            stream = null;
        }

        return (stringVal);
    }
}
