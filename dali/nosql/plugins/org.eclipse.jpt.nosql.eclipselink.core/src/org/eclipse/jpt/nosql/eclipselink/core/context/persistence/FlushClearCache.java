/*******************************************************************************
* Copyright (c) 2008, 2012 Oracle. All rights reserved.
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License v1.0, which accompanies this distribution
* and is available at http://www.eclipse.org/legal/epl-v10.html.
* 
* Contributors:
*     Oracle - initial API and implementation
*******************************************************************************/
package org.eclipse.jpt.nosql.eclipselink.core.context.persistence;

import org.eclipse.jpt.jpa.core.context.persistence.PersistenceXmlEnumValue;

/**
 *  FlushClearCache
 */
public enum FlushClearCache implements PersistenceXmlEnumValue {
	drop("Drop"), //$NON-NLS-1$
	drop_invalidate("DropInvalidate"), //$NON-NLS-1$
	merge("Merge");  //$NON-NLS-1$

	private final String propertyValue;

	FlushClearCache(String propertyValue) {
		this.propertyValue = propertyValue;
	}

	/**
	 * The string used as the property value in the persistence.xml
	 */
	public String getPropertyValue() {
		return this.propertyValue;
	}
}
