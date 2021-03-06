/*******************************************************************************
* Copyright (c) 2012 Oracle. All rights reserved.
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License v1.0, which accompanies this distribution
* and is available at http://www.eclipse.org/legal/epl-v10.html.
* 
* Contributors:
*     Oracle - initial API and implementation
*******************************************************************************/
package org.eclipse.jpt.nosql.eclipselink.core.internal.context.persistence;

import org.eclipse.jpt.jpa.core.context.persistence.JarFileRef;
import org.eclipse.jpt.jpa.core.context.persistence.Persistence;
import org.eclipse.jpt.jpa.core.context.persistence.PersistenceUnit;
import org.eclipse.jpt.jpa.core.context.persistence.PersistenceUnitProperties;
import org.eclipse.jpt.jpa.core.internal.context.persistence.AbstractPersistenceXmlContextNodeFactory;
import org.eclipse.jpt.jpa.core.resource.persistence.XmlJarFileRef;
import org.eclipse.jpt.jpa.core.resource.persistence.XmlPersistenceUnit;
import org.eclipse.jpt.nosql.eclipselink.core.context.persistence.EclipseLinkPersistenceUnit;
import org.eclipse.jpt.nosql.eclipselink.core.context.persistence.EclipseLinkPersistenceXmlContextNodeFactory;

 public abstract class AbstractEclipseLinkPersistenceXmlContextNodeFactory extends
		AbstractPersistenceXmlContextNodeFactory implements
		EclipseLinkPersistenceXmlContextNodeFactory {

	@Override
	public PersistenceUnit buildPersistenceUnit(Persistence parent, XmlPersistenceUnit xmlPersistenceUnit) {
		return new EclipseLinkPersistenceUnit(parent, xmlPersistenceUnit);
	}
	
	@Override
	public JarFileRef buildJarFileRef(PersistenceUnit parent, XmlJarFileRef xmlJarFileRef) {
		return new EclipseLinkJarFileRef(parent, xmlJarFileRef);
	}
	
	 public abstract PersistenceUnitProperties buildConnection(PersistenceUnit parent);

	 public abstract PersistenceUnitProperties buildOptions(PersistenceUnit parent);

	 public abstract PersistenceUnitProperties buildLogging(PersistenceUnit parent);

}
