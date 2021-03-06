/*******************************************************************************
 * Copyright (c) 2011, 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.nosql.eclipselink.ui.internal.details;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jpt.common.ui.internal.util.ControlSwitcher;
import org.eclipse.jpt.common.ui.internal.widgets.AddRemoveListPane;
import org.eclipse.jpt.common.ui.internal.widgets.AddRemovePane.AbstractAdapter;
import org.eclipse.jpt.common.ui.internal.widgets.AddRemovePane.Adapter;
import org.eclipse.jpt.common.ui.internal.widgets.Pane;
import org.eclipse.jpt.common.utility.internal.model.value.CollectionPropertyValueModelAdapter;
import org.eclipse.jpt.common.utility.internal.model.value.CompositeListValueModel;
import org.eclipse.jpt.common.utility.internal.model.value.ItemPropertyListValueModelAdapter;
import org.eclipse.jpt.common.utility.internal.model.value.ListAspectAdapter;
import org.eclipse.jpt.common.utility.internal.model.value.SimpleCollectionValueModel;
import org.eclipse.jpt.common.utility.iterable.ListIterable;
import org.eclipse.jpt.common.utility.model.value.CollectionValueModel;
import org.eclipse.jpt.common.utility.model.value.ListValueModel;
import org.eclipse.jpt.common.utility.model.value.ModifiableCollectionValueModel;
import org.eclipse.jpt.common.utility.model.value.PropertyValueModel;
import org.eclipse.jpt.common.utility.transformer.Transformer;
import org.eclipse.jpt.jpa.core.JpaNode;
import org.eclipse.jpt.jpa.core.context.ReadOnlyNamedColumn;
import org.eclipse.jpt.nosql.eclipselink.core.context.ReadOnlyTenantDiscriminatorColumn2_3;
import org.eclipse.jpt.nosql.eclipselink.core.context.TenantDiscriminatorColumn2_3;
import org.eclipse.jpt.nosql.eclipselink.ui.internal.EclipseLinkHelpContextIds;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.part.PageBook;

public class TenantDiscriminatorColumnsComposite<T extends JpaNode>
	extends Pane<T>
{
	/**
	 * The editor used to perform the common behaviors defined in the list pane.
	 */
	TenantDiscriminatorColumnsEditor<T> tenantDiscriminatorColumnsEditor;

	private Pane<ReadOnlyTenantDiscriminatorColumn2_3> tenantDiscriminatorColumnPane;
	private ModifiableCollectionValueModel<ReadOnlyTenantDiscriminatorColumn2_3> selectedTenantDiscriminatorColumnsModel;
	private PropertyValueModel<ReadOnlyTenantDiscriminatorColumn2_3> selectedTenantDiscriminatorColumnModel;


	public TenantDiscriminatorColumnsComposite(
			Pane<?> parent,
			PropertyValueModel<? extends T> subjectModel,
			PropertyValueModel<Boolean> enabledModel,
			Composite parentComposite,
			TenantDiscriminatorColumnsEditor<T> tenantDiscriminatorColumnsEditor) {
		super(parent, subjectModel, enabledModel, parentComposite);
		this.tenantDiscriminatorColumnsEditor = tenantDiscriminatorColumnsEditor;
		initializeLayout2();
	}

	@Override
	protected void initialize() {
		super.initialize();
		this.selectedTenantDiscriminatorColumnsModel = this.buildSelectedTenantDiscriminatorColumnsModel();
		this.selectedTenantDiscriminatorColumnModel = this.buildSelectedTenantDiscriminatorColumnModel(this.selectedTenantDiscriminatorColumnsModel);
	}

	private ModifiableCollectionValueModel<ReadOnlyTenantDiscriminatorColumn2_3> buildSelectedTenantDiscriminatorColumnsModel() {
		return new SimpleCollectionValueModel<ReadOnlyTenantDiscriminatorColumn2_3>();
	}

	private PropertyValueModel<ReadOnlyTenantDiscriminatorColumn2_3> buildSelectedTenantDiscriminatorColumnModel(CollectionValueModel<ReadOnlyTenantDiscriminatorColumn2_3> selectedTenantDiscriminatorColumnsModel) {
		return new CollectionPropertyValueModelAdapter<ReadOnlyTenantDiscriminatorColumn2_3, ReadOnlyTenantDiscriminatorColumn2_3>(selectedTenantDiscriminatorColumnsModel) {
			@Override
			protected ReadOnlyTenantDiscriminatorColumn2_3 buildValue() {
				if (this.collectionModel.size() == 1) {
					return this.collectionModel.iterator().next();
				}
				return null;
			}
		};
	}

	@Override
	protected void initializeLayout(Composite container) {
		//see intiailizeLayout2()
	}

	@Override
	public Composite getControl() {
		return (Composite) super.getControl();
	}

	private void initializeLayout2() {
		new AddRemoveListPane<T, ReadOnlyTenantDiscriminatorColumn2_3>(
			this,
			getControl(),
			buildTenantDiscriminatorColumnsAdapter(),
			buildTenantDiscriminatorColumnsListModel(),
			this.selectedTenantDiscriminatorColumnsModel,
			buildTenantDiscriminatorColumnsListLabelProvider(),
			EclipseLinkHelpContextIds.MULTITENANCY_TENANT_DISCRIMINATOR_COLUMNS
		);

		// Property pane
		PageBook pageBook = new PageBook(getControl(), SWT.NULL);
		pageBook.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		installPaneSwitcher(pageBook);
	}

	protected Pane<ReadOnlyTenantDiscriminatorColumn2_3> getTenantDiscriminatorColumnComposite(PageBook pageBook) {
		if (this.tenantDiscriminatorColumnPane == null) {
			this.tenantDiscriminatorColumnPane = this.buildTenantDiscriminatorColumnComposite(pageBook);
		}
		return this.tenantDiscriminatorColumnPane;
	}

	protected Pane<ReadOnlyTenantDiscriminatorColumn2_3> buildTenantDiscriminatorColumnComposite(PageBook pageBook) {
		return new TenantDiscriminatorColumnComposite(
			this,
			this.selectedTenantDiscriminatorColumnModel,
			pageBook
		);
	}

	private void installPaneSwitcher(PageBook pageBook) {
		new ControlSwitcher(this.selectedTenantDiscriminatorColumnModel, buildPaneTransformer(pageBook), pageBook);
	}

	private Transformer<ReadOnlyTenantDiscriminatorColumn2_3, Control> buildPaneTransformer(final PageBook pageBook) {
		return new Transformer<ReadOnlyTenantDiscriminatorColumn2_3, Control>() {
			public Control transform(ReadOnlyTenantDiscriminatorColumn2_3 column) {
				if (column == null) {
					return null;
				}
				return getTenantDiscriminatorColumnComposite(pageBook).getControl();
			}
		};
	}

	String buildTenantDiscriminatorColumnLabel(ReadOnlyTenantDiscriminatorColumn2_3 tenantDiscriminatorColumn) {
		if (tenantDiscriminatorColumn.isVirtual() || tenantDiscriminatorColumn.getSpecifiedName() == null) {
			return NLS.bind(
				EclipseLinkUiDetailsMessages.TenantDiscriminatorColumnComposite_defaultTenantDiscriminatorColumnNameLabel,
				tenantDiscriminatorColumn.getName()
			);
		}
		return tenantDiscriminatorColumn.getName();
	}

	private Adapter<ReadOnlyTenantDiscriminatorColumn2_3> buildTenantDiscriminatorColumnsAdapter() {
		return new AbstractAdapter<ReadOnlyTenantDiscriminatorColumn2_3>() {

			public ReadOnlyTenantDiscriminatorColumn2_3 addNewItem() {
				return TenantDiscriminatorColumnsComposite.this.tenantDiscriminatorColumnsEditor.addTenantDiscriminatorColumn(getSubject());
			}

			@Override
			public boolean hasOptionalButton() {
				return false;
			}

			@Override
			public PropertyValueModel<Boolean> buildRemoveButtonEnabledModel(CollectionValueModel<ReadOnlyTenantDiscriminatorColumn2_3> selectedItemsModel) {
				return buildSingleSelectedItemEnabledModel(selectedItemsModel);
			}

			public void removeSelectedItems(CollectionValueModel<ReadOnlyTenantDiscriminatorColumn2_3> selectedItemsModel) {
				TenantDiscriminatorColumn2_3 column = (TenantDiscriminatorColumn2_3) selectedItemsModel.iterator().next();
				TenantDiscriminatorColumnsComposite.this.tenantDiscriminatorColumnsEditor.removeTenantDiscriminatorColumn(getSubject(), column);
			}
		};
	}

	private ListValueModel<ReadOnlyTenantDiscriminatorColumn2_3> buildTenantDiscriminatorColumnsListModel() {
		return new ItemPropertyListValueModelAdapter<ReadOnlyTenantDiscriminatorColumn2_3>(buildTenantDiscriminatorColumnsListHolder(),
			ReadOnlyNamedColumn.SPECIFIED_NAME_PROPERTY,
			ReadOnlyNamedColumn.DEFAULT_NAME_PROPERTY);
	}

	private ListValueModel<ReadOnlyTenantDiscriminatorColumn2_3> buildTenantDiscriminatorColumnsListHolder() {
		List<ListValueModel<ReadOnlyTenantDiscriminatorColumn2_3>> list = new ArrayList<ListValueModel<ReadOnlyTenantDiscriminatorColumn2_3>>();
		list.add(buildDefaultTenantDiscriminatorColumnListHolder());
		list.add(buildSpecifiedTenantDiscriminatorColumnsListHolder());
		return CompositeListValueModel.forModels(list);
	}

	private ListValueModel<ReadOnlyTenantDiscriminatorColumn2_3> buildSpecifiedTenantDiscriminatorColumnsListHolder() {
		return new ListAspectAdapter<T, ReadOnlyTenantDiscriminatorColumn2_3>(getSubjectHolder(), this.tenantDiscriminatorColumnsEditor.getSpecifiedTenantDiscriminatorsListPropertyName()) {
			@Override
			protected ListIterable<ReadOnlyTenantDiscriminatorColumn2_3> getListIterable() {
				return TenantDiscriminatorColumnsComposite.this.tenantDiscriminatorColumnsEditor.getSpecifiedTenantDiscriminatorColumns(this.subject);
			}

			@Override
			protected int size_() {
				return TenantDiscriminatorColumnsComposite.this.tenantDiscriminatorColumnsEditor.getSpecifiedTenantDiscriminatorColumnsSize(this.subject);
			}
		};
	}

	private ListValueModel<ReadOnlyTenantDiscriminatorColumn2_3> buildDefaultTenantDiscriminatorColumnListHolder() {
		return new ListAspectAdapter<T, ReadOnlyTenantDiscriminatorColumn2_3>(getSubjectHolder(), this.tenantDiscriminatorColumnsEditor.getDefaultTenantDiscriminatorsListPropertyName()) {
			@Override
			protected ListIterable<ReadOnlyTenantDiscriminatorColumn2_3> getListIterable() {
				return TenantDiscriminatorColumnsComposite.this.tenantDiscriminatorColumnsEditor.getDefaultTenantDiscriminatorColumns(this.subject);
			}

			@Override
			protected int size_() {
				return TenantDiscriminatorColumnsComposite.this.tenantDiscriminatorColumnsEditor.getDefaultTenantDiscriminatorColumnsSize(this.subject);
			}
		};
	}

	private ILabelProvider buildTenantDiscriminatorColumnsListLabelProvider() {
		return new LabelProvider() {
			@Override
			public String getText(Object element) {
				return buildTenantDiscriminatorColumnLabel((ReadOnlyTenantDiscriminatorColumn2_3) element);
			}
		};
	}

	/**
	 * The editor is used to complete the behavior of this pane.
	 */
	public static interface TenantDiscriminatorColumnsEditor<T> {

		/**
		 * Add a tenant discriminator column to the given subject
		 */
		ReadOnlyTenantDiscriminatorColumn2_3 addTenantDiscriminatorColumn(T subject);

		/**
		 * Return whether the subject has specified tenant discriminator columns
		 */
		boolean hasSpecifiedTenantDiscriminatorColumns(T subject);

		/**
		 * Return the specified tenant discriminator from the given subject
		 */
		ListIterable<ReadOnlyTenantDiscriminatorColumn2_3> getSpecifiedTenantDiscriminatorColumns(T subject);

		/**
		 * Return the number of specified join columns on the given subject
		 */
		int getSpecifiedTenantDiscriminatorColumnsSize(T subject);

		/**
		 * Return the default tenant discriminator columns from the given subject or null.
		 */
		ListIterable<ReadOnlyTenantDiscriminatorColumn2_3> getDefaultTenantDiscriminatorColumns(T subject);

		/**
		 * Return the number of default tenant discriminator on the given subject
		 */
		int getDefaultTenantDiscriminatorColumnsSize(T subject);

		/**
		 * Return the property name of the specified tenant discriminator columns list
		 */
		String getSpecifiedTenantDiscriminatorsListPropertyName();

		/**
		 * Return the property name of the default tenant discriminator columns list
		 */
		String getDefaultTenantDiscriminatorsListPropertyName();

		/**
		 * Remove the tenant discriminator column from the subject
		 */
		void removeTenantDiscriminatorColumn(T subject, ReadOnlyTenantDiscriminatorColumn2_3 column);
	}
}