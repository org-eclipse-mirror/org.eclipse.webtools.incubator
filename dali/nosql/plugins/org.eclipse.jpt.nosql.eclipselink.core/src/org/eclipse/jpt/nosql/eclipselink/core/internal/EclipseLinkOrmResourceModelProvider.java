/*******************************************************************************
 * Copyright (c) 2008, 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.nosql.eclipselink.core.internal;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.jpt.common.core.resource.xml.JptXmlResource;
import org.eclipse.jpt.jpa.core.JpaProject;
import org.eclipse.jpt.jpa.core.JpaResourceModelProvider;
import org.eclipse.jpt.jpa.eclipselink.core.resource.orm.XmlEntityMappings;
import org.eclipse.jpt.nosql.eclipselink.core.internal.resource.orm.EclipseLinkOrmXmlResourceProvider;

/**
 * EclipseLink orm.xml
 */
public class EclipseLinkOrmResourceModelProvider
	implements JpaResourceModelProvider
{
	// singleton
	private static final JpaResourceModelProvider INSTANCE = new EclipseLinkOrmResourceModelProvider();
	
	
	/**
	 * Return the singleton
	 */
	public static JpaResourceModelProvider instance() {
		return INSTANCE;
	}
	
	
	/**
	 * Enforce singleton usage
	 */
	private EclipseLinkOrmResourceModelProvider() {
		super();
	}
	
	
	public IContentType getContentType() {
		return XmlEntityMappings.CONTENT_TYPE;
	}
	
	public JptXmlResource buildResourceModel(JpaProject jpaProject, IFile file) {
		return EclipseLinkOrmXmlResourceProvider.getXmlResourceProvider(file).getXmlResource();
	}
}
