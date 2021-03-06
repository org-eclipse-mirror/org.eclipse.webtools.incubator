/*******************************************************************************
* Copyright (c) 2009, 2012 Oracle. All rights reserved.
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License v1.0, which accompanies this distribution
* and is available at http://www.eclipse.org/legal/epl-v10.html.
* 
* Contributors:
*     Oracle - initial API and implementation
*******************************************************************************/
package org.eclipse.jpt.nosql.eclipselink.ui.internal.ddlgen.wizards;

import org.eclipse.jpt.jpa.core.JpaProject;
import org.eclipse.jpt.nosql.eclipselink.ui.internal.ddlgen.EclipseLinkDDLGeneratorUi;

/**
 *  EclipseLink2_0DDLGeneratorUi
 */
public class EclipseLink2_0DDLGeneratorUi extends EclipseLinkDDLGeneratorUi
{

	// ********** static method **********

	public static void generate(JpaProject project) {
		new EclipseLink2_0DDLGeneratorUi(project).generate();
	}

	// ********** constructors **********
	
	private EclipseLink2_0DDLGeneratorUi(JpaProject project) {
		super(project);
	}

	// ********** behavior **********

	@Override
	protected GenerateDDLWizard buildGenerateDDLWizard(JpaProject project, String puName) {
		return new EclipseLink2_0GenerateDDLWizard(project, puName);
	}
}

