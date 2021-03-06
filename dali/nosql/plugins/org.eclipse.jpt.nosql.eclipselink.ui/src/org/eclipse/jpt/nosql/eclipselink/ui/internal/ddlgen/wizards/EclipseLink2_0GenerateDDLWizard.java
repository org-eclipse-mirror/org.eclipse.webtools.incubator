/*******************************************************************************
* Copyright (c) 2012 Oracle. All rights reserved.
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License v1.0, which accompanies this distribution
* and is available at http://www.eclipse.org/legal/epl-v10.html.
* 
* Contributors:
*     Oracle - initial API and implementation
*******************************************************************************/
package org.eclipse.jpt.nosql.eclipselink.ui.internal.ddlgen.wizards;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jpt.common.core.gen.JptGenerator;
import org.eclipse.jpt.jpa.core.JpaProject;
import org.eclipse.jpt.nosql.eclipselink.core.context.persistence.OutputMode;
import org.eclipse.jpt.nosql.eclipselink.core.internal.ddlgen.EclipseLink2_0DDLGenerator;
import org.eclipse.wst.validation.ValidationFramework;

/**
 *  EclipseLink2_0GenerateDDLWizard
 */
public class EclipseLink2_0GenerateDDLWizard extends GenerateDDLWizard {

	public EclipseLink2_0GenerateDDLWizard(JpaProject jpaProject, String puName) {
		super(jpaProject, puName);
	}

	@Override
	protected WorkspaceJob buildGenerateDDLJob(String puName, JpaProject project, OutputMode outputMode) {
		return new Generate2_0DDLJob(puName, project, outputMode);
	}

	// ********** generate ddl job **********

	protected static class Generate2_0DDLJob extends GenerateDDLWizard.GenerateDDLJob {

		// ********** constructor **********
		
		protected Generate2_0DDLJob(String puName, JpaProject jpaProject, OutputMode outputMode) {
			super(puName, jpaProject, outputMode);
		}

		// ********** overwrite AbstractJptGenerateJob **********

		@Override
		protected JptGenerator buildGenerator() {
			return new EclipseLink2_0DDLGenerator(this.puName, this.jpaProject, this.outputMode);
		}

		@Override
		protected void postGenerate() {
			super.postGenerate();

			if(this.outputMode != OutputMode.database) {
				this.validateProject();
			}
		}

		// ********** internal methods **********

		/**
		 * Performs validation after tables have been generated
		 */
		private void validateProject() {
			IProject[] projects = new IProject[] {this.jpaProject.getProject()};
			try {
				ValidationFramework.getDefault().validate(projects, true, false, new NullProgressMonitor());
			}
			catch (CoreException e) {
				this.logException(e);
			}
		}
	}
	
}
