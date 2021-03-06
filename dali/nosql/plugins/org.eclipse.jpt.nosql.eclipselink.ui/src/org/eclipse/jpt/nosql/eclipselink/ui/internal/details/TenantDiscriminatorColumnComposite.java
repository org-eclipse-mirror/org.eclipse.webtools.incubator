/*******************************************************************************
 * Copyright (c) 2011, 2013 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.nosql.eclipselink.ui.internal.details;

import java.util.Arrays;
import java.util.Collection;

import org.eclipse.jpt.common.ui.JptCommonUiMessages;
import org.eclipse.jpt.common.ui.internal.utility.swt.SWTTools;
import org.eclipse.jpt.common.ui.internal.widgets.ComboPane;
import org.eclipse.jpt.common.ui.internal.widgets.EnumFormComboViewer;
import org.eclipse.jpt.common.ui.internal.widgets.IntegerCombo;
import org.eclipse.jpt.common.ui.internal.widgets.Pane;
import org.eclipse.jpt.common.ui.internal.widgets.TriStateCheckBox;
import org.eclipse.jpt.common.utility.internal.iterable.EmptyIterable;
import org.eclipse.jpt.common.utility.internal.model.value.PropertyAspectAdapter;
import org.eclipse.jpt.common.utility.internal.model.value.TransformationPropertyValueModel;
import org.eclipse.jpt.common.utility.model.value.ModifiablePropertyValueModel;
import org.eclipse.jpt.common.utility.model.value.PropertyValueModel;
import org.eclipse.jpt.jpa.core.context.DiscriminatorType;
import org.eclipse.jpt.jpa.core.context.ReadOnlyNamedColumn;
import org.eclipse.jpt.jpa.core.context.ReadOnlyNamedDiscriminatorColumn;
import org.eclipse.jpt.jpa.core.context.ReadOnlyTableColumn;
import org.eclipse.jpt.jpa.db.Table;
import org.eclipse.jpt.jpa.ui.JptJpaUiMessages;
import org.eclipse.jpt.jpa.ui.details.JptJpaUiDetailsMessages;
import org.eclipse.jpt.jpa.ui.internal.details.db.ColumnCombo;
import org.eclipse.jpt.jpa.ui.internal.details.db.DatabaseObjectCombo;
import org.eclipse.jpt.nosql.eclipselink.core.context.ReadOnlyTenantDiscriminatorColumn2_3;
import org.eclipse.jpt.nosql.eclipselink.core.context.TenantDiscriminatorColumn2_3;
import org.eclipse.jpt.nosql.eclipselink.ui.internal.EclipseLinkHelpContextIds;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

public class TenantDiscriminatorColumnComposite extends Pane<ReadOnlyTenantDiscriminatorColumn2_3> {

	public TenantDiscriminatorColumnComposite(Pane<?> parentPane,
	                                   PropertyValueModel<ReadOnlyTenantDiscriminatorColumn2_3> subjectHolder,
	                                   Composite parent) {

		super(parentPane, subjectHolder, parent);
	}

	@Override
	protected Composite addComposite(Composite container) {
		return this.addSubPane(container, 2, 0, 0, 0, 0);
	}

	@Override
	protected void initializeLayout(Composite container) {
		// Name widgets
		this.addLabel(container, EclipseLinkUiDetailsMessages.TenantDiscriminatorColumnComposite_nameLabel);
		this.addNameCombo(container);

		// Table widgets
		this.addLabel(container, EclipseLinkUiDetailsMessages.TenantDiscriminatorColumnComposite_tableLabel);
		this.addTableCombo(container);

		// Context property widgets
		this.addLabel(container, EclipseLinkUiDetailsMessages.TenantDiscriminatorColumnComposite_contextPropertyLabel);
		this.addContextPropertyCombo(container);

		// Discriminator Type widgets
		this.addLabel(container, EclipseLinkUiDetailsMessages.TenantDiscriminatorColumnComposite_discriminatorTypeLabel);
		this.addDiscriminatorTypeCombo(container);

		// Length widgets
		this.addLabel(container, JptJpaUiDetailsMessages.ColumnComposite_length);
		this.addLengthCombo(container);

		// Column Definition widgets
		this.addLabel(container, EclipseLinkUiDetailsMessages.TenantDiscriminatorColumnComposite_columnDefinitionLabel);
		this.addText(container, this.buildColumnDefinitionHolder(getSubjectHolder()));

		// Primary key tri-state check box
		TriStateCheckBox pkCheckBox = addTriStateCheckBoxWithDefault(
			container,
			EclipseLinkUiDetailsMessages.TenantDiscriminatorColumnComposite_primaryKey,
			buildPrimaryKeyHolder(),
			buildPrimaryKeyStringHolder(),
			EclipseLinkHelpContextIds.TENANT_DISCRIMINATOR_COLUMN_PRIMARY_KEY);

		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 2;
		pkCheckBox.getCheckBox().setLayoutData(gridData);
		SWTTools.controlVisibleState(new EclipseLink2_4ProjectFlagModel<ReadOnlyTenantDiscriminatorColumn2_3>(this.getSubjectHolder()), pkCheckBox.getCheckBox());
	}

	private ColumnCombo<ReadOnlyTenantDiscriminatorColumn2_3> addNameCombo(Composite container) {

		return new ColumnCombo<ReadOnlyTenantDiscriminatorColumn2_3>(this, container) {
			@Override
			protected void addPropertyNames(Collection<String> propertyNames) {
				super.addPropertyNames(propertyNames);
				propertyNames.add(ReadOnlyNamedColumn.DEFAULT_NAME_PROPERTY);
				propertyNames.add(ReadOnlyNamedColumn.SPECIFIED_NAME_PROPERTY);
				propertyNames.addAll(COLUMN_PICK_LIST_PROPERTIES);
			}

			@Override
			protected void propertyChanged(String propertyName) {
				if (COLUMN_PICK_LIST_PROPERTIES.contains(propertyName)) {
					this.repopulateComboBox();
				} else {
					super.propertyChanged(propertyName);
				}
			}

			@Override
			protected String getDefaultValue() {
				return getSubject().getDefaultName();
			}

			@Override
			protected void setValue(String value) {
				((TenantDiscriminatorColumn2_3) this.getSubject()).setSpecifiedName(value);
			}

			@Override
			protected Table getDbTable_() {
				ReadOnlyTenantDiscriminatorColumn2_3 column = this.getSubject();
				return (column == null) ? null : column.getDbTable();
			}

			@Override
			protected String getValue() {
				return getSubject().getSpecifiedName();
			}

			@Override
			protected String buildNullDefaultValueEntry() {
				return NLS.bind(
						JptCommonUiMessages.DEFAULT_WITH_ONE_PARAM,
						JptCommonUiMessages.NONE_SELECTED);
			}

			@Override
			protected String getHelpId() {
				return EclipseLinkHelpContextIds.TENANT_DISCRIMINATOR_COLUMN_NAME;
			}

			@Override
			public String toString() {
				return "TenantDiscriminatorColumnComposite.nameCombo"; //$NON-NLS-1$
			}
		};
	}

	/* CU private */ static final Collection<String> COLUMN_PICK_LIST_PROPERTIES = Arrays.asList(new String[] {
		ReadOnlyTableColumn.DEFAULT_TABLE_NAME_PROPERTY,
		ReadOnlyTableColumn.SPECIFIED_TABLE_NAME_PROPERTY
	});

	private Pane<ReadOnlyTenantDiscriminatorColumn2_3> addTableCombo(Composite container) {

		return new DatabaseObjectCombo<ReadOnlyTenantDiscriminatorColumn2_3>(this, container) {

			@Override
			protected void addPropertyNames(Collection<String> propertyNames) {
				super.addPropertyNames(propertyNames);
				propertyNames.add(ReadOnlyTableColumn.DEFAULT_TABLE_NAME_PROPERTY);
				propertyNames.add(ReadOnlyTableColumn.SPECIFIED_TABLE_NAME_PROPERTY);
			}

			@Override
			protected String getDefaultValue() {
				return this.getSubject().getDefaultTableName();
			}

			@Override
			protected void setValue(String value) {
				((TenantDiscriminatorColumn2_3) this.getSubject()).setSpecifiedTableName(value);
			}

			@Override
			protected String getValue() {
				return this.getSubject().getSpecifiedTableName();
			}

			// TODO we need to listen for this list to change...
			@Override
			protected Iterable<String> getValues_() {
				ReadOnlyTenantDiscriminatorColumn2_3 column = this.getSubject();
				return (column != null) ? column.getCandidateTableNames() : EmptyIterable.<String> instance();
			}

			@Override
			protected String buildNullDefaultValueEntry() {
				return NLS.bind(
						JptCommonUiMessages.DEFAULT_WITH_ONE_PARAM,
						JptCommonUiMessages.NONE_SELECTED);
			}

			@Override
			protected String getHelpId() {
				return EclipseLinkHelpContextIds.TENANT_DISCRIMINATOR_COLUMN_TABLE;
			}

			@Override
			public String toString() {
				return "TenantDiscriminatorColumnComposite.tableCombo"; //$NON-NLS-1$
			}
		};
	}

	private Pane<ReadOnlyTenantDiscriminatorColumn2_3> addContextPropertyCombo(Composite container) {

		return new ComboPane<ReadOnlyTenantDiscriminatorColumn2_3>(this, container) {

			@Override
			protected void addPropertyNames(Collection<String> propertyNames) {
				super.addPropertyNames(propertyNames);
				propertyNames.add(ReadOnlyTenantDiscriminatorColumn2_3.DEFAULT_CONTEXT_PROPERTY);
				propertyNames.add(ReadOnlyTenantDiscriminatorColumn2_3.SPECIFIED_CONTEXT_PROPERTY_PROPERTY);
			}

			@Override
			protected String getDefaultValue() {
				return this.getSubject().getDefaultContextProperty();
			}

			@Override
			protected void setValue(String value) {
				((TenantDiscriminatorColumn2_3) this.getSubject()).setSpecifiedContextProperty(value);
			}

			@Override
			protected String getValue() {
				return this.getSubject().getSpecifiedContextProperty();
			}

			@Override
			protected Iterable<String> getValues() {
				return EmptyIterable.<String> instance();
			}

			@Override
			protected String buildNullDefaultValueEntry() {
				return NLS.bind(
						JptCommonUiMessages.DEFAULT_WITH_ONE_PARAM,
						JptCommonUiMessages.NONE_SELECTED);
			}

			@Override
			public String toString() {
				return "TenantDiscriminatorColumnComposite.contextPropertyCombo"; //$NON-NLS-1$
			}

			@Override
			protected String getHelpId() {
				return EclipseLinkHelpContextIds.TENANT_DISCRIMINATOR_COLUMN_CONTEXT_PROPERTY;
			}
		};
	}

	private EnumFormComboViewer<ReadOnlyTenantDiscriminatorColumn2_3, DiscriminatorType> addDiscriminatorTypeCombo(Composite container) {

		return new EnumFormComboViewer<ReadOnlyTenantDiscriminatorColumn2_3, DiscriminatorType>(
			this,
			getSubjectHolder(),
			container)
		{
			@Override
			protected void addPropertyNames(Collection<String> propertyNames) {
				super.addPropertyNames(propertyNames);
				propertyNames.add(ReadOnlyNamedDiscriminatorColumn.DEFAULT_DISCRIMINATOR_TYPE_PROPERTY);
				propertyNames.add(ReadOnlyNamedDiscriminatorColumn.SPECIFIED_DISCRIMINATOR_TYPE_PROPERTY);
			}

			@Override
			protected DiscriminatorType[] getChoices() {
				return DiscriminatorType.values();
			}

			@Override
			protected DiscriminatorType getDefaultValue() {
				return getSubject().getDefaultDiscriminatorType();
			}

			@Override
			protected String displayString(DiscriminatorType value) {
				switch (value) {
					case CHAR :
						return JptJpaUiDetailsMessages.DiscriminatorColumnComposite_char;
					case INTEGER :
						return JptJpaUiDetailsMessages.DiscriminatorColumnComposite_integer;
					case STRING :
						return JptJpaUiDetailsMessages.DiscriminatorColumnComposite_string;
					default :
						throw new IllegalStateException();
				}
			}

			@Override
			protected String nullDisplayString() {
				return JptCommonUiMessages.NONE_SELECTED;
			}

			@Override
			protected DiscriminatorType getValue() {
				return getSubject().getSpecifiedDiscriminatorType();
			}

			@Override
			protected void setValue(DiscriminatorType value) {
				((TenantDiscriminatorColumn2_3) this.getSubject()).setSpecifiedDiscriminatorType(value);
			}

			@Override
			protected String getHelpId() {
				return EclipseLinkHelpContextIds.TENANT_DISCRIMINATOR_COLUMN_DISCRIMINATOR_TYPE;
			}
		};
	}

	private void addLengthCombo(Composite container) {
		new IntegerCombo<ReadOnlyTenantDiscriminatorColumn2_3>(this, container) {

			@Override
			protected String getHelpId() {
				return EclipseLinkHelpContextIds.TENANT_DISCRIMINATOR_COLUMN_LENGTH;
			}

			@Override
			protected PropertyValueModel<Integer> buildDefaultHolder() {
				return new PropertyAspectAdapter<ReadOnlyTenantDiscriminatorColumn2_3, Integer>(getSubjectHolder(), ReadOnlyNamedDiscriminatorColumn.DEFAULT_LENGTH_PROPERTY) {
					@Override
					protected Integer buildValue_() {
						return Integer.valueOf(this.subject.getDefaultLength());
					}
				};
			}

			@Override
			protected ModifiablePropertyValueModel<Integer> buildSelectedItemHolder() {
				return new PropertyAspectAdapter<ReadOnlyTenantDiscriminatorColumn2_3, Integer>(getSubjectHolder(), ReadOnlyNamedDiscriminatorColumn.SPECIFIED_LENGTH_PROPERTY) {
					@Override
					protected Integer buildValue_() {
						return this.subject.getSpecifiedLength();
					}

					@Override
					protected void setValue_(Integer value) {
						((TenantDiscriminatorColumn2_3) this.subject).setSpecifiedLength(value);
					}
				};
			}
		};
	}

	private ModifiablePropertyValueModel<String> buildColumnDefinitionHolder(PropertyValueModel<? extends ReadOnlyTenantDiscriminatorColumn2_3> discriminatorColumnHolder) {
		return new PropertyAspectAdapter<ReadOnlyTenantDiscriminatorColumn2_3, String>(discriminatorColumnHolder, ReadOnlyNamedColumn.COLUMN_DEFINITION_PROPERTY) {
			@Override
			protected String buildValue_() {
				return this.subject.getColumnDefinition();
			}
			@Override
			protected void setValue_(String value) {
				if (value.length() == 0) {
					value = null;
				}
				((TenantDiscriminatorColumn2_3) this.subject).setColumnDefinition(value);
			}
		};
	}

	ModifiablePropertyValueModel<Boolean> buildPrimaryKeyHolder() {
		return new PropertyAspectAdapter<ReadOnlyTenantDiscriminatorColumn2_3, Boolean>(getSubjectHolder(), ReadOnlyTenantDiscriminatorColumn2_3.SPECIFIED_PRIMARY_KEY_PROPERTY) {
			@Override
			protected Boolean buildValue_() {
				return this.subject.getSpecifiedPrimaryKey();
			}

			@Override
			protected void setValue_(Boolean value) {
				((TenantDiscriminatorColumn2_3) this.subject).setSpecifiedPrimaryKey(value);
			}
		};
	}

	PropertyValueModel<String> buildPrimaryKeyStringHolder() {
		return new TransformationPropertyValueModel<Boolean, String>(buildDefaultPrimaryKeyHolder()) {
			@Override
			protected String transform(Boolean value) {
				if (value != null) {
					String defaultStringValue = value.booleanValue() ? JptCommonUiMessages.BOOLEAN_TRUE : JptCommonUiMessages.BOOLEAN_FALSE;
					return NLS.bind(EclipseLinkUiDetailsMessages.TenantDiscriminatorColumnComposite_primaryKeyWithDefault, defaultStringValue);
				}
				return EclipseLinkUiDetailsMessages.TenantDiscriminatorColumnComposite_primaryKey;
			}
		};
	}

	PropertyValueModel<Boolean> buildDefaultPrimaryKeyHolder() {
		return new PropertyAspectAdapter<ReadOnlyTenantDiscriminatorColumn2_3, Boolean>(
				getSubjectHolder(),
				ReadOnlyTenantDiscriminatorColumn2_3.SPECIFIED_PRIMARY_KEY_PROPERTY,
				ReadOnlyTenantDiscriminatorColumn2_3.DEFAULT_PRIMARY_KEY_PROPERTY) {

			@Override
			protected Boolean buildValue_() {
				if (this.subject.getSpecifiedPrimaryKey() != null) {
					return null;
				}
				return Boolean.valueOf(this.subject.isPrimaryKey());
			}
		};
	}
}
