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

import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.eclipse.wst.xquery.marklogic.xcc.types.ValueType;
import org.eclipse.wst.xquery.marklogic.xcc.types.XSGYearMonth;


public class XsGYearMonthImpl extends AbstractDateItem implements XSGYearMonth {
    public XsGYearMonthImpl(String bodyString, TimeZone timezone, Locale locale) {
        super(ValueType.XS_GYEAR_MONTH, bodyString, timezone, locale);

        gCalFromGYearMonthString(bodyString);
    }

    public GregorianCalendar asGregorianCalendar() {
        return gCalFromGYearMonthString(asString());
    }
}
