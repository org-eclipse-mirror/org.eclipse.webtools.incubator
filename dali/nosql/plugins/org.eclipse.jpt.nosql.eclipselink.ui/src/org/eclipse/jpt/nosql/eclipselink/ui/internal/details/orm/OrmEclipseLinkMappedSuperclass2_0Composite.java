/*******************************************************************************
 * Copyright (c) 2008, 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.nosql.eclipselink.ui.internal.details.orm;

import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jpt.common.ui.WidgetFactory;
import org.eclipse.jpt.common.utility.model.value.PropertyValueModel;
import org.eclipse.jpt.nosql.eclipselink.core.context.orm.OrmEclipseLinkMappedSuperclass;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class OrmEclipseLinkMappedSuperclass2_0Composite
	extends AbstractOrmEclipseLinkMappedSuperclassComposite<OrmEclipseLinkMappedSuperclass>
{
	public OrmEclipseLinkMappedSuperclass2_0Composite(
			PropertyValueModel<? extends OrmEclipseLinkMappedSuperclass> mappedSuperclassModel,
			Composite parentComposite,
			WidgetFactory widgetFactory,
			ResourceManager resourceManager) {
		super(mappedSuperclassModel, parentComposite, widgetFactory, resourceManager);
	}

	@Override
	protected Control initializeCachingSection(Composite container) {
		return new OrmEclipseLinkCaching2_0Composite(this, this.buildCachingModel(), container).getControl();
	}
}
