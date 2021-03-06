/*******************************************************************************
 * Copyright (c) 2011, 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.nosql.eclipselink.ui.internal.wizards.conversion.java;

import org.eclipse.jpt.jpa.core.JpaProject;
import org.eclipse.jpt.jpa.ui.JptJpaUiMessages;
import org.eclipse.jpt.jpa.ui.internal.wizards.conversion.java.GenericJavaGeneratorConversionWizardPage;
import org.eclipse.jpt.nosql.eclipselink.ui.internal.EclipseLinkUiMessages;

public class EclipseLinkJavaGeneratorConversionWizardPage
	extends EclipseLinkJavaMetadataConversionWizardPage
{
	public EclipseLinkJavaGeneratorConversionWizardPage(JpaProject jpaProject) {
		super(jpaProject, JptJpaUiMessages.JavaGeneratorConversionWizardPage_title, JptJpaUiMessages.JavaGeneratorConversionWizardPage_description);
	}

	@Override
	protected boolean hasConvertibleJavaMetadata_() {
		return this.persistenceUnit.hasConvertibleJavaGenerators();
	}

	@Override
	protected String getMissingJavaMetadataWarningMessage() {
		return JptJpaUiMessages.JAVA_METADATA_CONVERSION_noGeneratorsToConvert;
	}

	@Override
	protected ConversionCommand.Strategy getConversionCommandStrategy() {
		return GenericJavaGeneratorConversionWizardPage.CONVERSION_COMMAND_STRATEGY;
	}

	@Override
	protected boolean hasAnyEquivalentJavaMetadata_() {
		return this.getPersistenceUnit().hasAnyEquivalentJavaGenerators();
	}

	@Override
	public String getEquivalentJavaMetadataWarningDialogTitle() {
		return EclipseLinkUiMessages.JavaMetadataConversion_equivalentGeneratorsWarningTitle;
	}

	@Override
	public String getEquivalentJavaMetadataWarningDialogMessage() {
		return EclipseLinkUiMessages.JavaMetadataConversion_equivalentGeneratorsWarningMessage;
	}
}
