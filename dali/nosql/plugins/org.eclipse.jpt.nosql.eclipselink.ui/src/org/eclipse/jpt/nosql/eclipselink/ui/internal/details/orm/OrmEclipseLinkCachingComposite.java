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

import org.eclipse.jpt.common.ui.internal.widgets.Pane;
import org.eclipse.jpt.common.utility.model.value.PropertyValueModel;
import org.eclipse.jpt.nosql.eclipselink.core.context.EclipseLinkCaching;
import org.eclipse.jpt.nosql.eclipselink.ui.internal.details.EclipseLinkCacheSizeCombo;
import org.eclipse.jpt.nosql.eclipselink.ui.internal.details.EclipseLinkCacheTypeComboViewer;
import org.eclipse.jpt.nosql.eclipselink.ui.internal.details.EclipseLinkCachingComposite;
import org.eclipse.jpt.nosql.eclipselink.ui.internal.details.EclipseLinkUiDetailsMessages;
import org.eclipse.jpt.nosql.eclipselink.ui.internal.details.java.JavaEclipseLinkEntityComposite;
import org.eclipse.swt.widgets.Composite;

/**
 * This pane shows the caching options.
 * <p>
 * Here the layout of this pane:
 * <pre>
 * -----------------------------------------------------------------------------
 * | x Shared                                                                  |
 * |    CacheTypeComposite                                                     |
 * |    CacheSizeComposite                                                     |
 * |    > Advanced   	                                                       |
 * |    	ExpiryComposite                                                    |
 * |    	AlwaysRefreshComposite                                             |
 * |   		RefreshOnlyIfNewerComposite                                        |
 * |    	DisableHitsComposite                                               |
 * |    	CacheCoordinationComposite                                         |
 * | ExistenceTypeComposite                                                    |
 * -----------------------------------------------------------------------------</pre>
 *
 * @see Entity
 * @see EclipseLinkCaching
 * @see JavaEclipseLinkEntityComposite - The parent container
 * @see EclipseLinkCacheTypeComboViewer
 * @see EclipseLinkCacheSizeCombo
 * @see EclipseLinkAlwaysRefreshComposite
 * @see EclipseLinkRefreshOnlyIfNewerComposite
 * @see EclipseLinkDisableHitsComposite
 *
 * @version 2.1
 * @since 2.1
 */
public class OrmEclipseLinkCachingComposite extends EclipseLinkCachingComposite<EclipseLinkCaching>
{

	public OrmEclipseLinkCachingComposite(Pane<?> parentPane,
        PropertyValueModel<EclipseLinkCaching> subjectHolder,
        Composite parent) {

		super(parentPane, subjectHolder, parent);
	}

	@Override
	protected void initializeExistenceCheckingComposite(Composite container) {
		this.addLabel(container, EclipseLinkUiDetailsMessages.EclipseLinkExistenceCheckingComposite_label);
		this.addExistenceCheckingTypeCombo(container);
	}
}