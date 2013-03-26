/*******************************************************************************
 * Copyright (c) 2009, 2012 Oracle. All rights reserved.
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
import org.eclipse.jpt.jpa.ui.JptJpaUiMessages;
import org.eclipse.jpt.jpa.ui.details.JptJpaUiDetailsMessages;
import org.eclipse.jpt.jpa.ui.details.orm.JptJpaUiDetailsOrmMessages;
import org.eclipse.jpt.jpa.ui.internal.details.AccessTypeComboViewer;
import org.eclipse.jpt.jpa.ui.internal.details.FetchTypeComboViewer;
import org.eclipse.jpt.jpa.ui.internal.details.orm.OrmMappingNameText;
import org.eclipse.jpt.jpa.ui.internal.jpa2.details.CollectionTable2_0Composite;
import org.eclipse.jpt.jpa.ui.internal.jpa2.details.TargetClassChooser;
import org.eclipse.jpt.jpa.ui.jpa2.details.JptJpaUiDetailsMessages2_0;
import org.eclipse.jpt.nosql.eclipselink.core.context.EclipseLinkElementCollectionMapping2_0;
import org.eclipse.jpt.nosql.eclipselink.ui.internal.details.EclipseLinkElementCollectionMapping2_0Composite;
import org.eclipse.jpt.nosql.eclipselink.ui.internal.details.EclipseLinkUiDetailsMessages;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.widgets.Hyperlink;

public class OrmEclipseLinkElementCollectionMapping2_0Composite
	extends EclipseLinkElementCollectionMapping2_0Composite<EclipseLinkElementCollectionMapping2_0>
{
	public OrmEclipseLinkElementCollectionMapping2_0Composite(
			PropertyValueModel<? extends EclipseLinkElementCollectionMapping2_0> mappingModel,
			PropertyValueModel<Boolean> enabledModel,
			Composite parentComposite,
			WidgetFactory widgetFactory,
			ResourceManager resourceManager) {
		super(mappingModel, enabledModel, parentComposite, widgetFactory, resourceManager);
	}

	@Override
	protected Control buildElementCollectionSectionClient(Composite container) {
		container = this.addSubPane(container, 2, 0, 0, 0, 0);

		// Target class widgets
		Hyperlink hyperlink = this.addHyperlink(container, JptJpaUiDetailsMessages2_0.TARGET_CLASS_COMPOSITE_LABEL);
		new TargetClassChooser(this, container, hyperlink);

		// Name widgets
		this.addLabel(container, JptJpaUiDetailsOrmMessages.ORM_MAPPING_NAME_CHOOSER_NAME);
		new OrmMappingNameText(this, getSubjectHolder(), container);

		// Attribute type widgets
		Hyperlink attributeTypeHyperlink = this.addHyperlink(container, EclipseLinkUiDetailsMessages.OrmAttributeTypeComposite_attributeType);
		new OrmAttributeTypeClassChooser(this, getSubjectHolder(), container, attributeTypeHyperlink);

		// Access type widgets
		this.addLabel(container, JptJpaUiMessages.AccessTypeComposite_access);
		new AccessTypeComboViewer(this, this.buildAccessReferenceModel(), container);

		// Fetch type widgets
		this.addLabel(container, JptJpaUiDetailsMessages.BasicGeneralSection_fetchLabel);
		new FetchTypeComboViewer(this, container);

		// Collection table widgets
		CollectionTable2_0Composite collectionTableComposite = new CollectionTable2_0Composite(this, buildCollectionTableModel(), container);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 2;
		collectionTableComposite.getControl().setLayoutData(gridData);

		return container;
	}
}