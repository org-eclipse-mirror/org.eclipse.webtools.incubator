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
import org.eclipse.jpt.jpa.ui.details.orm.JptJpaUiDetailsOrmMessages;
import org.eclipse.jpt.jpa.ui.internal.details.AccessTypeComboViewer;
import org.eclipse.jpt.jpa.ui.internal.details.ColumnComposite;
import org.eclipse.jpt.jpa.ui.internal.details.orm.OrmMappingNameText;
import org.eclipse.jpt.jpa.ui.internal.jpa2.details.IdMapping2_0MappedByRelationshipPane;
import org.eclipse.jpt.jpa.ui.internal.jpa2.details.IdMappingGeneration2_0Composite;
import org.eclipse.jpt.nosql.eclipselink.core.context.EclipseLinkIdMapping2_0;
import org.eclipse.jpt.nosql.eclipselink.ui.internal.details.EclipseLinkIdMappingComposite;
import org.eclipse.jpt.nosql.eclipselink.ui.internal.details.EclipseLinkMutableTriStateCheckBox;
import org.eclipse.jpt.nosql.eclipselink.ui.internal.details.EclipseLinkUiDetailsMessages;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.widgets.Hyperlink;

public class OrmEclipseLinkIdMapping2_0Composite
	extends EclipseLinkIdMappingComposite<EclipseLinkIdMapping2_0>
{
	public OrmEclipseLinkIdMapping2_0Composite(
			PropertyValueModel<? extends EclipseLinkIdMapping2_0> mappingModel,
			PropertyValueModel<Boolean> enabledModel,
			Composite parentComposite,
			WidgetFactory widgetFactory,
			ResourceManager resourceManager) {
		super(mappingModel, enabledModel, parentComposite, widgetFactory, resourceManager);
	}
	

	@Override
	protected void initializeLayout(Composite container) {
		initializeIdCollapsibleSection(container);
		initializeTypeCollapsibleSection(container);
		initializeConvertersCollapsibleSection(container);
		initializeGenerationCollapsibleSection(container);
	}
	
	@Override
	protected Control initializeIdSection(Composite container) {
		container = this.addSubPane(container, 2, 0, 0, 0, 0);

		IdMapping2_0MappedByRelationshipPane mappedByRelationshipPane = new IdMapping2_0MappedByRelationshipPane(this, getSubjectHolder(), container);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 2;
		mappedByRelationshipPane.getControl().setLayoutData(gridData);

		// Column widgets
		ColumnComposite columnComposite = new ColumnComposite(this, buildColumnModel(), container);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 2;
		columnComposite.getControl().setLayoutData(gridData);

		// Name widgets
		this.addLabel(container, JptJpaUiDetailsOrmMessages.ORM_MAPPING_NAME_CHOOSER_NAME);
		new OrmMappingNameText(this, getSubjectHolder(), container);

		// Attribute type widgets
		Hyperlink attributeTypeHyperlink = this.addHyperlink(container, EclipseLinkUiDetailsMessages.OrmAttributeTypeComposite_attributeType);
		new OrmAttributeTypeClassChooser(this, getSubjectHolder(), container, attributeTypeHyperlink);

		// Access type widgets
		this.addLabel(container, JptJpaUiMessages.AccessTypeComposite_access);
		new AccessTypeComboViewer(this, this.buildAccessReferenceModel(), container);

		// Mutable widgets
		EclipseLinkMutableTriStateCheckBox mutableCheckBox = new EclipseLinkMutableTriStateCheckBox(this, buildMutableModel(), container);
		gridData = new GridData();
		gridData.horizontalSpan = 2;
		mutableCheckBox.getControl().setLayoutData(gridData);

		return container;
	}
	
	@Override
	protected void initializeGenerationCollapsibleSection(Composite container) {
		new IdMappingGeneration2_0Composite(this, container);
	}
}
