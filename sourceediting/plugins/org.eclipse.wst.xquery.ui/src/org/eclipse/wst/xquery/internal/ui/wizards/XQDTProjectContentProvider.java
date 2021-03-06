/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xquery.internal.ui.wizards;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.wst.xquery.core.XQDTNature;

public class XQDTProjectContentProvider implements ITreeContentProvider {

    private boolean showClosedProjects = true;

    /**
     * Creates a new ContainerContentProvider.
     */
    public XQDTProjectContentProvider() {
    }

    /**
     * The visual part that is using this content provider is about to be disposed. Deallocate all
     * allocated SWT resources.
     */
    public void dispose() {
    }

    /*
     * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    public Object[] getChildren(Object element) {
        if (element instanceof IWorkspace) {
            // check if closed projects should be shown
            IProject[] allProjects = ((IWorkspace)element).getRoot().getProjects();
            List<IProject> xqdtProjects = new ArrayList<IProject>();
            for (int i = 0; i < allProjects.length; i++) {
                IProject project = allProjects[i];
                try {
                    if (project.hasNature(XQDTNature.NATURE_ID)) {
                        xqdtProjects.add(project);
                    }
                } catch (CoreException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            allProjects = xqdtProjects.toArray(new IProject[xqdtProjects.size()]);
            if (showClosedProjects) {
                return allProjects;
            }

            ArrayList accessibleProjects = new ArrayList();
            for (int i = 0; i < allProjects.length; i++) {
                if (allProjects[i].isOpen()) {
                    accessibleProjects.add(allProjects[i]);
                }
            }
            return accessibleProjects.toArray();
        } else if (element instanceof IContainer) {
            IContainer container = (IContainer)element;
            if (container.isAccessible()) {
                try {
                    List children = new ArrayList();
                    IResource[] members = container.members();
                    for (int i = 0; i < members.length; i++) {
                        if (members[i].getType() != IResource.FILE) {
                            children.add(members[i]);
                        }
                    }
                    return children.toArray();
                } catch (CoreException e) {
                    // this should never happen because we call #isAccessible before invoking
                    // #members
                }
            }
        }
        return new Object[0];
    }

    /*
     * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
     */
    public Object[] getElements(Object element) {
        return getChildren(element);
    }

    /*
     * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
     */
    public Object getParent(Object element) {
        if (element instanceof IResource) {
            return ((IResource)element).getParent();
        }
        return null;
    }

    /*
     * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
     */
    public boolean hasChildren(Object element) {
        return getChildren(element).length > 0;
    }

    /*
     * @see org.eclipse.jface.viewers.IContentProvider#inputChanged
     */
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    }

    /**
     * Specify whether or not to show closed projects in the tree viewer. Default is to show closed
     * projects.
     * 
     * @param show
     *            boolean if false, do not show closed projects in the tree
     */
    public void showClosedProjects(boolean show) {
        showClosedProjects = show;
    }
}
