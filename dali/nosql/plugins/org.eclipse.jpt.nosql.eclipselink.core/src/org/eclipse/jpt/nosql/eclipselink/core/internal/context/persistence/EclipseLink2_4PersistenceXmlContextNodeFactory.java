/*******************************************************************************
* Copyright (c) 2012, 2013 Oracle. All rights reserved.
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License v1.0, which accompanies this distribution
* and is available at http://www.eclipse.org/legal/epl-v10.html.
*
* Contributors:
*     Oracle - initial API and implementation
*******************************************************************************/
package org.eclipse.jpt.nosql.eclipselink.core.internal.context.persistence;

import org.eclipse.jpt.jpa.core.context.persistence.PersistenceUnit;
import org.eclipse.jpt.jpa.core.context.persistence.PersistenceUnitProperties;
import org.eclipse.jpt.jpa.core.jpa2.context.persistence.PersistenceUnit2_0;
import org.eclipse.jpt.jpa.core.jpa2.context.persistence.options.JpaOptions2_0;
import org.eclipse.jpt.nosql.eclipselink.core.context.persistence.connection.NoSqlConnection;
import org.eclipse.jpt.nosql.eclipselink.core.internal.context.persistence.connection.EclipseLinkMongoDBConnection;


public class EclipseLink2_4PersistenceXmlContextNodeFactory
		extends AbstractEclipseLinkPersistenceXmlContextNodeFactory {

	@Override
	public NoSqlConnection buildConnection(PersistenceUnit parent) {
		// TODO
		return new EclipseLinkMongoDBConnection(parent);
	}

	@Override
	public JpaOptions2_0 buildOptions(PersistenceUnit parent) {
		return new EclipseLinkOptions2_0((PersistenceUnit2_0) parent);
	}

	@Override
	public PersistenceUnitProperties buildLogging(PersistenceUnit parent) {
		return new EclipseLinkLogging2_4((PersistenceUnit2_0) parent);
	}
}
