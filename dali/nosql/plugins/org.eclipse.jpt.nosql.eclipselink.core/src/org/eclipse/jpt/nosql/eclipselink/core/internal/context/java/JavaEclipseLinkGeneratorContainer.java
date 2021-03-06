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

import org.eclipse.jpt.common.utility.internal.iterable.IterableTools;
import org.eclipse.jpt.jpa.core.context.Generator;
import org.eclipse.jpt.jpa.core.internal.jpa1.context.java.GenericJavaGeneratorContainer;
import org.eclipse.jpt.jpa.eclipselink.core.resource.java.EclipseLinkUuidGeneratorAnnotation2_4;
import org.eclipse.jpt.nosql.eclipselink.core.context.EclipseLinkGeneratorContainer;
import org.eclipse.jpt.nosql.eclipselink.core.context.java.JavaUuidGenerator;

public class JavaEclipseLinkGeneratorContainer
	extends GenericJavaGeneratorContainer
	implements EclipseLinkGeneratorContainer
{
	protected JavaUuidGenerator uuidGenerator;


	public JavaEclipseLinkGeneratorContainer(ParentAdapter parentAdapter) {
		super(parentAdapter);
		this.uuidGenerator = this.buildUuidGenerator();
	}


	// ********** synchronize/update **********

	@Override
	public void synchronizeWithResourceModel() {
		super.synchronizeWithResourceModel();
		this.syncUuidGenerator();
	}

	@Override
	public void update() {
		super.update();
		if (this.uuidGenerator != null) {
			this.uuidGenerator.update();
		}
	}


	// ********** Uuid generator **********

	public JavaUuidGenerator getUuidGenerator() {
		return this.uuidGenerator;
	}

	public JavaUuidGenerator addUuidGenerator() {
		if (this.uuidGenerator != null) {
			throw new IllegalStateException("UUID generator already exists: " + this.uuidGenerator); //$NON-NLS-1$
		}
		EclipseLinkUuidGeneratorAnnotation2_4 annotation = this.buildUuidGeneratorAnnotation();
		JavaUuidGenerator generator = this.buildUuidGenerator(annotation);
		this.setUuidGenerator(generator);
		return generator;
	}

	protected EclipseLinkUuidGeneratorAnnotation2_4 buildUuidGeneratorAnnotation() {
		return (EclipseLinkUuidGeneratorAnnotation2_4) this.parentAdapter.getResourceAnnotatedElement().addAnnotation(EclipseLinkUuidGeneratorAnnotation2_4.ANNOTATION_NAME);
	}

	public void removeUuidGenerator() {
		if (this.uuidGenerator == null) {
			throw new IllegalStateException("UUID generator does not exist"); //$NON-NLS-1$
		}
		this.parentAdapter.getResourceAnnotatedElement().removeAnnotation(EclipseLinkUuidGeneratorAnnotation2_4.ANNOTATION_NAME);
		this.setUuidGenerator(null);
	}

	protected JavaUuidGenerator buildUuidGenerator() {
		EclipseLinkUuidGeneratorAnnotation2_4 annotation = this.getUuidGeneratorAnnotation();
		return (annotation == null) ? null : this.buildUuidGenerator(annotation);
	}

	protected EclipseLinkUuidGeneratorAnnotation2_4 getUuidGeneratorAnnotation() {
		return (EclipseLinkUuidGeneratorAnnotation2_4) this.parentAdapter.getResourceAnnotatedElement().getAnnotation(EclipseLinkUuidGeneratorAnnotation2_4.ANNOTATION_NAME);
	}

	protected JavaUuidGenerator buildUuidGenerator(EclipseLinkUuidGeneratorAnnotation2_4 uuidGeneratorAnnotation) {
		return this.parentAdapter.parentSupportsGenerators() ?
				new JavaEclipseLinkUuidGenerator(this, uuidGeneratorAnnotation) :
				null;
	}

	protected void syncUuidGenerator() {
		EclipseLinkUuidGeneratorAnnotation2_4 annotation = this.getUuidGeneratorAnnotation();
		if (annotation == null) {
			if (this.uuidGenerator != null) {
				this.setUuidGenerator(null);
			}
		} else {
			if ((this.uuidGenerator != null) && (this.uuidGenerator.getGeneratorAnnotation() == annotation)) {
				this.uuidGenerator.synchronizeWithResourceModel();
			} else {
				this.setUuidGenerator(this.buildUuidGenerator(annotation));
			}
		}
	}

	protected void setUuidGenerator(JavaUuidGenerator uuidGenerator) {
		JavaUuidGenerator old = this.uuidGenerator;
		this.uuidGenerator = uuidGenerator;
		this.firePropertyChanged(UUID_GENERATOR_PROPERTY, old, uuidGenerator);
	}


	// ********** code completion **********

	@Override
	public Iterable<String> getCompletionProposals(int pos) {
		Iterable<String> result = super.getCompletionProposals(pos);
		if (result != null) {
			return result;
		}
		if (this.uuidGenerator != null) {
			result = this.uuidGenerator.getCompletionProposals(pos);
			if (result != null) {
				return result;
			}
		}
		return null;
	}


	// ********** misc **********

	@Override
	protected Iterable<Generator> getGenerators_() {
		return IterableTools.<Generator>iterable(this.sequenceGenerator, this.tableGenerator, this.uuidGenerator);
	}
}
