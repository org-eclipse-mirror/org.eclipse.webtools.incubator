/*******************************************************************************
 * Copyright (c) 2009, 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.nosql.eclipselink.ui.internal.details.java;

import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jpt.common.ui.WidgetFactory;
import org.eclipse.jpt.common.utility.internal.model.value.PropertyAspectAdapter;
import org.eclipse.jpt.common.utility.model.value.PropertyValueModel;
import org.eclipse.jpt.jpa.core.jpa2.context.Cascade2_0;
import org.eclipse.jpt.jpa.core.jpa2.context.OrphanRemovable2_0;
import org.eclipse.jpt.jpa.ui.JptJpaUiMessages;
import org.eclipse.jpt.jpa.ui.details.JptJpaUiDetailsMessages;
import org.eclipse.jpt.jpa.ui.internal.details.FetchTypeComboViewer;
import org.eclipse.jpt.jpa.ui.internal.details.TargetEntityClassChooser;
import org.eclipse.jpt.jpa.ui.internal.jpa2.details.CascadePane2_0;
import org.eclipse.jpt.jpa.ui.internal.jpa2.details.Ordering2_0Composite;
import org.eclipse.jpt.jpa.ui.internal.jpa2.details.OrphanRemoval2_0TriStateCheckBox;
import org.eclipse.jpt.nosql.eclipselink.core.context.EclipseLinkOneToManyMapping2_0;
import org.eclipse.jpt.nosql.eclipselink.ui.internal.details.AbstractEclipseLinkOneToManyMappingComposite;
import org.eclipse.jpt.nosql.eclipselink.ui.internal.details.EclipseLinkJoinFetchComboViewer;
import org.eclipse.jpt.nosql.eclipselink.ui.internal.details.EclipseLinkPrivateOwnedCheckBox;
import org.eclipse.jpt.nosql.eclipselink.ui.internal.details.EclipseLinkUiDetailsMessages;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.widgets.Hyperlink;

public class JavaEclipseLinkOneToManyMapping2_0Composite
	extends AbstractEclipseLinkOneToManyMappingComposite<EclipseLinkOneToManyMapping2_0, Cascade2_0>
{
	public JavaEclipseLinkOneToManyMapping2_0Composite(
			PropertyValueModel<? extends EclipseLinkOneToManyMapping2_0> mappingModel,
			PropertyValueModel<Boolean> enabledModel,
			Composite parentComposite,
			WidgetFactory widgetFactory,
			ResourceManager resourceManager) {
		super(mappingModel, enabledModel, parentComposite, widgetFactory, resourceManager);
	}

	@Override
	protected void initializeLayout(Composite container) {
		initializeOneToManyCollapsibleSection(container);
		initializeJoiningStrategyCollapsibleSection(container);
		initializeConvertersCollapsibleSection(container);
		initializeOrderingCollapsibleSection(container);
	}
	
	@Override
	protected Control initializeOneToManySection(Composite container) {
		container = this.addSubPane(container, 2, 0, 0, 0, 0);

		// Target entity widgets
		Hyperlink targetEntityHyperlink = this.addHyperlink(container, JptJpaUiDetailsMessages.TargetEntityChooser_label);
		new TargetEntityClassChooser(this, container, targetEntityHyperlink);

		// Fetch type widgets
		this.addLabel(container, JptJpaUiDetailsMessages.BasicGeneralSection_fetchLabel);
		new FetchTypeComboViewer(this, container);

		// Join fetch widgets
		this.addLabel(container, EclipseLinkUiDetailsMessages.EclipseLinkJoinFetchComposite_label);
		new EclipseLinkJoinFetchComboViewer(this, this.buildJoinFetchModel(), container);

		// Private owned widgets
		EclipseLinkPrivateOwnedCheckBox privateOwnedCheckBox = new EclipseLinkPrivateOwnedCheckBox(this, buildPrivateOwnedModel(), container);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 2;
		privateOwnedCheckBox.getControl().setLayoutData(gridData);

		// Orphan removal widgets
		OrphanRemoval2_0TriStateCheckBox orphanRemovalCheckBox = new OrphanRemoval2_0TriStateCheckBox(this, buildOrphanRemovableModel(), container);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 2;
		orphanRemovalCheckBox.getControl().setLayoutData(gridData);

		// Cascade widgets
		CascadePane2_0 cascadePane = new CascadePane2_0(this, this.buildCascadeModel(), container);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 2;
		cascadePane.getControl().setLayoutData(gridData);

		return container;
	}
	
	@Override
	protected Control initializeOrderingSection(Composite container) {
		return new Ordering2_0Composite(this, container).getControl();
	}

	protected PropertyValueModel<OrphanRemovable2_0> buildOrphanRemovableModel() {
		return new PropertyAspectAdapter<EclipseLinkOneToManyMapping2_0, OrphanRemovable2_0>(this.getSubjectHolder()) {
			@Override
			protected OrphanRemovable2_0 buildValue_() {
				return this.subject.getOrphanRemoval();
			}
		};
	}
}