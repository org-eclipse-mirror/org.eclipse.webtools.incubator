/*******************************************************************************
 * Copyright (c) 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.nosql.eclipselink.core.internal.context.java;

import org.eclipse.jpt.jpa.core.context.java.JavaGeneratorContainer;
import org.eclipse.jpt.jpa.core.context.orm.EntityMappings;
import org.eclipse.jpt.jpa.core.internal.context.java.AbstractJavaGenerator;
import org.eclipse.jpt.jpa.eclipselink.core.resource.java.EclipseLinkUuidGeneratorAnnotation2_4;
import org.eclipse.jpt.nosql.eclipselink.core.context.UuidGenerator;
import org.eclipse.jpt.nosql.eclipselink.core.context.java.JavaUuidGenerator;
import org.eclipse.jpt.nosql.eclipselink.core.context.orm.EclipseLinkEntityMappings;

/**
 * Java UUID generator
 */
public class JavaEclipseLinkUuidGenerator
	extends AbstractJavaGenerator<EclipseLinkUuidGeneratorAnnotation2_4>
	implements JavaUuidGenerator
{


	public JavaEclipseLinkUuidGenerator(JavaGeneratorContainer parent, EclipseLinkUuidGeneratorAnnotation2_4 generatorAnnotation) {
		super(parent, generatorAnnotation);
	}


	// ********** misc **********
	
	public Class<UuidGenerator> getType() {
		return UuidGenerator.class;
	}

	@Override
	public JavaEclipseLinkGeneratorContainer getParent() {
		return (JavaEclipseLinkGeneratorContainer) super.getParent();
	}

	// ********** metadata conversion **********

	public void convertTo(EntityMappings entityMappings) {
		((EclipseLinkEntityMappings) entityMappings).addUuidGenerator().convertFrom(this);
	}

	public void delete() {
		this.getParent().removeUuidGenerator();	
	}
}
