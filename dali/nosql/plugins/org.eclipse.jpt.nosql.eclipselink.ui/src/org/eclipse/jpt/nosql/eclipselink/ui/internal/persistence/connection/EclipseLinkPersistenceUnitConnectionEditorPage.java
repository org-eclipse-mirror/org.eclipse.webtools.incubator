/*******************************************************************************
 * Copyright (c) 2007, 2013 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.nosql.eclipselink.ui.internal.persistence.connection;

import java.util.Collection;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jpt.common.ui.JptCommonUiMessages;
import org.eclipse.jpt.common.ui.WidgetFactory;
import org.eclipse.jpt.common.ui.internal.widgets.EnumFormComboViewer;
import org.eclipse.jpt.common.ui.internal.widgets.IntegerCombo;
import org.eclipse.jpt.common.ui.internal.widgets.Pane;
import org.eclipse.jpt.common.ui.internal.widgets.TriStateCheckBox;
import org.eclipse.jpt.common.utility.internal.model.value.PropertyAspectAdapter;
import org.eclipse.jpt.common.utility.internal.model.value.TransformationPropertyValueModel;
import org.eclipse.jpt.common.utility.model.value.ModifiablePropertyValueModel;
import org.eclipse.jpt.common.utility.model.value.PropertyValueModel;
import org.eclipse.jpt.jpa.core.context.persistence.PersistenceUnit;
import org.eclipse.jpt.jpa.core.context.persistence.PersistenceUnitTransactionType;
import org.eclipse.jpt.jpa.ui.internal.JpaHelpContextIds;
import org.eclipse.jpt.nosql.eclipselink.core.context.persistence.BatchWriting;
import org.eclipse.jpt.nosql.eclipselink.core.context.persistence.connection.NoSqlConnection;
import org.eclipse.jpt.nosql.eclipselink.ui.internal.EclipseLinkUiMessages;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.Section;

public class EclipseLinkPersistenceUnitConnectionEditorPage
	extends Pane<NoSqlConnection>
{
	public EclipseLinkPersistenceUnitConnectionEditorPage(
		PropertyValueModel<NoSqlConnection> connectionModel,
		Composite parentComposite,
		WidgetFactory widgetFactory,
		ResourceManager resourceManager
	) {
		super(connectionModel, parentComposite, widgetFactory, resourceManager);
	}

	@Override
	protected void initializeLayout(Composite container) {
		Section section = this.getWidgetFactory().createSection(container, ExpandableComposite.TITLE_BAR | Section.DESCRIPTION);
		section.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		section.setText(EclipseLinkUiMessages.PersistenceXmlConnectionTab_sectionTitle);
		section.setDescription(EclipseLinkUiMessages.PersistenceXmlConnectionTab_sectionDescription);

		Composite client = this.getWidgetFactory().createComposite(section);
		GridLayout layout = new GridLayout(2, false);
		layout.marginHeight = 0;
		layout.marginWidth  = 0;
		layout.marginTop    = 0;
		layout.marginLeft   = 0;
		layout.marginBottom = 0;
		layout.marginRight  = 0;
		client.setLayout(layout);
		client.setLayoutData(new GridData(GridData.FILL_BOTH));
		section.setClient(client);

		//transaction type
		this.addLabel(client, EclipseLinkUiMessages.PersistenceXmlConnectionTab_transactionTypeLabel);
		this.addTransactionTypeCombo(client);

		//batch writing
		this.addLabel(client, EclipseLinkUiMessages.PersistenceXmlConnectionTab_batchWritingLabel);
		this.addBatchWritingCombo(client);


		//cache statements
		ModifiablePropertyValueModel<Boolean> cacheStatementsHolder = buildCacheStatementsHolder();
		this.addTriStateCheckBox(
				client,
			EclipseLinkUiMessages.PersistenceXmlConnectionTab_cacheStatementsLabel,
			cacheStatementsHolder,
			JpaHelpContextIds.PERSISTENCE_XML_CONNECTION
		);
		IntegerCombo<?> combo = addCacheStatementsSizeCombo(client);

		this.controlEnabledState(cacheStatementsHolder, combo.getControl());


		TriStateCheckBox nativeSqlCheckBox = this.addTriStateCheckBoxWithDefault(
			client,
			EclipseLinkUiMessages.PersistenceXmlConnectionTab_nativeSqlLabel,
			this.buildNativeSqlHolder(),
			this.buildNativeSqlStringHolder(),
			JpaHelpContextIds.PERSISTENCE_XML_CONNECTION
		);
		GridData gridData = new GridData();
		gridData.horizontalSpan = 2;
		nativeSqlCheckBox.getCheckBox().setLayoutData(gridData);

		ConnectionPropertiesComposite<NoSqlConnection> connectionPropertiesComposite = new ConnectionPropertiesComposite<NoSqlConnection>(this, client);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 2;
		connectionPropertiesComposite.getControl().setLayoutData(gridData);
	}

	private EnumFormComboViewer<PersistenceUnit, PersistenceUnitTransactionType> addTransactionTypeCombo(Composite container) {
		 return new EnumFormComboViewer<PersistenceUnit, PersistenceUnitTransactionType>(this, this.buildPersistenceUnitHolder(), container) {
			@Override
			protected void addPropertyNames(Collection<String> propertyNames) {
				super.addPropertyNames(propertyNames);
				propertyNames.add(PersistenceUnit.SPECIFIED_TRANSACTION_TYPE_PROPERTY);
				propertyNames.add(PersistenceUnit.DEFAULT_TRANSACTION_TYPE_PROPERTY);
			}

			@Override
			protected PersistenceUnitTransactionType[] getChoices() {
				return PersistenceUnitTransactionType.values();
			}

			@Override
			protected PersistenceUnitTransactionType getDefaultValue() {
				return this.getSubject().getDefaultTransactionType();
			}

			@Override
			protected String displayString(PersistenceUnitTransactionType value) {
				switch (value) {
					case JTA :
						return EclipseLinkUiMessages.TransactionTypeComposite_jta;
					case RESOURCE_LOCAL :
						return EclipseLinkUiMessages.TransactionTypeComposite_resource_local;
					default :
						throw new IllegalStateException();
				}
			}

			@Override
			protected PersistenceUnitTransactionType getValue() {
				return this.getSubject().getSpecifiedTransactionType();
			}

			@Override
			protected void setValue(PersistenceUnitTransactionType value) {
				this.getSubject().setSpecifiedTransactionType(value);

				if (value == PersistenceUnitTransactionType.RESOURCE_LOCAL) {
					clearJTAProperties();
				}
				else {
					clearResourceLocalProperties();
				}
			}

			@Override
			protected String getHelpId() {
				return JpaHelpContextIds.PERSISTENCE_XML_CONNECTION;
			}
		};
	}

	private PropertyValueModel<PersistenceUnit> buildPersistenceUnitHolder() {
		return new PropertyAspectAdapter<NoSqlConnection, PersistenceUnit>(getSubjectHolder()) {
			@Override
			protected PersistenceUnit buildValue_() {
				return this.subject.getPersistenceUnit();
			}
		};
	}

	private void clearJTAProperties() {
		getSubject().getPersistenceUnit().setJtaDataSource(null);
	}

	private void clearResourceLocalProperties() {
		NoSqlConnection connection = this.getSubject();
		connection.getPersistenceUnit().setNonJtaDataSource(null);
		connection.setDriver(null);
		connection.setUrl(null);
		connection.setUser(null);
		connection.setPassword(null);
		connection.setBindParameters(null);
		connection.setWriteConnectionsMax(null);
		connection.setWriteConnectionsMin(null);
		connection.setReadConnectionsMax(null);
		connection.setReadConnectionsMin(null);
		connection.setReadConnectionsShared(null);
		connection.setExclusiveConnectionMode(null);
		connection.setLazyConnection(null);
		connection.setConnectionSpec(null);
		// TODO
//		connection.setDatabaseName(null);
//		connection.setHost(null);
//		connection.setPort(null);
	}

	private EnumFormComboViewer<NoSqlConnection, BatchWriting> addBatchWritingCombo(Composite container) {
		return new EnumFormComboViewer<NoSqlConnection, BatchWriting>(this, container) {
			@Override
			protected void addPropertyNames(Collection<String> propertyNames) {
				super.addPropertyNames(propertyNames);
				propertyNames.add(NoSqlConnection.BATCH_WRITING_PROPERTY);
			}

			@Override
			protected BatchWriting[] getChoices() {
				return BatchWriting.values();
			}

			@Override
			protected BatchWriting getDefaultValue() {
				return getSubject().getDefaultBatchWriting();
			}

			@Override
			protected String displayString(BatchWriting value) {
				switch (value) {
					case buffered :
						return EclipseLinkUiMessages.BatchWritingComposite_buffered;
					case jdbc :
						return EclipseLinkUiMessages.BatchWritingComposite_jdbc;
					case none :
						return EclipseLinkUiMessages.BatchWritingComposite_none;
					case oracle_jdbc :
						return EclipseLinkUiMessages.BatchWritingComposite_oracle_jdbc;
					default :
						throw new IllegalStateException();
				}
			}

			@Override
			protected BatchWriting getValue() {
				return getSubject().getBatchWriting();
			}

			@Override
			protected void setValue(BatchWriting value) {
				getSubject().setBatchWriting(value);
			}

			@Override
			protected String getHelpId() {
				return JpaHelpContextIds.PERSISTENCE_XML_CONNECTION;
			}
		};
	}

	private ModifiablePropertyValueModel<Boolean> buildNativeSqlHolder() {
		return new PropertyAspectAdapter<NoSqlConnection, Boolean>(getSubjectHolder(), NoSqlConnection.NATIVE_SQL_PROPERTY) {
			@Override
			protected Boolean buildValue_() {
				return this.subject.getNativeSql();
			}

			@Override
			protected void setValue_(Boolean value) {
				this.subject.setNativeSql(value);
			}

		};
	}

	private PropertyValueModel<String> buildNativeSqlStringHolder() {
		return new TransformationPropertyValueModel<Boolean, String>(buildDefaultNativeSqlHolder()) {
			@Override
			protected String transform(Boolean value) {
				if (value != null) {
					String defaultStringValue = value.booleanValue() ? JptCommonUiMessages.BOOLEAN_TRUE : JptCommonUiMessages.BOOLEAN_FALSE;
					return NLS.bind(EclipseLinkUiMessages.PersistenceXmlConnectionTab_nativeSqlLabelDefault, defaultStringValue);
				}
				return EclipseLinkUiMessages.PersistenceXmlConnectionTab_nativeSqlLabel;
			}
		};
	}

	private PropertyValueModel<Boolean> buildDefaultNativeSqlHolder() {
		return new PropertyAspectAdapter<NoSqlConnection, Boolean>(
			getSubjectHolder(),
			NoSqlConnection.NATIVE_SQL_PROPERTY)
		{
			@Override
			protected Boolean buildValue_() {
				if (this.subject.getNativeSql() != null) {
					return null;
				}
				return this.subject.getDefaultNativeSql();
			}
		};
	}


	private ModifiablePropertyValueModel<Boolean> buildCacheStatementsHolder() {
		return new PropertyAspectAdapter<NoSqlConnection, Boolean>(getSubjectHolder(), NoSqlConnection.CACHE_STATEMENTS_PROPERTY) {
			@Override
			protected Boolean buildValue_() {
				return this.subject.getCacheStatements();
			}

			@Override
			protected void setValue_(Boolean value) {
				this.subject.setCacheStatements(value);
			}

			@Override
			protected synchronized void subjectChanged() {
				Boolean oldValue = this.getValue();
				super.subjectChanged();
				Boolean newValue = this.getValue();

				// Make sure the default value is appended to the text
				if (oldValue == newValue && newValue == null) {
					this.fireAspectChanged(Boolean.TRUE, newValue);
				}
			}
		};
	}

	private IntegerCombo<NoSqlConnection> addCacheStatementsSizeCombo(Composite container) {
		return new IntegerCombo<NoSqlConnection>(this, container) {
			@Override
			protected String getHelpId() {
				return JpaHelpContextIds.PERSISTENCE_XML_CONNECTION;
			}

			@Override
			protected PropertyValueModel<Integer> buildDefaultHolder() {
				return new PropertyAspectAdapter<NoSqlConnection, Integer>(getSubjectHolder()) {
					@Override
					protected Integer buildValue_() {
						return this.subject.getDefaultCacheStatementsSize();
					}
				};
			}

			@Override
			protected ModifiablePropertyValueModel<Integer> buildSelectedItemHolder() {
				return new PropertyAspectAdapter<NoSqlConnection, Integer>(getSubjectHolder(), NoSqlConnection.CACHE_STATEMENTS_SIZE_PROPERTY) {
					@Override
					protected Integer buildValue_() {
						return this.subject.getCacheStatementsSize();
					}

					@Override
					protected void setValue_(Integer value) {
						this.subject.setCacheStatementsSize(value);
					}
				};
			}
		};
	}
}