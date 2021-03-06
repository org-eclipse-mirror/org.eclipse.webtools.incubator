/*******************************************************************************
 * Copyright (c) 2008, 2013 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.nosql.eclipselink.core.internal.context.java;

import org.eclipse.jpt.common.utility.internal.iterable.ArrayIterable;
import org.eclipse.jpt.common.utility.internal.iterable.IterableTools;
import org.eclipse.jpt.jpa.core.context.java.DefaultJavaAttributeMappingDefinition;
import org.eclipse.jpt.jpa.core.context.java.JavaAttributeMappingDefinition;
import org.eclipse.jpt.jpa.core.context.java.JavaPersistentAttribute;
import org.eclipse.jpt.jpa.core.internal.context.java.JavaAttributeMappingDefinitionWrapper;
import org.eclipse.jpt.jpa.core.internal.context.java.JavaOneToManyMappingDefinition;
import org.eclipse.jpt.jpa.eclipselink.core.resource.java.EclipseLinkConverterAnnotation;
import org.eclipse.jpt.jpa.eclipselink.core.resource.java.EclipseLinkJoinFetchAnnotation;
import org.eclipse.jpt.jpa.eclipselink.core.resource.java.EclipseLinkObjectTypeConverterAnnotation;
import org.eclipse.jpt.jpa.eclipselink.core.resource.java.EclipseLinkPrivateOwnedAnnotation;
import org.eclipse.jpt.jpa.eclipselink.core.resource.java.EclipseLinkStructConverterAnnotation;
import org.eclipse.jpt.jpa.eclipselink.core.resource.java.EclipseLinkTypeConverterAnnotation;

public class EclipseLinkJavaOneToManyMappingDefinition
	extends JavaAttributeMappingDefinitionWrapper
	implements DefaultJavaAttributeMappingDefinition
{
	private static final JavaAttributeMappingDefinition DELEGATE = JavaOneToManyMappingDefinition.instance();

	// singleton
	private static final DefaultJavaAttributeMappingDefinition INSTANCE = new EclipseLinkJavaOneToManyMappingDefinition();

	/**
	 * Return the singleton.
	 */
	public static DefaultJavaAttributeMappingDefinition instance() {
		return INSTANCE;
	}


	/**
	 * Enforce singleton usage
	 */
	private EclipseLinkJavaOneToManyMappingDefinition() {
		super();
	}

	@Override
	protected JavaAttributeMappingDefinition getDelegate() {
		return DELEGATE;
	}

	@Override
	public Iterable<String> getSupportingAnnotationNames() {
		return COMBINED_SUPPORTING_ANNOTATION_NAMES;
	}

	private static final String[] ECLIPSE_LINK_SUPPORTING_ANNOTATION_NAMES_ARRAY = new String[] {
		EclipseLinkConverterAnnotation.ANNOTATION_NAME,
		EclipseLinkJoinFetchAnnotation.ANNOTATION_NAME,
		EclipseLinkObjectTypeConverterAnnotation.ANNOTATION_NAME,
		EclipseLinkPrivateOwnedAnnotation.ANNOTATION_NAME,
		EclipseLinkStructConverterAnnotation.ANNOTATION_NAME,
		EclipseLinkTypeConverterAnnotation.ANNOTATION_NAME,
	};
	public static final Iterable<String> ECLIPSE_LINK_SUPPORTING_ANNOTATION_NAMES = IterableTools.iterable(ECLIPSE_LINK_SUPPORTING_ANNOTATION_NAMES_ARRAY);

	@SuppressWarnings("unchecked")
	private static final Iterable<String> COMBINED_SUPPORTING_ANNOTATION_NAMES = IterableTools.concatenate(
		DELEGATE.getSupportingAnnotationNames(),
		ECLIPSE_LINK_SUPPORTING_ANNOTATION_NAMES
	);

	public boolean isDefault(JavaPersistentAttribute persistentAttribute) {
		String targetEntity = persistentAttribute.getMultiReferenceTargetTypeName();
		return (targetEntity != null)
				&& (persistentAttribute.getPersistenceUnit().getEntity(targetEntity) != null);
	}
}
