/*******************************************************************************
 * Copyright (c) 2008, 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.nosql.eclipselink.ui.internal.details;

import org.eclipse.jpt.common.ui.internal.widgets.Pane;
import org.eclipse.jpt.jpa.core.context.Embeddable;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.Hyperlink;

public class EclipseLinkEmbeddableAdvancedComposite extends EclipseLinkTypeMappingAdvancedComposite<Embeddable> {
	
	public EclipseLinkEmbeddableAdvancedComposite(
			Pane<? extends Embeddable> parentPane,
			Composite parent) {

		super(parentPane, parent);
	}

	@Override
	protected Composite addComposite(Composite container) {
		return this.addSubPane(container, 2, 0, 0, 0, 0);
	}

	@Override
	protected void initializeLayout(Composite container) {
		// customizer class chooser
		Hyperlink customizerHyperlink = addHyperlink(container, EclipseLinkUiDetailsMessages.EclipseLinkCustomizerComposite_classLabel);
		new EclipseLinkCustomizerClassChooser(this, this.buildCustomizerHolder(), container, customizerHyperlink);

		// change tracking type
		this.addLabel(container, EclipseLinkUiDetailsMessages.EclipseLinkChangeTrackingComposite_label); 
		new EclipseLinkChangeTrackingComboViewer(this, this.buildChangeTrackingHolder(), container);
	}
}
