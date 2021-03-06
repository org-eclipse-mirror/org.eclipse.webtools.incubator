/*******************************************************************************
 * Copyright (c) 2009, 2011 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.nosql.eclipselink.core.internal.resource.java.binary;

import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jpt.common.core.resource.java.JavaResourceAnnotatedElement;
import org.eclipse.jpt.jpa.eclipselink.core.resource.java.EclipseLink;
import org.eclipse.jpt.jpa.eclipselink.core.resource.java.EclipseLinkReadTransformerAnnotation;

/**
 * org.eclipse.persistence.annotations.ReadTransformer
 */
public final class BinaryEclipseLinkReadTransformerAnnotation
	extends BinaryEclipseLinkTransformerAnnotation
	implements EclipseLinkReadTransformerAnnotation
{

	public BinaryEclipseLinkReadTransformerAnnotation(JavaResourceAnnotatedElement parent, IAnnotation jdtAnnotation) {
		super(parent, jdtAnnotation);
	}

	public String getAnnotationName() {
		return ANNOTATION_NAME;
	}


	// ********** BinaryTransformerAnnotation implementation **********

	@Override
	String getTransformerClassElementName() {
		return EclipseLink.READ_TRANSFORMER__TRANSFORMER_CLASS;
	}

	@Override
	String getMethodElementName() {
		return EclipseLink.READ_TRANSFORMER__METHOD;
	}

}
