/*******************************************************************************
 * Copyright (c) 2009, 2012 Oracle. All rights reserved.
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
import org.eclipse.jpt.jpa.eclipselink.core.resource.java.EclipseLinkConvertAnnotation;

/**
 * <code>org.eclipse.persistence.annotations.Convert</code>
 */
public final class BinaryEclipseLinkConvertAnnotation
	extends BinaryEclipseLinkBaseConvertAnnotation
	implements EclipseLinkConvertAnnotation
{
	public BinaryEclipseLinkConvertAnnotation(JavaResourceAnnotatedElement parent, IAnnotation jdtAnnotation) {
		super(parent, jdtAnnotation);
	}

	public String getAnnotationName() {
		return ANNOTATION_NAME;
	}

	@Override
	protected String getValueElementName() {
		return EclipseLink.CONVERT__VALUE;
	}
}
