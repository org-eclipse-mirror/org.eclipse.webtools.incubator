/*******************************************************************************
 * Copyright (c) 2002, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * 		IBM Corporation, Jens Lukowski/Innoopract - original implementation of 
 * 			the XMLCatalogEntriesView
 * 		Martin Schmied - customizations for the RELAX NG Bindings preference page
 *******************************************************************************/
package org.eclipse.wst.rng.ui.internal.preferences;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.rng.core.internal.binding.PluginSchemaBindings;
import org.eclipse.wst.rng.core.internal.binding.RngSchemaBinding;
import org.eclipse.wst.rng.core.internal.binding.UserSchemaBindings;


class RngSchemaBindingView extends Composite implements UserSchemaBindings.BindingChangedListener {
	private UserSchemaBindings userBindings;
	
	private PluginSchemaBindings pluginBindings;

	private RngSchemaBindingTreeViewer bindingViewer;
	
	private List<SimpleRngBindingSelectionObserver> selectionObservers = new LinkedList<SimpleRngBindingSelectionObserver>();
	
	private Button newButton;
	
	private Button editButton;
	
	private Button removeButton;
	
	RngSchemaBindingView(Composite parent, UserSchemaBindings userBindings, PluginSchemaBindings pluginBindings) {
		super(parent, SWT.NONE);
		this.userBindings = userBindings;
		this.pluginBindings = pluginBindings;
		
		createControls();
	}
	
