/*******************************************************************************
 * Copyright (c) 2011, 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.nosql.eclipselink.ui.internal.details.java;

import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jpt.common.ui.WidgetFactory;
import org.eclipse.jpt.common.utility.model.value.PropertyValueModel;
import org.eclipse.jpt.jpa.core.context.Entity;
import org.eclipse.jpt.jpa.core.context.MappedSuperclass;
import org.eclipse.jpt.jpa.ui.details.JpaComposite;
import org.eclipse.jpt.nosql.eclipselink.core.context.java.JavaEclipseLinkEntity;
import org.eclipse.jpt.nosql.eclipselink.core.context.java.JavaEclipseLinkMappedSuperclass;
import org.eclipse.swt.widgets.Composite;

public class EclipseLink2_3JavaUiFactory
	extends EclipseLink2_0JavaUiFactory
{
	// ********** type mappings **********

	@SuppressWarnings("unchecked")
	@Override
	public JpaComposite createEntityComposite(
			PropertyValueModel<? extends Entity> entityModel,
			Composite parentComposite,
			WidgetFactory widgetFactory,
			ResourceManager resourceManager) {
		return new JavaEclipseLinkEntity2_3Composite((PropertyValueModel<? extends JavaEclipseLinkEntity>) entityModel, parentComposite, widgetFactory, resourceManager);
	}

	@SuppressWarnings("unchecked")
	@Override
	public JpaComposite createMappedSuperclassComposite(
			PropertyValueModel<? extends MappedSuperclass> mappedSuperclassModel,
			Composite parentComposite,
			WidgetFactory widgetFactory,
			ResourceManager resourceManager) {
		return new JavaEclipseLinkMappedSuperclass2_3Composite((PropertyValueModel<? extends JavaEclipseLinkMappedSuperclass>) mappedSuperclassModel, parentComposite, widgetFactory, resourceManager);
	}
}
