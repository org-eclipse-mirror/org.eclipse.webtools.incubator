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
import org.eclipse.jpt.common.core.internal.resource.java.binary.BinaryAnnotation;
import org.eclipse.jpt.common.core.resource.java.JavaResourceAnnotatedElement;
import org.eclipse.jpt.common.core.utility.TextRange;
import org.eclipse.jpt.jpa.eclipselink.core.resource.java.EclipseLink;
import org.eclipse.jpt.jpa.eclipselink.core.resource.java.EclipseLinkExistenceCheckingAnnotation;
import org.eclipse.jpt.jpa.eclipselink.core.resource.java.ExistenceType;

/**
 * <code>org.eclipse.persistence.annotations.ExistenceChecking</code>
 */
public final class BinaryEclipseLinkExistenceCheckingAnnotation
	extends BinaryAnnotation
	implements EclipseLinkExistenceCheckingAnnotation
{
	private ExistenceType value;


	public BinaryEclipseLinkExistenceCheckingAnnotation(JavaResourceAnnotatedElement parent, IAnnotation jdtAnnotation) {
		super(parent, jdtAnnotation);
		this.value = this.buildValue();
	}

	public String getAnnotationName() {
		return ANNOTATION_NAME;
	}

	@Override
	public void update() {
		super.update();
		this.setValue_(this.buildValue());
	}


	// ********** ExistenceCheckingAnnotation implementation **********

	// ***** value
	public ExistenceType getValue() {
		return this.value;
	}

	public void setValue(ExistenceType value) {
		throw new UnsupportedOperationException();
	}

	private void setValue_(ExistenceType value) {
		ExistenceType old = this.value;
		this.value = value;
		this.firePropertyChanged(VALUE_PROPERTY, old, value);
	}

	private ExistenceType buildValue() {
		return ExistenceType.fromJavaAnnotationValue(this.getJdtMemberValue(EclipseLink.EXISTENCE_CHECKING__VALUE));
	}

	public TextRange getValueTextRange() {
		throw new UnsupportedOperationException();
	}
}
