/*******************************************************************************
 * Copyright (c) 2009, 2011 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.nosql.eclipselink.core.internal.resource.java;

import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jpt.common.core.resource.java.Annotation;
import org.eclipse.jpt.common.core.resource.java.AnnotationDefinition;
import org.eclipse.jpt.common.core.resource.java.JavaResourceAnnotatedElement;
import org.eclipse.jpt.common.core.utility.jdt.AnnotatedElement;
import org.eclipse.jpt.jpa.eclipselink.core.resource.java.EclipseLinkCacheAnnotation;
import org.eclipse.jpt.nosql.eclipselink.core.internal.resource.java.binary.BinaryEclipseLinkCacheAnnotation;
import org.eclipse.jpt.nosql.eclipselink.core.internal.resource.java.source.SourceEclipseLinkCacheAnnotation;

/**
 * org.eclipse.persistence.annotations.Cache
 */
public class EclipseLinkCacheAnnotationDefinition
	implements AnnotationDefinition
{
	// singleton
	private static final AnnotationDefinition INSTANCE = new EclipseLinkCacheAnnotationDefinition();

	/**
	 * Return the singleton.
	 */
	public static AnnotationDefinition instance() {
		return INSTANCE;
	}

	/**
	 * Ensure single instance.
	 */
	private EclipseLinkCacheAnnotationDefinition() {
		super();
	}

	public Annotation buildAnnotation(JavaResourceAnnotatedElement parent, AnnotatedElement annotatedElement) {
		return new SourceEclipseLinkCacheAnnotation(parent, annotatedElement);
	}

	public Annotation buildNullAnnotation(JavaResourceAnnotatedElement parent) {
		return new NullEclipseLinkCacheAnnotation(parent);
	}

	public Annotation buildAnnotation(JavaResourceAnnotatedElement parent, IAnnotation jdtAnnotation) {
		return new BinaryEclipseLinkCacheAnnotation(parent, jdtAnnotation);
	}

	public String getAnnotationName() {
		return EclipseLinkCacheAnnotation.ANNOTATION_NAME;
	}
}
