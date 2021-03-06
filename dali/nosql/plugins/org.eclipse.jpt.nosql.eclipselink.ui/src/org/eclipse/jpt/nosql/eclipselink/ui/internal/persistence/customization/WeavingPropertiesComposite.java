/*******************************************************************************
 * Copyright (c) 2010, 2013 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.nosql.eclipselink.ui.internal.persistence.customization;

import java.util.Collection;

import org.eclipse.jpt.common.ui.JptCommonUiMessages;
import org.eclipse.jpt.common.ui.internal.widgets.EnumFormComboViewer;
import org.eclipse.jpt.common.ui.internal.widgets.Pane;
import org.eclipse.jpt.common.ui.internal.widgets.TriStateCheckBox;
import org.eclipse.jpt.common.utility.internal.model.value.PropertyAspectAdapter;
import org.eclipse.jpt.common.utility.internal.model.value.TransformationPropertyValueModel;
import org.eclipse.jpt.common.utility.model.value.ModifiablePropertyValueModel;
import org.eclipse.jpt.common.utility.model.value.PropertyValueModel;
import org.eclipse.jpt.nosql.eclipselink.core.context.persistence.Customization;
import org.eclipse.jpt.nosql.eclipselink.core.context.persistence.Weaving;
import org.eclipse.jpt.nosql.eclipselink.ui.internal.EclipseLinkHelpContextIds;
import org.eclipse.jpt.nosql.eclipselink.ui.internal.EclipseLinkUiMessages;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

/**
 *  WeavingPropertiesComposite
 */
public class WeavingPropertiesComposite extends Pane<Customization>
{
	public WeavingPropertiesComposite(Pane<? extends Customization> subjectHolder,
	                                       Composite container) {

		super(subjectHolder, container);
	}

	@Override
	protected Composite addComposite(Composite parent) {
		return this.addSubPane(parent, 2, 0, 0, 0, 0);
	}

	@Override
	protected void initializeLayout(Composite container) {
		// Weaving
		this.addLabel(container, EclipseLinkUiMessages.PersistenceXmlCustomizationTab_weavingLabel);
		this.addWeavingCombo(container);

		// Weaving Lazy
		TriStateCheckBox weavingLazyCheckBox = this.addTriStateCheckBoxWithDefault(
			container,
			EclipseLinkUiMessages.PersistenceXmlCustomizationTab_weavingLazyLabel,
			this.buildWeavingLazyHolder(),
			this.buildWeavingLazyStringHolder(),
			EclipseLinkHelpContextIds.PERSISTENCE_CUSTOMIZATION
		);
		GridData gridData = new GridData();
		gridData.horizontalSpan = 2;
		weavingLazyCheckBox.getCheckBox().setLayoutData(gridData);

		// Weaving Fetch Groups
		TriStateCheckBox weavingFetchGroupsCheckBox = this.addTriStateCheckBoxWithDefault(
			container,
			EclipseLinkUiMessages.PersistenceXmlCustomizationTab_weavingFetchGroupsLabel,
			this.buildWeavingFetchGroupsHolder(),
			this.buildWeavingFetchGroupsStringHolder(),
			EclipseLinkHelpContextIds.PERSISTENCE_CUSTOMIZATION
		);
		gridData = new GridData();
		gridData.horizontalSpan = 2;
		weavingFetchGroupsCheckBox.getCheckBox().setLayoutData(gridData);

		// Weaving Internal
		TriStateCheckBox weavingInternalCheckBox = this.addTriStateCheckBoxWithDefault(
			container,
			EclipseLinkUiMessages.PersistenceXmlCustomizationTab_weavingInternalLabel,
			this.buildWeavingInternalHolder(),
			this.buildWeavingInternalStringHolder(),
			EclipseLinkHelpContextIds.PERSISTENCE_CUSTOMIZATION
		);
		gridData = new GridData();
		gridData.horizontalSpan = 2;
		weavingInternalCheckBox.getCheckBox().setLayoutData(gridData);

		// Weaving Eager
		TriStateCheckBox weavingEagerCheckBox = this.addTriStateCheckBoxWithDefault(
			container,
			EclipseLinkUiMessages.PersistenceXmlCustomizationTab_weavingEagerLabel,
			this.buildWeavingEagerHolder(),
			this.buildWeavingEagerStringHolder(),
			EclipseLinkHelpContextIds.PERSISTENCE_CUSTOMIZATION
		);
		gridData = new GridData();
		gridData.horizontalSpan = 2;
		weavingEagerCheckBox.getCheckBox().setLayoutData(gridData);

		// Weaving Change Tracking
		TriStateCheckBox weavingChangeTrackingCheckBox = this.addTriStateCheckBoxWithDefault(
			container,
			EclipseLinkUiMessages.PersistenceXmlCustomizationTab_weavingChangeTrackingLabel,
			this.buildWeavingChangeTrackingHolder(),
			this.buildWeavingChangeTrackingStringHolder(),
			EclipseLinkHelpContextIds.PERSISTENCE_CUSTOMIZATION
		);
		gridData = new GridData();
		gridData.horizontalSpan = 2;
		weavingChangeTrackingCheckBox.getCheckBox().setLayoutData(gridData);
	}

