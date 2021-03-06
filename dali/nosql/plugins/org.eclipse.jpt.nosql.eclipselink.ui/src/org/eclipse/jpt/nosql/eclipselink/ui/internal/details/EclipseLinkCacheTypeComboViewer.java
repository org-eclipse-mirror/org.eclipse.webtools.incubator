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

import java.util.Collection;

import org.eclipse.jpt.common.ui.internal.widgets.EnumFormComboViewer;
import org.eclipse.jpt.common.ui.internal.widgets.Pane;
import org.eclipse.jpt.common.utility.model.value.PropertyValueModel;
import org.eclipse.jpt.nosql.eclipselink.core.context.EclipseLinkCacheType;
import org.eclipse.jpt.nosql.eclipselink.core.context.EclipseLinkCaching;
import org.eclipse.jpt.nosql.eclipselink.ui.internal.EclipseLinkHelpContextIds;
import org.eclipse.swt.widgets.Composite;

/**
 * Here is the layout of this pane:
 * <pre>
 * ----------------------------------------------------------------------------
 * |       ------------------------------------------------------------------ |
 * | Type: |                                                              |v| |
 * |       ------------------------------------------------------------------ |
 * ----------------------------------------------------------------------------</pre>
 *
 * @see EclipseLinkCaching
 * @see EclipseLinkCachingComposite - A container of this widget
 *
 * @version 2.1
 * @since 2.1
 */
public class EclipseLinkCacheTypeComboViewer extends EnumFormComboViewer<EclipseLinkCaching, EclipseLinkCacheType> {

	/**
	 * Creates a new <code>CacheTypeComposite</code>.
	 *
	 * @param parentPane The parent container of this one
	 * @param parent The parent container
	 */
	public EclipseLinkCacheTypeComboViewer(Pane<? extends EclipseLinkCaching> parentPane,
	                          Composite parent,
	                          PropertyValueModel<Boolean> enabledModel) {

		super(parentPane, parent, enabledModel);
	}

	@Override
	protected void addPropertyNames(Collection<String> propertyNames) {
		super.addPropertyNames(propertyNames);
		propertyNames.add(EclipseLinkCaching.DEFAULT_TYPE_PROPERTY);
		propertyNames.add(EclipseLinkCaching.SPECIFIED_TYPE_PROPERTY);
	}

	@Override
	protected EclipseLinkCacheType[] getChoices() {
		return EclipseLinkCacheType.values();
	}

	@Override
	protected EclipseLinkCacheType getDefaultValue() {
		return getSubject().getDefaultType();
	}

	@Override
	protected String displayString(EclipseLinkCacheType value) {
		switch (value) {
			case FULL :
				return EclipseLinkUiDetailsMessages.EclipseLinkCacheTypeComposite_full;
			case WEAK :
				return EclipseLinkUiDetailsMessages.EclipseLinkCacheTypeComposite_weak;
			case SOFT :
				return EclipseLinkUiDetailsMessages.EclipseLinkCacheTypeComposite_soft;
			case SOFT_WEAK :
				return EclipseLinkUiDetailsMessages.EclipseLinkCacheTypeComposite_soft_weak;
			case HARD_WEAK :
				return EclipseLinkUiDetailsMessages.EclipseLinkCacheTypeComposite_hard_weak;
			case CACHE :
				return EclipseLinkUiDetailsMessages.EclipseLinkCacheTypeComposite_cache;
			case NONE :
				return EclipseLinkUiDetailsMessages.EclipseLinkCacheTypeComposite_none;
			default :
				throw new IllegalStateException();
		}
	}

	@Override
	protected EclipseLinkCacheType getValue() {
		return getSubject().getSpecifiedType();
	}

	@Override
	protected void setValue(EclipseLinkCacheType value) {
		getSubject().setSpecifiedType(value);
	}

	@Override
	protected boolean sortChoices() {
		return false;
	}

	@Override
	protected String getHelpId() {
		return EclipseLinkHelpContextIds.CACHING_CACHE_TYPE;
	}
}
