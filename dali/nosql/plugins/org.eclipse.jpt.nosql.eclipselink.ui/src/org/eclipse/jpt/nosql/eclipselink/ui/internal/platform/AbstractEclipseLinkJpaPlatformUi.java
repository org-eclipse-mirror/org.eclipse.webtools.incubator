/*******************************************************************************
 * Copyright (c) 2008, 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.nosql.eclipselink.ui.internal.platform;

import org.eclipse.jpt.common.ui.jface.ItemTreeStateProviderFactoryProvider;
import org.eclipse.jpt.jpa.core.JpaProject;
import org.eclipse.jpt.jpa.ui.JpaPlatformUiProvider;
import org.eclipse.jpt.jpa.ui.internal.platform.base.AbstractJpaPlatformUi;
import org.eclipse.jpt.nosql.eclipselink.ui.internal.wizards.conversion.java.EclipseLinkJavaConverterConversionWizardPage;
import org.eclipse.jpt.nosql.eclipselink.ui.internal.wizards.conversion.java.EclipseLinkJavaGeneratorConversionWizardPage;
import org.eclipse.jpt.nosql.eclipselink.ui.internal.wizards.conversion.java.EclipseLinkJavaQueryConversionWizardPage;

public abstract class AbstractEclipseLinkJpaPlatformUi
	extends AbstractJpaPlatformUi
	implements EclipseLinkJpaPlatformUi
{
	protected AbstractEclipseLinkJpaPlatformUi(
			ItemTreeStateProviderFactoryProvider navigatorFactoryProvider,
			JpaPlatformUiProvider platformUiProvider
	) {
		super(navigatorFactoryProvider, platformUiProvider);
	}

	// ********** metadata conversion **********
	
	public void convertJavaQueryMetadataToGlobal(JpaProject jpaProject) {
		this.openInDialog(new EclipseLinkJavaQueryConversionWizardPage(jpaProject));
	}
	
	public void convertJavaGeneratorMetadataToGlobal(JpaProject jpaProject) {
		this.openInDialog(new EclipseLinkJavaGeneratorConversionWizardPage(jpaProject));
	}

	public void convertJavaConverterMetadataToGlobal(JpaProject jpaProject) {
		this.openInDialog(new EclipseLinkJavaConverterConversionWizardPage(jpaProject));
	}
}
