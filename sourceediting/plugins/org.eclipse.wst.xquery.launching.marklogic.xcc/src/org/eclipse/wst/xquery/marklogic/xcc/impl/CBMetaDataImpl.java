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
package org.eclipse.wst.xquery.marklogic.xcc.impl;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.wst.xquery.marklogic.xcc.ContentbaseMetaData;
import org.eclipse.wst.xquery.marklogic.xcc.Request;
import org.eclipse.wst.xquery.marklogic.xcc.ResultSequence;
import org.eclipse.wst.xquery.marklogic.xcc.Session;
import org.eclipse.wst.xquery.marklogic.xcc.Version;
import org.eclipse.wst.xquery.marklogic.xcc.exceptions.RequestException;
import org.eclipse.wst.xquery.marklogic.xcc.types.XSDecimal;
import org.eclipse.wst.xquery.marklogic.xcc.types.XSInteger;


class CBMetaDataImpl implements ContentbaseMetaData {
    private static final long REFRESH_DELTA_MILLIS = 1 * 60 * 1000;
    private Session session;
    private final Request serverVersionRequest;
    private final Request contentBaseNameRequest;
    private final Request forestMapRequest;
    private String serverVersionString = null;
    private int serverMajorVersion = 0;
    private int serverMinorVersion = 0;
    private int serverPatchVersion = 0;
    private BigInteger contentbaseId = null;
    private String contentBaseName = null;
    private Map<String, BigInteger> forestMap = null;

    CBMetaDataImpl(Session session) {
        this.session = session;

        serverVersionRequest = session.newAdhocQuery("xdmp:version()");
        contentBaseNameRequest = session.newAdhocQuery("(xdmp:database(), xdmp:database-name (xdmp:database()))");
        forestMapRequest = session.newAdhocQuery("for $id in xdmp:database-forests (xdmp:database())"
                + "return (xdmp:forest-name ($id), $id)");
    }

    public Session getSession() {
        return session;
    }

    public String getUser() {
        return session.getUserCredentials().getUserName();
    }

    public String getContentBaseName() throws RequestException {
        refreshContentBaseIdentity();

        return contentBaseName;
    }

    public BigInteger getContentBaseId() throws RequestException {
        refreshContentBaseIdentity();

        return contentbaseId;
    }

    public BigInteger[] getForestIds() throws RequestException {
        refreshForestMap();

        Collection<BigInteger> values = forestMap.values();
        BigInteger[] ids = new BigInteger[values.size()];

        values.toArray(ids);

        return ids;
    }

    public String[] getForestNames() throws RequestException {
        refreshForestMap();

        Set<String> keys = forestMap.keySet();
        String[] names = new String[keys.size()];

        keys.toArray(names);

        return (names);
    }

    public Map<String, BigInteger> getForestMap() throws RequestException {
        refreshForestMap();

        return forestMap;
    }

    public String getDriverVersionString() {
        return Version.getVersionString();
    }

    public int getDriverMajorVersion() {
        return Version.getVersionMajor();
    }

    public int getDriverMinorVersion() {
        return Version.getVersionMinor();
    }

    public int getDriverPatchVersion() {
        return Version.getVersionPatch();
    }

    public String getServerVersionString() throws RequestException {
        refreshServerVersion();

        return serverVersionString;
    }

    public int getServerMajorVersion() throws RequestException {
        refreshServerVersion();

        return serverMajorVersion;
    }

    public int getServerMinorVersion() throws RequestException {
        refreshServerVersion();

        return serverMinorVersion;
    }

    public int getServerPatchVersion() throws RequestException {
        refreshServerVersion();

        return serverPatchVersion;
    }

    // --------------------------------------

    private long lastServerVersionCheck = 0;
    private long lastContentBaseNameCheck = 0;
    private long lastForestMapCheck = 0;

    private void refreshServerVersion() throws RequestException {
        if (stillFresh(lastServerVersionCheck)) {
            return;
        }

        lastServerVersionCheck = System.currentTimeMillis();

        ResultSequence rs = session.submitRequest(serverVersionRequest);

        serverVersionString = rs.asString();

        String[] subs = serverVersionString.split("\\.|-");

        if (subs.length < 3) {
            throw new RequestException("Malformed server version string: " + serverVersionString, serverVersionRequest);
        }

        serverMajorVersion = Integer.parseInt(subs[0]);
        serverMinorVersion = Integer.parseInt(subs[1]);
        serverPatchVersion = Integer.parseInt(subs[2]);
    }

    private void refreshContentBaseIdentity() throws RequestException {
        if (stillFresh(lastContentBaseNameCheck)) {
            return;
        }

        lastContentBaseNameCheck = System.currentTimeMillis();

        ResultSequence rs = session.submitRequest(contentBaseNameRequest);

        if (rs.itemAt(0) instanceof XSDecimal) {
            contentbaseId = ((XSDecimal)rs.itemAt(0)).asBigDecimal().toBigInteger();
        } else {
            contentbaseId = ((XSInteger)rs.itemAt(0)).asBigInteger();
        }

        contentBaseName = rs.itemAt(1).asString();
    }

    private void refreshForestMap() throws RequestException {
        if (stillFresh(lastForestMapCheck)) {
            return;
        }

        lastForestMapCheck = System.currentTimeMillis();

        ResultSequence rs = session.submitRequest(forestMapRequest);
        String[] values = rs.asStrings();
        Map<String, BigInteger> map = new HashMap<String, BigInteger>();

        for (int i = 0; i < values.length; i += 2) {
            map.put(values[i], new BigInteger(values[i + 1]));
        }

        forestMap = Collections.unmodifiableMap(map);
    }

    private boolean stillFresh(long timestamp) {
        long now = System.currentTimeMillis();

        return ((now - timestamp) < REFRESH_DELTA_MILLIS);
    }
}
