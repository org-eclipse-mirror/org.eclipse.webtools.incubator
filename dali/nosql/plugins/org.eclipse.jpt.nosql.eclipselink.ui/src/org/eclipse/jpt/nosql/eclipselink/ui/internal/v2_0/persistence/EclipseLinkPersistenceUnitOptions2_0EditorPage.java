/*******************************************************************************
 * Copyright (c) 2008, 2013 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.nosql.eclipselink.ui.internal.v2_0.persistence;

import java.util.Collection;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.ui.IJavaElementSearchConstants;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.jpt.common.ui.JptCommonUiMessages;
import org.eclipse.jpt.common.ui.WidgetFactory;
import org.eclipse.jpt.common.ui.internal.widgets.AddRemoveListPane;
import org.eclipse.jpt.common.ui.internal.widgets.AddRemovePane.Adapter;
import org.eclipse.jpt.common.ui.internal.widgets.EnumFormComboViewer;
import org.eclipse.jpt.common.ui.internal.widgets.IntegerCombo;
import org.eclipse.jpt.common.ui.internal.widgets.TriStateCheckBox;
import org.eclipse.jpt.common.utility.internal.model.value.ListAspectAdapter;
import org.eclipse.jpt.common.utility.internal.model.value.PropertyAspectAdapter;
import org.eclipse.jpt.common.utility.internal.model.value.SimpleCollectionValueModel;
import org.eclipse.jpt.common.utility.internal.model.value.TransformationPropertyValueModel;
import org.eclipse.jpt.common.utility.iterable.ListIterable;
import org.eclipse.jpt.common.utility.model.value.CollectionValueModel;
import org.eclipse.jpt.common.utility.model.value.ListValueModel;
import org.eclipse.jpt.common.utility.model.value.ModifiableCollectionValueModel;
import org.eclipse.jpt.common.utility.model.value.ModifiablePropertyValueModel;
import org.eclipse.jpt.common.utility.model.value.PropertyValueModel;
import org.eclipse.jpt.jpa.core.context.persistence.PersistenceUnit;
import org.eclipse.jpt.jpa.core.jpa2.context.persistence.PersistenceUnit2_0;
import org.eclipse.jpt.jpa.core.jpa2.context.persistence.options.JpaOptions2_0;
import org.eclipse.jpt.jpa.core.jpa2.context.persistence.options.ValidationMode;
import org.eclipse.jpt.jpa.ui.jpa2.persistence.JptJpaUiPersistenceMessages2_0;
import org.eclipse.jpt.nosql.eclipselink.core.context.persistence.EclipseLinkPersistenceUnit;
import org.eclipse.jpt.nosql.eclipselink.core.context.persistence.Logging2_0;
import org.eclipse.jpt.nosql.eclipselink.core.context.persistence.Options2_0;
import org.eclipse.jpt.nosql.eclipselink.ui.internal.EclipseLinkHelpContextIds;
import org.eclipse.jpt.nosql.eclipselink.ui.internal.EclipseLinkUiMessages;
import org.eclipse.jpt.nosql.eclipselink.ui.internal.persistence.options.EclipseLinkPersistenceUnitOptionsEditorPage;
import org.eclipse.jpt.nosql.eclipselink.ui.internal.plugin.JptNoSQLEclipseLinkUiPlugin;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.SelectionDialog;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.progress.IProgressService;

class EclipseLinkPersistenceUnitOptions2_0EditorPage 
	extends EclipseLinkPersistenceUnitOptionsEditorPage {

	private PropertyValueModel<Options2_0> optionsModel;

	public EclipseLinkPersistenceUnitOptions2_0EditorPage(
			PropertyValueModel<PersistenceUnit> persistenceUnitModel,
            Composite parentComposite,
            WidgetFactory widgetFactory,
            ResourceManager resourceManager) {
		super(persistenceUnitModel, parentComposite, widgetFactory, resourceManager);
	}

	@Override
	protected Control initializeMiscellaneousSection(Section section) {	
		Composite container = this.addSubPane(section, 2, 0, 0, 0, 0);

		TriStateCheckBox checkBox = this.addTriStateCheckBoxWithDefault(
			container,
			EclipseLinkUiMessages.PersistenceXmlOptionsTab_temporalMutableLabel,
			this.buildTemporalMutableHolder(),
			this.buildTemporalMutableStringHolder(),
			EclipseLinkHelpContextIds.PERSISTENCE_OPTIONS
		);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 2;
		checkBox.getCheckBox().setLayoutData(gridData);

		this.optionsModel = this.buildOptions2_0Model();
		this.addLabel(container, JptJpaUiPersistenceMessages2_0.LOCKING_CONFIGURATION_COMPOSITE_LOCK_TIMEOUT_LABEL);
		this.addLockTimeoutCombo(container);

		this.addLabel(container, JptJpaUiPersistenceMessages2_0.QUERY_CONFIGURATION_COMPOSITE_QUERY_TIMEOUT_LABEL);
		this.addQueryTimeoutCombo(container);


		// ValidationMode
		this.addLabel(container, JptJpaUiPersistenceMessages2_0.VALIDATION_MODE_COMPOSITE_VALIDATION_MODE_LABEL);
		this.addValidationModeCombo(container);

		// ValidationGroupPrePersist
		this.addLabel(container, JptJpaUiPersistenceMessages2_0.VALIDATION_CONFIGURATION_COMPOSITE_GROUP_PRE_PERSIST_LABEL);
		this.addPrePersistListPane(container);

		// ValidationGroupPreUpdate
		this.addLabel(container, JptJpaUiPersistenceMessages2_0.VALIDATION_CONFIGURATION_COMPOSITE_GROUP_PRE_UPDATE_LABEL);
		this.addPreUpdateListPane(container);

		// ValidationGroupPreRemove
		this.addLabel(container, JptJpaUiPersistenceMessages2_0.VALIDATION_CONFIGURATION_COMPOSITE_GROUP_PRE_REMOVE_LABEL);
		this.addPreRemoveListPane(container);

		return container;
	}

	@Override
	protected Control initializeLoggingSection(Section section) {			
		return new EclipseLinkLogging2_0Composite(this, this.buildLoggingModel(), section).getControl();
	}

	protected PropertyValueModel<Logging2_0> buildLoggingModel() {
		return new TransformationPropertyValueModel<PersistenceUnit, Logging2_0>(getSubjectHolder()) {
			@Override
			protected Logging2_0 transform_(PersistenceUnit value) {
				return (Logging2_0) ((EclipseLinkPersistenceUnit) value).getLogging();
			}
		};
	}

	private void addLockTimeoutCombo(Composite container) {
		new IntegerCombo<Options2_0>(this, this.optionsModel, container) {
			@Override
			protected String getHelpId() {
				return null; // TODO 
			}
			@Override
			protected PropertyValueModel<Integer> buildDefaultHolder() {
				return new PropertyAspectAdapter<Options2_0, Integer>(this.getSubjectHolder()) {
					@Override
					protected Integer buildValue_() {
						return this.subject.getDefaultLockTimeout();
					}
				};
			}

			@Override
			protected ModifiablePropertyValueModel<Integer> buildSelectedItemHolder() {
				return new PropertyAspectAdapter<Options2_0, Integer>(this.getSubjectHolder(), Options2_0.LOCK_TIMEOUT_PROPERTY) {
					@Override
					protected Integer buildValue_() {
						return this.subject.getLockTimeout();
					}

					@Override
					protected void setValue_(Integer value) {
						this.subject.setLockTimeout(value);
					}
				};
			}
		};
	}

	private void addQueryTimeoutCombo(Composite container) {
		new IntegerCombo<Options2_0>(this, this.optionsModel, container) {
			@Override
			protected String getHelpId() {
				return null;		// TODO
			}

			@Override
			protected PropertyValueModel<Integer> buildDefaultHolder() {
				return new PropertyAspectAdapter<Options2_0, Integer>(this.getSubjectHolder()) {
					@Override
					protected Integer buildValue_() {
						return this.subject.getDefaultQueryTimeout();
					}
				};
			}

			@Override
			protected ModifiablePropertyValueModel<Integer> buildSelectedItemHolder() {
				return new PropertyAspectAdapter<Options2_0, Integer>(this.getSubjectHolder(), Options2_0.QUERY_TIMEOUT_PROPERTY) {
					@Override
					protected Integer buildValue_() {
						return this.subject.getQueryTimeout();
					}

					@Override
					protected void setValue_(Integer value) {
						this.subject.setQueryTimeout(value);
					}
				};
			}
		};
	}

	private EnumFormComboViewer<PersistenceUnit, ValidationMode> addValidationModeCombo(Composite parent) {
		return new EnumFormComboViewer<PersistenceUnit, ValidationMode>(this, this.getSubjectHolder(), parent) {
			@Override
			protected void addPropertyNames(Collection<String> propertyNames) {
				super.addPropertyNames(propertyNames);
				propertyNames.add(PersistenceUnit2_0.SPECIFIED_VALIDATION_MODE_PROPERTY);
			}

			@Override
			protected ValidationMode[] getChoices() {
				return ValidationMode.values();
			}

			@Override
			protected boolean sortChoices() {
				return false;
			}

			@Override
			protected ValidationMode getDefaultValue() {
				return ((PersistenceUnit2_0) this.getSubject()).getDefaultValidationMode();
			}

			@Override
			protected String displayString(ValidationMode value) {
				switch (value) {
					case AUTO :
						return JptJpaUiPersistenceMessages2_0.VALIDATION_MODE_COMPOSITE_AUTO;
					case CALLBACK :
						return JptJpaUiPersistenceMessages2_0.VALIDATION_MODE_COMPOSITE_CALLBACK;
					case NONE :
						return JptJpaUiPersistenceMessages2_0.VALIDATION_MODE_COMPOSITE_NONE;
					default :
						throw new IllegalStateException();
				}
			}

			@Override
			protected ValidationMode getValue() {
				return ((PersistenceUnit2_0) this.getSubject()).getSpecifiedValidationMode();
			}

			@Override
			protected void setValue(ValidationMode value) {
				((PersistenceUnit2_0) this.getSubject()).setSpecifiedValidationMode(value);
			}
		};
	}

	// ********** ValidationGroupPrePersists **********

	private void addPrePersistListPane(Composite parent) {
		new AddRemoveListPane<Options2_0, String>(
			this,
			this.optionsModel,
			parent,
			this.buildPrePersistAdapter(),
			this.buildPrePersistListModel(),
			this.buildSelectedItemsModel(),
			this.buildLabelProvider()
		);
	}

	private Adapter<String> buildPrePersistAdapter() {
		return new AddRemoveListPane.AbstractAdapter<String>() {
			public String addNewItem() {
				return addPrePersistClass();
			}

			@Override
			public PropertyValueModel<Boolean> buildRemoveButtonEnabledModel(CollectionValueModel<String> selectedItemsModel) {
				//enable the remove button only when 1 item is selected, same as the optional button
				return this.buildSingleSelectedItemEnabledModel(selectedItemsModel);
			}

			public void removeSelectedItems(CollectionValueModel<String> selectedItemsModel) {
				String item = selectedItemsModel.iterator().next();
				optionsModel.getValue().removeValidationGroupPrePersist(item);
			}
		};
	}

	private ListValueModel<String> buildPrePersistListModel() {
		return new ListAspectAdapter<Options2_0, String>(this.optionsModel, JpaOptions2_0.VALIDATION_GROUP_PRE_PERSIST_LIST) {
			@Override
			protected ListIterable<String> getListIterable() {
				return subject.getValidationGroupPrePersists();
			}

			@Override
			protected int size_() {
				return subject.getValidationGroupPrePersistsSize();
			}
		};
	}

	private String addPrePersistClass() {
		IType type = this.chooseType();

		if (type != null) {
			String className = type.getFullyQualifiedName('$');
			if( ! this.optionsModel.getValue().validationGroupPrePersistExists(className)) {
				return this.optionsModel.getValue().addValidationGroupPrePersist(className);
			}
		}
		return null;
	}

	// ********** ValidationGroupPreUpdates **********

	private void addPreUpdateListPane(Composite parent) {
		new AddRemoveListPane<Options2_0, String>(
			this,
			this.optionsModel,
			parent,
			this.buildPreUpdateAdapter(),
			this.buildPreUpdateListHolder(),
			this.buildSelectedItemsModel(),
			this.buildLabelProvider()
		);
	}

	private Adapter<String> buildPreUpdateAdapter() {
		return new AddRemoveListPane.AbstractAdapter<String>() {
			public String addNewItem() {
				return addPreUpdateClass();
			}

			@Override
			public PropertyValueModel<Boolean> buildRemoveButtonEnabledModel(CollectionValueModel<String> selectedItemsModel) {
				//enable the remove button only when 1 item is selected, same as the optional button
				return this.buildSingleSelectedItemEnabledModel(selectedItemsModel);
			}

			public void removeSelectedItems(CollectionValueModel<String> selectedItemsModel) {
				String item = selectedItemsModel.iterator().next();
				optionsModel.getValue().removeValidationGroupPreUpdate(item);
			}
		};
	}

	private ListValueModel<String> buildPreUpdateListHolder() {
		return new ListAspectAdapter<Options2_0, String>(this.optionsModel, Options2_0.VALIDATION_GROUP_PRE_UPDATE_LIST) {
			@Override
			protected ListIterable<String> getListIterable() {
				return subject.getValidationGroupPreUpdates();
			}

			@Override
			protected int size_() {
				return subject.getValidationGroupPreUpdatesSize();
			}
		};
	}

	private String addPreUpdateClass() {
		IType type = this.chooseType();

		if (type != null) {
			String className = type.getFullyQualifiedName('$');
			if( ! this.optionsModel.getValue().validationGroupPreUpdateExists(className)) {
				return this.optionsModel.getValue().addValidationGroupPreUpdate(className);
			}
		}
		return null;
	}

	// ********** ValidationGroupPreRemoves **********

	private void addPreRemoveListPane(Composite parent) {
		new AddRemoveListPane<Options2_0, String>(
			this,
			this.optionsModel,
			parent,
			this.buildPreRemoveAdapter(),
			this.buildPreRemoveListHolder(),
			this.buildSelectedItemsModel(),
			this.buildLabelProvider()
		);
	}

	private Adapter<String> buildPreRemoveAdapter() {
		return new AddRemoveListPane.AbstractAdapter<String>() {
			public String addNewItem() {
				return addPreRemoveClass();
			}

			@Override
			public PropertyValueModel<Boolean> buildRemoveButtonEnabledModel(CollectionValueModel<String> selectedItemsModel) {
				//enable the remove button only when 1 item is selected, same as the optional button
				return this.buildSingleSelectedItemEnabledModel(selectedItemsModel);
			}

			public void removeSelectedItems(CollectionValueModel<String> selectedItemsModel) {
				String item = selectedItemsModel.iterator().next();
				optionsModel.getValue().removeValidationGroupPreRemove(item);
			}
		};
	}

	private ListValueModel<String> buildPreRemoveListHolder() {
		return new ListAspectAdapter<Options2_0, String>(this.optionsModel, JpaOptions2_0.VALIDATION_GROUP_PRE_REMOVE_LIST) {
			@Override
			protected ListIterable<String> getListIterable() {
				return subject.getValidationGroupPreRemoves();
			}

			@Override
			protected int size_() {
				return subject.getValidationGroupPreRemovesSize();
			}
		};
	}

	private String addPreRemoveClass() {

		IType type = this.chooseType();

		if (type != null) {
			String className = type.getFullyQualifiedName('$');
			if( ! this.optionsModel.getValue().validationGroupPreRemoveExists(className)) {
				return this.optionsModel.getValue().addValidationGroupPreRemove(className);
			}
		}
		return null;
	}


	// ********** Private methods **********

	private ILabelProvider buildLabelProvider() {
		return new LabelProvider() {

			@Override
			public String getText(Object element) {
				String name = (String) element;

				if (name == null) {
					name = EclipseLinkUiMessages.PersistenceXmlOptionsTab_noName;
				}
				return name;
			}
		};
	}

	/**
	 * Prompts the user the Open Type dialog.
	 *
	 * @return Either the selected type or <code>null</code> if the user
	 * canceled the dialog
	 */
	private IType chooseType() {
		IJavaProject javaProject = this.getSubject().getJpaProject().getJavaProject();
		IJavaElement[] elements = new IJavaElement[] { javaProject };
		IJavaSearchScope scope = SearchEngine.createJavaSearchScope(elements);
		IProgressService service = PlatformUI.getWorkbench().getProgressService();
		SelectionDialog typeSelectionDialog;

		try {
			typeSelectionDialog = JavaUI.createTypeDialog(
				getShell(),
				service,
				scope,
				IJavaElementSearchConstants.CONSIDER_CLASSES,
				false,
				""
			);
		}
		catch (JavaModelException e) {
			JptNoSQLEclipseLinkUiPlugin.instance().logError(e);
			return null;
		}

		typeSelectionDialog.setTitle(JptCommonUiMessages.CLASS_CHOOSER_PANE__DIALOG_TITLE);
		typeSelectionDialog.setMessage(JptCommonUiMessages.CLASS_CHOOSER_PANE__DIALOG_MESSAGE);

		if (typeSelectionDialog.open() == Window.OK) {
			return (IType) typeSelectionDialog.getResult()[0];
		}

		return null;
	}

	private ModifiableCollectionValueModel<String> buildSelectedItemsModel() {
		return new SimpleCollectionValueModel<String>();
	}

	private PropertyValueModel<Options2_0> buildOptions2_0Model() {
		return new TransformationPropertyValueModel<PersistenceUnit, Options2_0>(getSubjectHolder()) {
			@Override
			protected Options2_0 transform_(PersistenceUnit value) {
				return (Options2_0) ((PersistenceUnit2_0) value).getOptions();
			}
		};
	}
}