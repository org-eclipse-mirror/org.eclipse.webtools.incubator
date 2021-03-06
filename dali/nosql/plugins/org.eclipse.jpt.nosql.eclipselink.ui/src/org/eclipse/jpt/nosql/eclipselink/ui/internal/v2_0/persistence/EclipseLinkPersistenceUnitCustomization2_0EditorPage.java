/*******************************************************************************
* Copyright (c) 2010, 2012 Oracle. All rights reserved.
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License v1.0, which accompanies this distribution
* and is available at http://www.eclipse.org/legal/epl-v10.html.
* 
* Contributors:
*     Oracle - initial API and implementation
*******************************************************************************/
package org.eclipse.jpt.nosql.eclipselink.ui.internal.v2_0.persistence;

import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jpt.common.ui.WidgetFactory;
import org.eclipse.jpt.common.utility.model.value.PropertyValueModel;
import org.eclipse.jpt.nosql.eclipselink.core.context.persistence.Customization;
import org.eclipse.jpt.nosql.eclipselink.ui.internal.persistence.customization.EclipseLinkPersistenceUnitCustomizationEditorPage;
import org.eclipse.swt.widgets.Composite;

public class EclipseLinkPersistenceUnitCustomization2_0EditorPage
	extends EclipseLinkPersistenceUnitCustomizationEditorPage<Customization>
{
	public EclipseLinkPersistenceUnitCustomization2_0EditorPage(
		PropertyValueModel<Customization> subjectModel,
			Composite parentComposite,
            WidgetFactory widgetFactory,
            ResourceManager resourceManager) {
		super(subjectModel, parentComposite, widgetFactory, resourceManager);
	}

	@Override
	protected void buildEntityListComposite(Composite parent) {
		// do nothing
	}
}