	// ********* weaving **********

	private EnumFormComboViewer<Customization, Weaving> addWeavingCombo(Composite container) {
		return new EnumFormComboViewer<Customization, Weaving>(this, container) {
			@Override
			protected void addPropertyNames(Collection<String> propertyNames) {
				super.addPropertyNames(propertyNames);
				propertyNames.add(Customization.WEAVING_PROPERTY);
			}

			@Override
			protected Weaving[] getChoices() {
				return Weaving.values();
			}

			@Override
			protected Weaving getDefaultValue() {
				return getSubject().getDefaultWeaving();
			}

			@Override
			protected String displayString(Weaving value) {
				switch (value) {
					case true_ :
						return EclipseLinkUiMessages.WeavingComposite_true_;
					case false_ :
						return EclipseLinkUiMessages.WeavingComposite_false_;
					case static_ :
						return EclipseLinkUiMessages.WeavingComposite_static_;
					default :
						throw new IllegalStateException();
				}
			}

			@Override
			protected Weaving getValue() {
				return getSubject().getWeaving();
			}

			@Override
			protected void setValue(Weaving value) {
				getSubject().setWeaving(value);
			}

			@Override
			protected String getHelpId() {
				return EclipseLinkHelpContextIds.PERSISTENCE_CUSTOMIZATION;
			}
		};
	}


	// ********* weaving lazy **********
	
	private ModifiablePropertyValueModel<Boolean> buildWeavingLazyHolder() {
		return new PropertyAspectAdapter<Customization, Boolean>(getSubjectHolder(), Customization.WEAVING_LAZY_PROPERTY) {
			@Override
			protected Boolean buildValue_() {
				return this.subject.getWeavingLazy();
			}

			@Override
			protected void setValue_(Boolean value) {
				this.subject.setWeavingLazy(value);
			}
		};
	}

	private PropertyValueModel<String> buildWeavingLazyStringHolder() {
		return new TransformationPropertyValueModel<Boolean, String>(buildDefaultWeavingLazyHolder()) {
			@Override
			protected String transform(Boolean value) {
				if (value != null) {
					String defaultStringValue = value.booleanValue() ? JptCommonUiMessages.BOOLEAN_TRUE : JptCommonUiMessages.BOOLEAN_FALSE;
					return NLS.bind(EclipseLinkUiMessages.PersistenceXmlCustomizationTab_weavingLazyLabelDefault, defaultStringValue);
				}
				return EclipseLinkUiMessages.PersistenceXmlCustomizationTab_weavingLazyLabel;
			}
		};
	}
	
	private PropertyValueModel<Boolean> buildDefaultWeavingLazyHolder() {
		return new PropertyAspectAdapter<Customization, Boolean>(
			getSubjectHolder(),
			Customization.WEAVING_LAZY_PROPERTY)
		{
			@Override
			protected Boolean buildValue_() {
				if (this.subject.getWeavingLazy() != null) {
					return null;
				}
				return this.subject.getDefaultWeavingLazy();
			}
		};
	}


	// ********* weaving fetch groups **********

	private ModifiablePropertyValueModel<Boolean> buildWeavingFetchGroupsHolder() {
		return new PropertyAspectAdapter<Customization, Boolean>(getSubjectHolder(), Customization.WEAVING_FETCH_GROUPS_PROPERTY) {
			@Override
			protected Boolean buildValue_() {
				return this.subject.getWeavingFetchGroups();
			}

			@Override
			protected void setValue_(Boolean value) {
				this.subject.setWeavingFetchGroups(value);
			}
		};
	}

	private PropertyValueModel<String> buildWeavingFetchGroupsStringHolder() {
		return new TransformationPropertyValueModel<Boolean, String>(buildDefaultWeavingFetchGroupsHolder()) {
			@Override
			protected String transform(Boolean value) {
				if (value != null) {
					String defaultStringValue = value.booleanValue() ? JptCommonUiMessages.BOOLEAN_TRUE : JptCommonUiMessages.BOOLEAN_FALSE;
					return NLS.bind(EclipseLinkUiMessages.PersistenceXmlCustomizationTab_weavingFetchGroupsLabelDefault, defaultStringValue);
				}
				return EclipseLinkUiMessages.PersistenceXmlCustomizationTab_weavingFetchGroupsLabel;
			}
		};
	}
	
	private PropertyValueModel<Boolean> buildDefaultWeavingFetchGroupsHolder() {
		return new PropertyAspectAdapter<Customization, Boolean>(
			getSubjectHolder(),
			Customization.WEAVING_FETCH_GROUPS_PROPERTY)
		{
			@Override
			protected Boolean buildValue_() {
				if (this.subject.getWeavingFetchGroups() != null) {
					return null;
				}
				return this.subject.getDefaultWeavingFetchGroups();
			}
		};
	}


	// ********* weaving internal **********
	
