/*******************************************************************************
 * Copyright (c) 2008, 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.nosql.eclipselink.ui.internal.details.java;

import org.eclipse.jpt.jpa.core.MappingKeys;
import org.eclipse.jpt.jpa.ui.details.DefaultMappingUiDefinition;
import org.eclipse.jpt.jpa.ui.internal.details.AbstractOneToOneMappingUiDefinition;
import org.eclipse.jpt.nosql.eclipselink.ui.internal.details.EclipseLinkUiDetailsMessages;

public class DefaultJavaEclipseLinkOneToOneMappingUiDefinition
	extends AbstractOneToOneMappingUiDefinition
	implements DefaultMappingUiDefinition
{
	// singleton
	private static final DefaultJavaEclipseLinkOneToOneMappingUiDefinition INSTANCE = new DefaultJavaEclipseLinkOneToOneMappingUiDefinition();

	/**
	 * Return the singleton.
	 */
	public static DefaultMappingUiDefinition instance() {
		return INSTANCE;
	}


	/**
	 * Ensure single instance.
	 */
	private DefaultJavaEclipseLinkOneToOneMappingUiDefinition() {
		super();
	}

	public String getKey() {
		return null;
	}

	public String getDefaultKey() {
		return MappingKeys.ONE_TO_ONE_ATTRIBUTE_MAPPING_KEY;
	}

	public String getLabel() {
		return EclipseLinkUiDetailsMessages.DefaultEclipseLinkOneToOneMappingUiProvider_label;
	}

	public String getLinkLabel() {
		return EclipseLinkUiDetailsMessages.DefaultEclipseLinkOneToOneMappingUiProvider_linkLabel;
	}
}
