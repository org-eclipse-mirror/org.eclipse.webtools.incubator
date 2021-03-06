/*******************************************************************************
 * Copyright (c) 2008, 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.nosql.eclipselink.core.internal.context.java;

import java.util.List;
import org.eclipse.jpt.common.core.utility.TextRange;
import org.eclipse.jpt.jpa.eclipselink.core.resource.java.EclipseLinkConverterAnnotation;
import org.eclipse.jpt.nosql.eclipselink.core.context.EclipseLinkCustomConverter;
import org.eclipse.jpt.nosql.eclipselink.core.context.java.JavaEclipseLinkConverterContainer;
import org.eclipse.jpt.nosql.eclipselink.core.context.orm.OrmEclipseLinkConverterContainer;
import org.eclipse.jpt.nosql.eclipselink.core.internal.EclipseLinkJpaValidationMessages;
import org.eclipse.wst.validation.internal.provisional.core.IMessage;

/**
 * <code>org.eclipse.persistence.annotations.Converter</code>
 */
public class JavaEclipseLinkCustomConverter
	extends JavaEclipseLinkConverterClassConverter<EclipseLinkConverterAnnotation>
	implements EclipseLinkCustomConverter
{
	private String fullyQualifiedConverterClass;


	public JavaEclipseLinkCustomConverter(JavaEclipseLinkConverterContainer parent, EclipseLinkConverterAnnotation converterAnnotation) {
		super(parent, converterAnnotation, converterAnnotation.getConverterClass());
	}


	// ********** synchronize/update **********

	@Override
	public void update() {
		super.update();
		this.setFullyQualifiedConverterClass(this.converterAnnotation.getFullyQualifiedConverterClassName());
	}


	// ********** converter class **********

	@Override
	protected String getAnnotationConverterClass() {
		return this.converterAnnotation.getConverterClass();
	}

	@Override
	protected void setAnnotationConverterClass(String converterClass) {
		this.converterAnnotation.setConverterClass(converterClass);
	}


	// ********** fully qualified converter class **********

	public String getFullyQualifiedConverterClass() {
		return this.fullyQualifiedConverterClass;
	}

	protected void setFullyQualifiedConverterClass(String converterClass) {
		String old = this.fullyQualifiedConverterClass;
		this.fullyQualifiedConverterClass = converterClass;
		this.firePropertyChanged(FULLY_QUALIFIED_CONVERTER_CLASS_PROPERTY, old, converterClass);
	}


	// ********** misc **********

	public Class<EclipseLinkCustomConverter> getType() {
		return EclipseLinkCustomConverter.class;
	}


	//************ validation ***************

	@Override
	protected String getEclipseLinkConverterInterface() {
		return ECLIPSELINK_CONVERTER_CLASS_NAME;
	}

	@Override
	protected String getEclipseLinkConverterInterfaceErrorMessage() {
		return EclipseLinkJpaValidationMessages.CONVERTER_CLASS_IMPLEMENTS_CONVERTER;
	}

	@Override
	protected TextRange getAnnotationConverterClassTextRange() {
		return this.converterAnnotation.getConverterClassTextRange();
	}

	@Override
	protected void addConverterClassDoesNotExistMessageTo(List<IMessage> messages) {
		// no need to add message since there will already be a compiler error
	}

	// ********** metadata conversion **********

	@Override
	public void convertTo(OrmEclipseLinkConverterContainer ormConverterContainer) {
		ormConverterContainer.addCustomConverter().convertFrom(this);
	}
	
	@Override
	public void delete() {
		this.getParent().removeCustomConverter(this);
	}
}