	private ModifiablePropertyValueModel<Boolean> buildWeavingInternalHolder() {
		return new PropertyAspectAdapter<Customization, Boolean>(getSubjectHolder(), Customization.WEAVING_INTERNAL_PROPERTY) {
			@Override
			protected Boolean buildValue_() {
				return this.subject.getWeavingInternal();
			}

			@Override
			protected void setValue_(Boolean value) {
				this.subject.setWeavingInternal(value);
			}
		};
	}

	private PropertyValueModel<String> buildWeavingInternalStringHolder() {
		return new TransformationPropertyValueModel<Boolean, String>(buildDefaultWeavingInternalHolder()) {
			@Override
			protected String transform(Boolean value) {
				if (value != null) {
					String defaultStringValue = value.booleanValue() ? JptCommonUiMessages.BOOLEAN_TRUE : JptCommonUiMessages.BOOLEAN_FALSE;
					return NLS.bind(EclipseLinkUiMessages.PersistenceXmlCustomizationTab_weavingInternalLabelDefault, defaultStringValue);
				}
				return EclipseLinkUiMessages.PersistenceXmlCustomizationTab_weavingInternalLabel;
			}
		};
	}
	
	
	private PropertyValueModel<Boolean> buildDefaultWeavingInternalHolder() {
		return new PropertyAspectAdapter<Customization, Boolean>(
			getSubjectHolder(),
			Customization.WEAVING_INTERNAL_PROPERTY)
		{
			@Override
			protected Boolean buildValue_() {
				if (this.subject.getWeavingInternal() != null) {
					return null;
				}
				return this.subject.getDefaultWeavingInternal();
			}
		};
	}


	// ********* weaving eager **********
	
	private ModifiablePropertyValueModel<Boolean> buildWeavingEagerHolder() {
		return new PropertyAspectAdapter<Customization, Boolean>(getSubjectHolder(), Customization.WEAVING_EAGER_PROPERTY) {
			@Override
			protected Boolean buildValue_() {
				return this.subject.getWeavingEager();
			}

			@Override
			protected void setValue_(Boolean value) {
				this.subject.setWeavingEager(value);
			}
		};
	}

	private PropertyValueModel<String> buildWeavingEagerStringHolder() {
		return new TransformationPropertyValueModel<Boolean, String>(buildDefaultWeavingEagerHolder()) {
			@Override
			protected String transform(Boolean value) {
				if (value != null) {
					String defaultStringValue = value.booleanValue() ? JptCommonUiMessages.BOOLEAN_TRUE : JptCommonUiMessages.BOOLEAN_FALSE;
					return NLS.bind(EclipseLinkUiMessages.PersistenceXmlCustomizationTab_weavingEagerLabelDefault, defaultStringValue);
				}
				return EclipseLinkUiMessages.PersistenceXmlCustomizationTab_weavingEagerLabel;
			}
		};
	}
	
	private PropertyValueModel<Boolean> buildDefaultWeavingEagerHolder() {
		return new PropertyAspectAdapter<Customization, Boolean>(
			getSubjectHolder(),
			Customization.WEAVING_EAGER_PROPERTY)
		{
			@Override
			protected Boolean buildValue_() {
				if (this.subject.getWeavingEager() != null) {
					return null;
				}
				return this.subject.getDefaultWeavingEager();
			}
		};
	}


	// ********* weaving change tracking **********
	
	private ModifiablePropertyValueModel<Boolean> buildWeavingChangeTrackingHolder() {
		return new PropertyAspectAdapter<Customization, Boolean>(getSubjectHolder(), Customization.WEAVING_CHANGE_TRACKING_PROPERTY) {
			@Override
			protected Boolean buildValue_() {
				return this.subject.getWeavingChangeTracking();
			}

			@Override
			protected void setValue_(Boolean value) {
				this.subject.setWeavingChangeTracking(value);
			}
		};
	}

	private PropertyValueModel<String> buildWeavingChangeTrackingStringHolder() {
		return new TransformationPropertyValueModel<Boolean, String>(buildDefaultWeavingChangeTrackingHolder()) {
			@Override
			protected String transform(Boolean value) {
				if (value != null) {
					String defaultStringValue = value.booleanValue() ? JptCommonUiMessages.BOOLEAN_TRUE : JptCommonUiMessages.BOOLEAN_FALSE;
					return NLS.bind(EclipseLinkUiMessages.PersistenceXmlCustomizationTab_weavingChangeTrackingLabelDefault, defaultStringValue);
				}
				return EclipseLinkUiMessages.PersistenceXmlCustomizationTab_weavingChangeTrackingLabel;
			}
		};
	}
	
	private PropertyValueModel<Boolean> buildDefaultWeavingChangeTrackingHolder() {
		return new PropertyAspectAdapter<Customization, Boolean>(
			getSubjectHolder(),
			Customization.WEAVING_CHANGE_TRACKING_PROPERTY)
		{
			@Override
			protected Boolean buildValue_() {
				if (this.subject.getWeavingChangeTracking() != null) {
					return null;
				}
				return this.subject.getDefaultWeavingChangeTracking();
			}
		};
	}
}