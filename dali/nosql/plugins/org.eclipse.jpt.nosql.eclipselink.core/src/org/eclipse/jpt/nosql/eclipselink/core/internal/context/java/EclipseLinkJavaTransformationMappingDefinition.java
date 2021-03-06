/*******************************************************************************
 * Copyright (c) 2008, 2011 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.nosql.eclipselink.core.internal.context.java;

import org.eclipse.jpt.common.utility.internal.iterable.EmptyIterable;
import org.eclipse.jpt.jpa.core.JpaFactory;
import org.eclipse.jpt.jpa.core.context.java.JavaAttributeMapping;
import org.eclipse.jpt.jpa.core.context.java.JavaAttributeMappingDefinition;
import org.eclipse.jpt.jpa.core.context.java.JavaPersistentAttribute;
import org.eclipse.jpt.jpa.eclipselink.core.resource.java.EclipseLinkTransformationAnnotation;
import org.eclipse.jpt.nosql.eclipselink.core.EclipseLinkMappingKeys;
import org.eclipse.jpt.nosql.eclipselink.core.internal.EclipseLinkJpaFactory;

public class EclipseLinkJavaTransformationMappingDefinition
	implements JavaAttributeMappingDefinition
{
	// singleton
	private static final JavaAttributeMappingDefinition INSTANCE = new EclipseLinkJavaTransformationMappingDefinition();

	/**
	 * Return the singleton.
	 */
	public static JavaAttributeMappingDefinition instance() {
		return INSTANCE;
	}


	/**
	 * Enforce singleton usage
	 */
	private EclipseLinkJavaTransformationMappingDefinition() {
		super();
	}

	public String getKey() {
		return EclipseLinkMappingKeys.TRANSFORMATION_ATTRIBUTE_MAPPING_KEY;
	}

	public String getAnnotationName() {
		return EclipseLinkTransformationAnnotation.ANNOTATION_NAME;
	}

	public boolean isSpecified(JavaPersistentAttribute persistentAttribute) {
		return persistentAttribute.getResourceAttribute().getAnnotation(this.getAnnotationName()) != null;
	}

	public Iterable<String> getSupportingAnnotationNames() {
		return EmptyIterable.instance();
	}

	public JavaAttributeMapping buildMapping(JavaPersistentAttribute persistentAttribute, JpaFactory factory) {
		return ((EclipseLinkJpaFactory) factory).buildJavaEclipseLinkTransformationMapping(persistentAttribute);
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
}