	private void createControls() {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		this.setLayout(gridLayout);

		bindingViewer = new RngSchemaBindingTreeViewer(this, userBindings, pluginBindings);
		bindingViewer.setInput("dummy"); //$NON-NLS-1$
		
		Point initialSize = bindingViewer.getTree().computeSize(SWT.DEFAULT, SWT.DEFAULT);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.widthHint = initialSize.x;
		gridData.heightHint = initialSize.y;
		bindingViewer.getControl().setLayoutData(gridData);
		
		bindingViewer.expandToLevel(2);
		bindingViewer.reveal(RngSchemaBindingTreeViewer.USER_SPECIFIED_ENTRIES_OBJECT);
		
		bindingViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			// @Override
			public void selectionChanged(SelectionChangedEvent event) {
				ISelection selection = bindingViewer.getSelection();
				Set<RngSchemaBinding> selectedBindings = extractSelectedBindings(selection, true);
				if (selectedBindings.isEmpty()) {
					fireSelectionCleared();
					editButton.setEnabled(false);
					removeButton.setEnabled(false);
				} else if (selectedBindings.size() == 1) {
					fireSelectionChanged(selectedBindings.iterator().next());
					editButton.setEnabled(true);
					removeButton.setEnabled(true);
				} else {
					fireSelectionChanged(selectedBindings);
					editButton.setEnabled(false);
					removeButton.setEnabled(true);
				}
			}
		});
		
		bindingViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			// @Override
			public void selectionChanged(SelectionChangedEvent event) {
				ISelection selection = bindingViewer.getSelection();
				Set<RngSchemaBinding> selectedBindings = extractSelectedBindings(selection, false);
				if (selectedBindings.isEmpty()) {
					fireSelectionCleared();
				} else if (selectedBindings.size() == 1) {
					fireSelectionChanged(selectedBindings.iterator().next());
				} else {
					fireSelectionChanged(selectedBindings);
				}
			}
		});
		
		userBindings.addChangeListener(this);
		
		createButtons(this);
	}
	
	/*
	 * Extracts selected Schema Bindings ... when constrainToUserBindings flag is true, then if there element
	 * other than User defined binding in the selection, empty set is returned
	 */
	@SuppressWarnings("unchecked")
	private Set<RngSchemaBinding> extractSelectedBindings(ISelection selection, boolean constrainToUserBindings) {
		if (!(selection instanceof IStructuredSelection)) {
			return Collections.emptySet();
		}
		IStructuredSelection structuredSelection = (IStructuredSelection) selection;
		Iterator<Object> i = structuredSelection.iterator();
		Set<RngSchemaBinding> selectedBindings = new HashSet<RngSchemaBinding>();
		while (i.hasNext()) {
			Object o = i.next();
			if (o instanceof RngSchemaBinding) {
				RngSchemaBinding binding = (RngSchemaBinding) o;
				if (!constrainToUserBindings || userBindings.contains(binding.getNamespace())) {
					selectedBindings.add(binding);
				} else {
					return Collections.emptySet();
				}						
			} else {
				return Collections.emptySet();
			}
		}
		return selectedBindings;
	}
	
	private void createButtons(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridData gd = new GridData();
		gd.verticalAlignment = GridData.BEGINNING;
		composite.setLayoutData(gd);
		GridLayout gridLayout = new GridLayout();
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		gridLayout.marginLeft = 3;
		composite.setLayout(gridLayout);


		// add the "New..." button
		gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;

		newButton = new Button(composite, SWT.NONE);
		newButton.setText("Add...");
		newButton.setLayoutData(gd);
		newButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				performNew();
			}
		});

		// add the "Edit..." button
		gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		editButton = new Button(composite, SWT.NONE);
		editButton.setText("Edit...");
		editButton.setLayoutData(gd);
		editButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				performEdit();
			}
		});

		// add the "Remove" button
		gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		removeButton = new Button(composite, SWT.NONE);
		removeButton.setText("Remove");
		removeButton.setLayoutData(gd);
		removeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				performRemove();
			}
		});
		
		gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalSpan = 4;

		Button hiddenButton = new Button(composite, SWT.NONE);
		hiddenButton.setLayoutData(gd);
		hiddenButton.setVisible(false);
		hiddenButton.setEnabled(false);

		// a cruddy hack so that the PreferenceDialog doesn't close every time
		// we press 'enter'
		getShell().setDefaultButton(hiddenButton);
	}
	
	private void performNew() {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		EditRngSchemaBindingDialog dialog = new EditRngSchemaBindingDialog(shell, userBindings);
		dialog.create();
		dialog.getShell().setText("Add RELAX NG Schema Binding");
		dialog.open();
	}
	
	private void performEdit() {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		EditRngSchemaBindingDialog dialog = new EditRngSchemaBindingDialog(shell, userBindings, extractSelectedBindings(bindingViewer.getSelection(), true).iterator().next());
		dialog.create();
		dialog.getShell().setText("Edit RELAX NG Schema Binding");
		dialog.open();
	}
	
	private void performRemove() {
		Set<RngSchemaBinding> selectedBindings = extractSelectedBindings(bindingViewer.getSelection(), true);
		userBindings.removeBindings(selectedBindings);
	}
	
	// @Override
	public void onBindingChanged(UserSchemaBindings binding) {
		bindingViewer.refresh(RngSchemaBindingTreeViewer.USER_SPECIFIED_ENTRIES_OBJECT);
	}
	
	public void addSelectionObserver(SimpleRngBindingSelectionObserver observer) {
		selectionObservers.add(observer);
	}
	
	public boolean removeSelectionObserver(SimpleRngBindingSelectionObserver observer) {
		return selectionObservers.remove(observer);
	}
	
	private void fireSelectionChanged(RngSchemaBinding binding) {
		for (SimpleRngBindingSelectionObserver observer : selectionObservers) {
			observer.onSingleSelection(binding);
		}
	}
	
	private void fireSelectionChanged(Set<RngSchemaBinding> bindings) {
		for (SimpleRngBindingSelectionObserver observer : selectionObservers) {
			observer.onMultiSelection(bindings);
		}
	}
	
	private void fireSelectionCleared() {
		for (SimpleRngBindingSelectionObserver observer : selectionObservers) {
			observer.onSelectionCleared();
		}
	}
	
	static interface SimpleRngBindingSelectionObserver {
		void onSelectionCleared();
		
		void onSingleSelection(RngSchemaBinding binding);
		
		void onMultiSelection(Set<RngSchemaBinding> bindings);
	}
	
	@Override
	public void dispose() {
		userBindings.removeChangeListener(this);
	}
}